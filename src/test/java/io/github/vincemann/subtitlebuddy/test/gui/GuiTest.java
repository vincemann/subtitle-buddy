package io.github.vincemann.subtitlebuddy.test.gui;

import com.google.inject.Key;
import io.github.vincemann.subtitlebuddy.test.TestFiles;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractorImpl;
import io.github.vincemann.subtitlebuddy.properties.ApachePropertiesFile;
import io.github.vincemann.subtitlebuddy.properties.PropertiesFile;
import io.github.vincemann.subtitlebuddy.config.strings.ApacheUIStringsFile;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.gui.srtdisplayer.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.stages.StageState;
import io.github.vincemann.subtitlebuddy.gui.stages.controller.AbstractStageController;
import io.github.vincemann.subtitlebuddy.test.guice.IntegrationTest;
import io.github.vincemann.subtitlebuddy.test.guice.MockFileChooserModule;
import io.github.vincemann.subtitlebuddy.Main;
import io.github.vincemann.subtitlebuddy.module.*;
import io.github.vincemann.subtitlebuddy.util.LoggingUtils;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
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


    private static class Result{
        private boolean done = false;

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }
    }

    @BeforeClass
    public static void setupSpec() throws Exception {
//        // cli arg
//        if (Boolean.getBoolean("headless")) {
//            System.out.println("running test in headless mode");
//            System.setProperty("testfx.robot", "glass");
//            System.setProperty("testfx.headless", "true");
//            System.setProperty("prism.order", "sw");
//            System.setProperty("prism.text", "t2k");
//            System.setProperty("java.awt.headless", "true");
//        }
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

    public boolean isStageShowing(Class<? extends Annotation> a){
        AbstractStageController abstractStageController = getInstance(AbstractStageController.class,a);
        return abstractStageController.getStageState().equals(StageState.OPEN);
    }

    /**
     * holt stage to front und returned erst wenn stage @ front ist
     * @param a
     */
    public void focusStage(Class<? extends Annotation> a) throws TimeoutException {
        AbstractStageController abstractStageController = getInstance(AbstractStageController.class,a);
        Runnable toFrontTask = () -> {
            abstractStageController.getStage().toFront();
            abstractStageController.getStage().requestFocus();
        };
        doOnFxThreadAndWait(toFrontTask);
        refreshGui();
        if(!abstractStageController.getStage().isFocused()){
            throw new IllegalStateException("Stage is not focused");
        }
    }

    public AbstractStageController  findStageController(Class<? extends Annotation> stageAnnotation) {
        return getInstance(AbstractStageController.class, stageAnnotation);
    }

    public SrtDisplayer findSrtDisplayer(Class<? extends SrtDisplayer> modeAnnotation){
        return getInstance(modeAnnotation);
    }

    public void refreshGui(){
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void start(Stage stage) throws Exception {
        PropertiesFile testPropertiesFile = new ApachePropertiesFile(new File(TestFiles.TEST_PROPERTIES_FILE_PATH));
        //use original strings constants file
        UIStringsFile testStringsFile = new ApacheUIStringsFile(new File(Main.UI_STRINGS_CONFIG_FILE_PATH));
        //set all modules for integration test, mock those that need to be mocked
        Main.createInjector(
                new ClassPathFileExtractorModule(new ClassPathFileExtractorImpl()),
                new ConfigFileModule(testPropertiesFile, testStringsFile),
                new MockFileChooserModule(testStringsFile,testPropertiesFile),
                new ParserModule(testStringsFile, testPropertiesFile) ,
                new GuiModule(testStringsFile, testPropertiesFile,stage),
                new UserInputHandlerModule()
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
        System.err.println("hiding stage");
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
