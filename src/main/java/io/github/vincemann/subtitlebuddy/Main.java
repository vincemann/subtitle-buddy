package io.github.vincemann.subtitlebuddy;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectoryImpl;
import io.github.vincemann.subtitlebuddy.config.ConfigFileManager;
import io.github.vincemann.subtitlebuddy.config.ConfigFileManagerImpl;
import io.github.vincemann.subtitlebuddy.config.strings.ApacheUIStringsFile;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractor;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractorImpl;
import io.github.vincemann.subtitlebuddy.events.EventHandlerRegistrar;
import io.github.vincemann.subtitlebuddy.gui.stages.controller.SettingsStageController;
import io.github.vincemann.subtitlebuddy.listeners.JLinkJNativeLibraryLocator;
import io.github.vincemann.subtitlebuddy.listeners.key.GlobalHotKeyListener;
import io.github.vincemann.subtitlebuddy.listeners.mouse.GlobalMouseListener;
import io.github.vincemann.subtitlebuddy.module.*;
import io.github.vincemann.subtitlebuddy.properties.ApachePropertiesFile;
import io.github.vincemann.subtitlebuddy.properties.PropertiesFile;
import io.github.vincemann.subtitlebuddy.srt.engine.SrtParserEngine;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.List;

@Log4j2
@NoArgsConstructor
@Singleton
public class Main extends Application {

    static {
        if ("true".equals(System.getProperty("jlink"))) {
            // fixing lib loading issues within jlink image
            log.info("Setting Jlink-specific library locator for jNativeHook");
            System.setProperty("jnativehook.lib.locator", JLinkJNativeLibraryLocator.class.getName());
        } else {
            // for fat jars the default locator is fine
            log.info("Using default library locator for jNativeHook");
        }
    }

    public static final String CONFIG_FILE_NAME = "application.properties";
    public static final String UI_STRINGS_FILE_PATH = "application.string.properties";

    private static Injector injector;

    private boolean applicationReady = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // disable very verbose jnativehook logging
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(java.util.logging.Level.OFF);

        ClassPathFileExtractor classPathFileExtractor = new ClassPathFileExtractorImpl();
        ConfigFileManager configFileManager = new ConfigFileManagerImpl(new ConfigDirectoryImpl(), classPathFileExtractor);
        PropertiesFile propertiesManager = new ApachePropertiesFile(configFileManager.findOrCreateConfigFile(CONFIG_FILE_NAME));
        UIStringsFile stringConfiguration = new ApacheUIStringsFile(classPathFileExtractor.findOnClassPath(UI_STRINGS_FILE_PATH).getFile());
        injector = createInjector(propertiesManager, stringConfiguration, primaryStage, classPathFileExtractor);
        injector.getInstance(EventHandlerRegistrar.class).registerEventHandlers();

        // start by showing settings view
        SettingsStageController settingsStageController = injector.getInstance(SettingsStageController.class);
        settingsStageController.open();

        // start parser
        injector.getInstance(SrtParserEngine.class).start();
        // only for jnativehook 2.2.2:
        // needs to be run on diff thread, otherwise segfault
        Platform.runLater(this::registerHook);
    }

    private void registerHook() {
        try {
            if (GlobalScreen.isNativeHookRegistered()){
                applicationReady = true;
                log.debug("hook is already registered from previous launch");
            }
            else{
                log.info("registering jnativehook");
                GlobalScreen.registerNativeHook();
            }
            // listeners must be reset each time new context is created (in tests relevant)
            // must register here bc of thread issues and race conditions in jnativehook
            // otherwise I would do this centralized in EventHandlerRegistrar
            GlobalHotKeyListener hotKeyListener = injector.getInstance(GlobalHotKeyListener.class);
            GlobalMouseListener mouseListener = injector.getInstance(GlobalMouseListener.class);
            GlobalScreen.addNativeMouseListener(mouseListener);
            GlobalScreen.addNativeKeyListener(hotKeyListener);
            applicationReady = true;
        } catch (NativeHookException e) {
            log.error("Failed to register native hook", e);
            System.exit(1);
        }
    }

    /**
     * Creates injector or use test injector created in GuiTest.
     */
    private Injector createInjector(PropertiesFile propertiesManager,
                                           UIStringsFile stringConfiguration,
                                           Stage primaryStage,
                                           ClassPathFileExtractor classPathFileExtractor) {
        if (injector == null) {
            // use default modules
            List<Module> moduleList = Arrays.asList(
                    new ClassPathFileExtractorModule(classPathFileExtractor),
                    new ConfigFileModule(propertiesManager, stringConfiguration),
                    new FileChooserModule(stringConfiguration, propertiesManager),
                    new ParserModule(stringConfiguration, propertiesManager),
                    new GuiModule(stringConfiguration, propertiesManager, primaryStage),
                    new UserInputHandlerModule()
            );
            return Guice.createInjector(moduleList);
        } else {
            // injector is already set via test, use it instead
            // use external modules
            return injector;
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        unregisterListeners();
        unregisterHook();
        unregisterEventHandlers();
    }

    private void unregisterEventHandlers(){
        injector.getInstance(EventHandlerRegistrar.class).unregisterEventHandlers();
    }

    private void unregisterHook(){
        if (!GlobalScreen.isNativeHookRegistered()){
            log.debug("hook is already unregistered");
        }
        else {
            log.debug("unregistering nativehook");
            try {
                GlobalScreen.unregisterNativeHook();
                applicationReady = false;
            } catch (NativeHookException e) {
                log.error("could not unregister jnativehook",e);
            }
        }
    }

    public void unregisterListeners(){
        GlobalHotKeyListener hotKeyListener = injector.getInstance(GlobalHotKeyListener.class);
        GlobalMouseListener mouseListener = injector.getInstance(GlobalMouseListener.class);
        GlobalScreen.removeNativeKeyListener(hotKeyListener);
        GlobalScreen.removeNativeMouseListener(mouseListener);
    }


    public boolean isReady() {
        return applicationReady;
    }

    //only for testing Purposes
    public static void setInjector(Injector injector) {
        Main.injector = injector;
    }

    public static Injector getInjector() {
        return injector;
    }

}
