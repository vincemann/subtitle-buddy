package gui;

import com.google.inject.Key;
import com.youneedsoftware.subtitleBuddy.classpathFileFinder.SpringClassPathFileFinder;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.ApachePropertiesFile;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertiesFile;
import com.youneedsoftware.subtitleBuddy.config.uiStringsFile.ApacheUIStringsFile;
import com.youneedsoftware.subtitleBuddy.config.uiStringsFile.UIStringsFile;
import com.youneedsoftware.subtitleBuddy.guice.module.*;
import com.youneedsoftware.subtitleBuddy.main.Main;
import com.youneedsoftware.subtitleBuddy.util.LoggingUtils;
import constants.TestConstants;
import guice.IntegrationTest;
import guice.modules.mockModules.MockFileChooserModule;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxToolkit.registerPrimaryStage;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

public abstract class GuiTest extends ApplicationTest implements IntegrationTest {

    private static final long GUI_TEST_TIME_OUT = 3000;

    @Getter
    @Setter
    private static class Result{
        private boolean done = false;
    }

    @BeforeClass
    public static void setupSpec() throws Exception {
        //muss als cli arg reingepasst werden, dann wird headless getestet (klappt nicht bei linux)
        if (Boolean.getBoolean("headless")) {
            System.out.println("running test in headless mode");
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
        registerPrimaryStage();
    }

    @Before
    public void setUpClass() throws Exception {
        LoggingUtils.disableUtilLogger();
        ApplicationTest.launch(Main.class);
    }

    public void focusNode(String query) throws TimeoutException {
        Node node = find(query);
        Runnable runnable = node::requestFocus;
        doOnFxThreadAndWait(runnable);
        refreshGui();
    }

    protected void doOnFxThreadAndWait(Runnable task) throws TimeoutException {
        long startTime = System.nanoTime();
        final Result result = new Result();
        Runnable runnable = () -> {
            task.run();
            result.setDone(true);
        };
        Platform.runLater(runnable);
        while (true){
            if(result.isDone()){
                break;
            }
            long currTime = System.nanoTime();
            if(currTime-startTime>=GUI_TEST_TIME_OUT*1000000){
                throw new TimeoutException();
            }
            sleep(50);
        }
    }

    public void refreshGui(){
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void start(Stage stage) throws Exception {
        PropertiesFile testPropertiesFile = new ApachePropertiesFile(new File(TestConstants.TEST_PROPERTIES_FILE_PATH));
        //use original strings constants file
        UIStringsFile testStringsFile = new ApacheUIStringsFile(new File(Main.UI_STRINGS_CONFIG_FILE_PATH));
        //set all modules for integration test, mock those that need to be mocked
        Main.createInjector(
                new ClassPathFileFinderModule(new SpringClassPathFileFinder()),
                new ConfigFileModule(testPropertiesFile, testStringsFile),
                new MockFileChooserModule(testStringsFile,testPropertiesFile),
                new ParserModule(testStringsFile, testPropertiesFile) ,
                new GuiModule(testStringsFile, testPropertiesFile,stage),
                new UserInputHandlerModule() ,
                new SystemCommandModule() ,
                new OsModule()
        );
        stage.show();
    }

    /**
     * uses application injector to find an instance of class
     * @param type
     * @param <T>
     * @return
     */
    <T> T getInstance(Class<T> type) {
        final Key<T> key = Key.get(type);
        return getApplicationInjector().getInstance(key);
    }

    /**
     * uses application injector to find an instance annotated with @param option
     * @param type
     * @param option
     * @param <T>
     * @return
     */
    <T> T getInstance(Class<T> type, Class<? extends Annotation> option) {
        final Key<T> key = Key.get(type, option);
        return getApplicationInjector().getInstance(key);
    }


    @After
    public void afterEachTest() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    /**
     * Waits for the {@code Node} that is looked up by the given {@code NodeQuery} to be visible.
     * <p>
     * This method handles the case where the node lookup first returns {@literal null} gracefully.
     * <p>
     * Example:
     * <pre>{@code
     * WaitForAsyncUtils.waitForVisibleNode("#someNode", 15, TimeUnit.SECONDS, fxRobot);
     * }</pre>
     */
    public void waitForVisibleNode(String nodeQuery)
            throws TimeoutException {
        // First we wait for the node lookup to be non-null. Then, in the remaining time, wait for the node to be visible.
        waitFor(GUI_TEST_TIME_OUT/2, TimeUnit.MILLISECONDS, () -> {
            return lookup(nodeQuery).query()!=null;

        });
        waitFor(GUI_TEST_TIME_OUT/2,
                TimeUnit.MILLISECONDS, () -> lookup(nodeQuery).query().isVisible());
    }

    public <T> T find(final String query){
        return (T) lookup(query).queryAll().iterator().next();
    }
}
