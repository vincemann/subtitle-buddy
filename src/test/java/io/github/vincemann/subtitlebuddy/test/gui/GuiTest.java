package io.github.vincemann.subtitlebuddy.test.gui;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import io.github.vincemann.subtitlebuddy.Main;
import io.github.vincemann.subtitlebuddy.config.strings.ApacheUIStringsFile;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractorImpl;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.Windows;
import io.github.vincemann.subtitlebuddy.gui.WindowManager;
import io.github.vincemann.subtitlebuddy.listeners.key.GlobalHotKeyListener;
import io.github.vincemann.subtitlebuddy.module.*;
import io.github.vincemann.subtitlebuddy.options.ApachePropertiesFile;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;
import io.github.vincemann.subtitlebuddy.test.TestFiles;
import io.github.vincemann.subtitlebuddy.test.guice.MockFileChooserModule;
import io.github.vincemann.subtitlebuddy.util.LoggingUtils;
import javafx.application.Platform;
import javafx.geometry.Point2D;
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
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static io.github.vincemann.subtitlebuddy.util.fx.FxThreadUtils.runOnFxThreadAndWait;
import static org.testfx.api.FxToolkit.registerPrimaryStage;

public abstract class GuiTest extends ApplicationTest {

//    private static final long GUI_TEST_TIME_OUT = 3000;

    private WindowManager windowManager;

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



    public void clickNextToSettingsStage() {
        Stage settingsStage = findStage(Windows.SETTINGS);
        Point2D nextToSettingsStage = new Point2D(settingsStage.getX() + settingsStage.getWidth() + 10, settingsStage.getY() + 10);
        clickOn(nextToSettingsStage);
        refreshGui();
    }

    public void clickOnStage(String name){
        Stage settingsStage = findStage(name);
        Point2D middleOfStage = new Point2D(settingsStage.getX() + settingsStage.getWidth()/2, settingsStage.getY() + settingsStage.getHeight()/2);
        clickOn(middleOfStage);
    }

    // mac has stricter focus policies, so sometimes I need to click on the window, to tell mac its the active window and may
    // request focus
    public void safeFocusStage(String name){
        if (runningOnMac())
            focusStage(name,true);
        else
            focusStage(name);
    }

    @Before
    public void beforeEach() throws Exception {
        LoggingUtils.disableUtilLogger();

        windowManager = getInstance(WindowManager.class);

        Main application = (Main) ApplicationTest.launch(Main.class);

        // Explicitly wait for the application to be ready
        WaitForAsyncUtils.waitForFxEvents();
        waitUntilApplicationReady(application);
    }


    // only needed for jnativehook 2.2.2
    private void waitUntilApplicationReady(Main application) throws TimeoutException {
        // Use a polling mechanism to wait for ready property to become true
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, application::isReady);
    }


    public void focusNode(String query) throws TimeoutException {
        Node node = find(query);
        Runnable runnable = node::requestFocus;
        runOnFxThreadAndWait(runnable);
        refreshGui();
    }

    // needs to be pressed natively, bc robot works on a more abstract layer of keystrokes in javafx
    public void typeAltN(){
        if (runningOnMac()){
            typeNativeAltN();
        }
        else {
            press(KeyCode.ALT).type(KeyCode.N).release(KeyCode.ALT);
        }
    }

    public boolean runningOnMac(){
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("mac");
    }

    public void typeAltEscape(){
        if (runningOnMac()){
            typeNativeAltEscape();
        }
        else {
            press(KeyCode.ALT).type(KeyCode.ESCAPE).release(KeyCode.ALT);
        }
    }

    private void typeNativeAltEscape(){
        GlobalHotKeyListener hotKeyListener = getInstance(GlobalHotKeyListener.class);
        // Simulate pressing Alt
        NativeKeyEvent altPressed = new NativeKeyEvent(
                NativeKeyEvent.NATIVE_KEY_PRESSED,
                0,  // Modifiers (none)
                0, // Raw code can be left as 0 if not specifically handled
                NativeKeyEvent.VC_ALT,
                NativeKeyEvent.CHAR_UNDEFINED,
                NativeKeyEvent.KEY_LOCATION_LEFT
        );
        hotKeyListener.nativeKeyPressed(altPressed);

        // Simulate pressing N
        NativeKeyEvent escapePressed = new NativeKeyEvent(
                NativeKeyEvent.NATIVE_KEY_PRESSED,
                NativeKeyEvent.ALT_MASK, // Modifier to indicate Alt is pressed
                0, // Raw code for Escape, again, can be 0 if not handled
                NativeKeyEvent.VC_ESCAPE,
                NativeKeyEvent.CHAR_UNDEFINED,
                NativeKeyEvent.KEY_LOCATION_STANDARD
        );
        hotKeyListener.nativeKeyPressed(escapePressed);

        // Optionally release keys...
        hotKeyListener.nativeKeyReleased(altPressed);
        hotKeyListener.nativeKeyReleased(escapePressed);
    }


    private void typeNativeAltN(){
        GlobalHotKeyListener hotKeyListener = getInstance(GlobalHotKeyListener.class);
        // Simulate pressing Alt
        NativeKeyEvent altPressed = new NativeKeyEvent(
                NativeKeyEvent.NATIVE_KEY_PRESSED,
                0,  // Modifiers (none)
                0, // Raw code can be left as 0 if not specifically handled
                NativeKeyEvent.VC_ALT,
                NativeKeyEvent.CHAR_UNDEFINED,
                NativeKeyEvent.KEY_LOCATION_LEFT
        );
        hotKeyListener.nativeKeyPressed(altPressed);

        // Simulate pressing N
        NativeKeyEvent nPressed = new NativeKeyEvent(
                NativeKeyEvent.NATIVE_KEY_PRESSED,
                NativeKeyEvent.ALT_MASK, // Modifier to indicate Alt is pressed
                0,
                NativeKeyEvent.VC_N,
                'N', // Char representation of N
                NativeKeyEvent.KEY_LOCATION_STANDARD
        );
        hotKeyListener.nativeKeyPressed(nPressed);

        // Optionally release keys...
        hotKeyListener.nativeKeyReleased(altPressed);
        hotKeyListener.nativeKeyReleased(nPressed);
    }



    public boolean isStageShowing(String name) {
//        AbstractStageController abstractStageController = getInstance(AbstractStageController.class, a);
//        return abstractStageController.getStageState().equals(StageState.OPEN);
        Stage stage = findStage(name);
        return stage.isShowing();
    }

    /**
     * Tests Focus stage method.
     * I am not using {@link WindowManager} here bc I need to wait until focused for testing.
     * Optional parameter move, only relevant for mac.
     * Need to move cursor to stage, bc on mac can only focus when cursor hovers above it.
     * @param name class of stage to focus
     */
    public void focusStage(String name, Boolean... move) {
        if (move.length > 0 && move[0])
            clickOnStage(name);
        Stage stage = findStage(name);
        Runnable toFrontTask = () -> {
            stage.toFront();
            stage.requestFocus();
        };
        runOnFxThreadAndWait(toFrontTask);
        refreshGui();
        if (!stage.isFocused()) {
            throw new IllegalStateException("Stage is not focused");
        }
    }

    public Stage findStage(String name){
        return windowManager.findWindow(name).getStage();
    }

    public SrtDisplayer findSrtDisplayer(Class<? extends SrtDisplayer> modeAnnotation) {
        return getInstance(modeAnnotation);
    }

    public void refreshGui() {
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        PropertiesFile testPropertiesFile = new ApachePropertiesFile(new File(TestFiles.TEST_PROPERTIES_FILE_PATH));
        // use original strings constants file
        UIStringsFile testStringsFile = new ApacheUIStringsFile(new File(Main.UI_STRINGS_FILE_PATH));
        // set all modules for integration test, mock those that need to be mocked
        Injector testInjector = Guice.createInjector(Arrays.asList(
                new ClassPathFileModule(new ClassPathFileExtractorImpl()),
                new ConfigFileModule(testPropertiesFile, testStringsFile),
                new MockFileChooserModule(testStringsFile, testPropertiesFile),
                new FontModule(testStringsFile,testPropertiesFile),
                new ParserModule(testStringsFile, testPropertiesFile),
                new GuiModule(testStringsFile, testPropertiesFile),
                new UserInputHandlerModule())
        );
        Main.setInjector(testInjector);

        primaryStage.show();
    }

    /**
     * uses application injector to find an instance of class
     *
     * @param type
     * @param <T>
     * @return
     */
    <T> T getInstance(Class<T> type) {
        final Key<T> key = Key.get(type);
        return Main.getInjector().getInstance(key);
    }

    /**
     * uses application injector to find an instance annotated with @param option
     *
     * @param type
     * @param option
     * @param <T>
     * @return
     */
    <T> T getInstance(Class<T> type, Class<? extends Annotation> option) {
        final Key<T> key = Key.get(type, option);
        return Main.getInjector().getInstance(key);
    }


    @After
    public void afterEachTest() throws TimeoutException {
        Main app = getInstance(Main.class);
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
        app.unregisterListeners();
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
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T find(final String query) {
        return (T) lookup(query).queryAll().iterator().next();
    }
}
