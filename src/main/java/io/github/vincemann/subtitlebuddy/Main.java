package io.github.vincemann.subtitlebuddy;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectoryImpl;
import io.github.vincemann.subtitlebuddy.config.ConfigFileLoader;
import io.github.vincemann.subtitlebuddy.config.ConfigFileLoaderImpl;
import io.github.vincemann.subtitlebuddy.config.strings.ApacheUIStringsFile;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractor;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractorImpl;
import io.github.vincemann.subtitlebuddy.events.EventHandlerRegistrar;
import io.github.vincemann.subtitlebuddy.gui.Window;
import io.github.vincemann.subtitlebuddy.gui.WindowManager;
import io.github.vincemann.subtitlebuddy.gui.Windows;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieStageFactory;
import io.github.vincemann.subtitlebuddy.gui.options.OptionsStageFactory;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsStageFactory;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieStageController;
import io.github.vincemann.subtitlebuddy.gui.options.OptionsStageController;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsStageController;
import io.github.vincemann.subtitlebuddy.listeners.JLinkJNativeLibraryLocator;
import io.github.vincemann.subtitlebuddy.listeners.key.GlobalHotKeyListener;
import io.github.vincemann.subtitlebuddy.listeners.mouse.GlobalMouseListener;
import io.github.vincemann.subtitlebuddy.module.*;
import io.github.vincemann.subtitlebuddy.options.ApachePropertiesFile;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;
import io.github.vincemann.subtitlebuddy.srt.engine.SrtParserEngine;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
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

    /**
     * Is used for testing purposes. I need to register hook on diff thread, so I need a way to wait for registration in my tests.
     */
    private boolean applicationReady = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // disable very verbose jnativehook logging
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(java.util.logging.Level.OFF);

        ClassPathFileExtractor classPathFileExtractor = new ClassPathFileExtractorImpl();
        ConfigFileLoader configFileLoader = new ConfigFileLoaderImpl(new ConfigDirectoryImpl(), classPathFileExtractor);
        PropertiesFile propertiesManager = new ApachePropertiesFile(configFileLoader.findOrCreateConfigFile(CONFIG_FILE_NAME));
        UIStringsFile stringConfiguration = new ApacheUIStringsFile(classPathFileExtractor.findOnClassPath(UI_STRINGS_FILE_PATH).getFile());
        injector = createInjector(propertiesManager, stringConfiguration, classPathFileExtractor);
        injector.getInstance(EventHandlerRegistrar.class).registerEventHandlers();
        WindowManager windowManager = injector.getInstance(WindowManager.class);
        createStages();

        // start by showing settings view
        windowManager.showWindow(Windows.SETTINGS);

        // start parser
        injector.getInstance(SrtParserEngine.class).start();
        // only for jnativehook 2.2.2:
        // needs to be run on diff thread, otherwise segfault
        Platform.runLater(this::registerHook);
    }

    private void createStages() throws IOException {
        WindowManager windowManager = injector.getInstance(WindowManager.class);
        SettingsStageFactory settingsStageFactory = injector.getInstance(SettingsStageFactory.class);
        SettingsStageController settingsStageController = injector.getInstance(SettingsStageController.class);
        Stage settingsStage = settingsStageFactory.create();
        windowManager.registerWindow(new Window(Windows.SETTINGS,settingsStage,settingsStageController));

        OptionsStageFactory optionsStageFactory = injector.getInstance(OptionsStageFactory.class);
        Stage optionsStage = optionsStageFactory.create();
        OptionsStageController optionsStageController = injector.getInstance(OptionsStageController.class);
        windowManager.registerWindow(new Window(Windows.OPTIONS,optionsStage,optionsStageController));

        MovieStageFactory movieStageFactory = injector.getInstance(MovieStageFactory.class);
        Stage movieStage = movieStageFactory.create();
        MovieStageController movieStageController = injector.getInstance(MovieStageController.class);
        windowManager.registerWindow(new Window(Windows.MOVIE,movieStage,movieStageController));

    }

    private void registerHook() {
        try {
            if (GlobalScreen.isNativeHookRegistered()) {
                log.debug("hook is already registered from previous launch");
            } else {
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
                                    ClassPathFileExtractor classPathFileExtractor) {
        if (injector == null) {
            // use default modules
            List<Module> moduleList = Arrays.asList(
                    new ClassPathFileModule(classPathFileExtractor),
                    new ConfigFileModule(propertiesManager, stringConfiguration),
                    new FileChooserModule(stringConfiguration, propertiesManager),
                    new ParserModule(stringConfiguration, propertiesManager),
                    new GuiModule(stringConfiguration, propertiesManager),
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

    private void unregisterEventHandlers() {
        injector.getInstance(EventHandlerRegistrar.class).unregisterEventHandlers();
    }

    private void unregisterHook() {
        if (!GlobalScreen.isNativeHookRegistered()) {
            log.debug("hook is already unregistered");
            applicationReady = false;
        } else {
            log.debug("unregistering jnativehook");
            try {
                GlobalScreen.unregisterNativeHook();
                applicationReady = false;
            } catch (NativeHookException e) {
                log.error("could not unregister jnativehook", e);
            }
        }
    }

    public void unregisterListeners() {
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
