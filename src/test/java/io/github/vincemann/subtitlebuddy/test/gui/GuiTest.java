package io.github.vincemann.subtitlebuddy.test.gui;

import com.google.inject.Key;
import io.github.vincemann.subtitlebuddy.Main;
import io.github.vincemann.subtitlebuddy.config.strings.ApacheUIStringsFile;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractorImpl;
import io.github.vincemann.subtitlebuddy.gui.srtdisplayer.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.stages.StageState;
import io.github.vincemann.subtitlebuddy.gui.stages.controller.AbstractStageController;
import io.github.vincemann.subtitlebuddy.module.*;
import io.github.vincemann.subtitlebuddy.properties.ApachePropertiesFile;
import io.github.vincemann.subtitlebuddy.properties.PropertiesFile;
import io.github.vincemann.subtitlebuddy.test.TestFiles;
import io.github.vincemann.subtitlebuddy.test.guice.IntegrationTest;
import io.github.vincemann.subtitlebuddy.test.guice.MockFileChooserModule;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxToolkit.registerPrimaryStage;

public abstract class GuiTest extends ApplicationTest implements IntegrationTest {

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
        Main application = (Main) ApplicationTest.launch(Main.class);

        // Explicitly wait for the application to be ready
        WaitForAsyncUtils.waitForFxEvents();

        waitUntilJNativeHookListenersAdded(application);

    }

    private void waitUntilJNativeHookListenersAdded(Main application){
        // wait until jnativehook is registered
        FutureTask<Boolean> readyTask = new FutureTask<>(() -> application.readyProperty().get());

        // Submit the task to be run on the JavaFX thread and wait for it
        WaitForAsyncUtils.asyncFx(readyTask);
        WaitForAsyncUtils.waitFor(readyTask);
        assert application.isReady();
    }


    public void focusNode(String query) {
        Node node = find(query);
        Runnable runnable = node::requestFocus;
        doOnFxThreadAndWait(runnable);
        refreshGui();
    }

    protected void doOnFxThreadAndWait(Runnable task) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Platform.runLater(() -> {
            try {
                task.run();
                future.complete(null);  // Signal completion
            } catch (Exception e) {
                future.completeExceptionally(e);  // Propagate error
            }
        });

        try {
            future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());  // Throw the cause of the exception
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Handle thread interruption
            throw new RuntimeException("Interrupted while waiting", e);
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
    public void focusStage(Class<? extends Annotation> a) {
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
    public void waitForVisibleNode(String nodeQuery) {
        try {
            CompletableFuture<Void> nodeExistsFuture = new CompletableFuture<>();
            CompletableFuture<Void> nodeVisibleFuture = new CompletableFuture<>();

            // Check for node existence
            Platform.runLater(() -> {
                try {
                    if (lookup(nodeQuery).query() != null) {
                        nodeExistsFuture.complete(null);  // Node exists
                    } else {
                        nodeExistsFuture.completeExceptionally(new IllegalStateException("Node does not exist: " + nodeQuery));
                    }
                } catch (Exception e) {
                    nodeExistsFuture.completeExceptionally(e);
                }
            });

            // Wait for node existence without a timeout
            nodeExistsFuture.get();  // This will block until the future is completed

            // Check for node visibility
            Platform.runLater(() -> {
                try {
                    if (lookup(nodeQuery).query().isVisible()) {
                        nodeVisibleFuture.complete(null);  // Node is visible
                    } else {
                        nodeVisibleFuture.completeExceptionally(new IllegalStateException("Node is not visible: " + nodeQuery));
                    }
                } catch (Exception e) {
                    nodeVisibleFuture.completeExceptionally(e);
                }
            });

            // Wait for node visibility without a timeout
            nodeVisibleFuture.get();  // This will block until the future is completed
        }catch (InterruptedException| ExecutionException e){
            throw new RuntimeException(e);
        }
    }

    public <T> T find(final String query){
        return (T) lookup(query).queryAll().iterator().next();
    }
}
