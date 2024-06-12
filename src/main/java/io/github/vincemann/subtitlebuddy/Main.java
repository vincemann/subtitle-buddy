package io.github.vincemann.subtitlebuddy;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.google.inject.*;
import com.google.inject.Module;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectory;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectoryImpl;
import io.github.vincemann.subtitlebuddy.config.ConfigFileLoader;
import io.github.vincemann.subtitlebuddy.config.ConfigFileLoaderImpl;
import io.github.vincemann.subtitlebuddy.config.strings.ApacheMessageSource;
import io.github.vincemann.subtitlebuddy.config.strings.MessageSource;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractor;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractorImpl;
import io.github.vincemann.subtitlebuddy.events.EventBus;
import io.github.vincemann.subtitlebuddy.events.EventHandlerRegistrar;
import io.github.vincemann.subtitlebuddy.events.RequestSubtitleUpdateEvent;
import io.github.vincemann.subtitlebuddy.font.DefaultFontsInstaller;
import io.github.vincemann.subtitlebuddy.font.FontManager;
import io.github.vincemann.subtitlebuddy.font.FontsDirectory;
import io.github.vincemann.subtitlebuddy.gui.Window;
import io.github.vincemann.subtitlebuddy.gui.WindowManager;
import io.github.vincemann.subtitlebuddy.gui.Windows;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieStageController;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieStageFactory;
import io.github.vincemann.subtitlebuddy.gui.options.OptionsStageController;
import io.github.vincemann.subtitlebuddy.gui.options.OptionsStageFactory;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsStageController;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsStageFactory;
import io.github.vincemann.subtitlebuddy.listeners.JLinkJNativeLibraryLocator;
import io.github.vincemann.subtitlebuddy.listeners.key.GlobalHotKeyListener;
import io.github.vincemann.subtitlebuddy.listeners.mouse.GlobalMouseListener;
import io.github.vincemann.subtitlebuddy.module.*;
import io.github.vincemann.subtitlebuddy.options.ApachePropertiesFile;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;
import io.github.vincemann.subtitlebuddy.srt.engine.SrtParserEngine;
import io.github.vincemann.subtitlebuddy.util.SetupTestUtil;
import io.github.vincemann.subtitlebuddy.util.fx.IconUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static io.github.vincemann.subtitlebuddy.util.OSUtils.isMacOS;

@Log4j2
@NoArgsConstructor
@Singleton
public class Main extends Application {

    private static final String VERSION = "2.0.0";
    public static final String CONFIG_FILE_NAME = "application.properties";
    public static final String STRINGS_FILE_NAME = "application.string.properties";


    private static Injector injector;


    /**
     * Is used for testing purposes. I need to register hook on diff thread, so I need a way to wait for registration in my tests.
     */
    private boolean applicationReady = false;
    private static Boolean setupTest = false;
    private static String setupTestFile;

    public static void main(String[] args) {
        if (args.length > 0) {
            String arg1 = args[0];
            if ("--version".equals(arg1)) {
                System.out.println(VERSION);
                System.exit(0);
                return;
            } else if ("--setup-test".equals(arg1)) {
                setupTest = true;
                setupTestFile = args[1];
            }
        }
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        disableVerboseJNativeHookLogging();
        initJNativeHookLibLocator();

        ConfigDirectory configDir = createConfigDir();
        PropertiesFile properties = createPropertiesFile(configDir);
        MessageSource strings = readStrings();

        injector = createInjector(properties, strings);

        createFontDir();
        installDefaultFonts();
        loadFonts();

        registerEventHandlers();

        WindowManager windowManager = createStages(primaryStage);

        // start by showing settings window
        windowManager.open(Windows.SETTINGS);

        startParser();

        registerJNativeHook();

        displayFirstSubtitle();

        // for ci testing purposes
        if (setupTest)
            waitUntilSetupThenExit(windowManager);
    }

    private void waitUntilSetupThenExit(WindowManager windowManager) {
        // wait until jnativehook registered and settings window is open
        SetupTestUtil.performSetupTest(() -> applicationReady
                && windowManager.find(Windows.SETTINGS).getStage().isShowing());
    }


    private void initJNativeHookLibLocator() {
        if ("true".equals(System.getProperty("jlink"))) {
            // fixing lib loading issues within jlink image
            log.info("Setting Jlink-specific library locator for jNativeHook");
            System.setProperty("jnativehook.lib.locator", JLinkJNativeLibraryLocator.class.getName());
        } else {
            // for fat jars the default locator is fine
            log.info("Using default library locator for jNativeHook");
        }
    }


    private void displayFirstSubtitle() {
        injector.getInstance(EventBus.class).post(new RequestSubtitleUpdateEvent());
    }

    private void installDefaultFonts() throws IOException {
        log.debug("installing default fonts");
        Path fontDir = injector.getInstance(FontsDirectory.class).find();
        DefaultFontsInstaller fontsInstaller = injector.getInstance(DefaultFontsInstaller.class);
        fontsInstaller.installIfNeeded(fontDir);
    }

    private void createFontDir() throws IOException {
        ConfigDirectory configDirectory = injector.getInstance(ConfigDirectory.class);
        Path configDir = configDirectory.find();

        log.debug("init font dir");
        FontsDirectory fontsDirectory = injector.getInstance(FontsDirectory.class);
        Path fontDir = fontsDirectory.create(configDir);
    }

    private MessageSource readStrings() throws ConfigurationException {
        // reads from application.string.properties from within jar
        return new ApacheMessageSource(STRINGS_FILE_NAME);
    }

    private void registerJNativeHook() {
        // only for jnativehook 2.2.2:
        // needs to be run on diff thread, otherwise segfault
        Platform.runLater(this::registerHook);
    }

    private void startParser() {
        injector.getInstance(SrtParserEngine.class).start();
    }

    private void registerEventHandlers() {
        injector.getInstance(EventHandlerRegistrar.class).registerEventHandlers();
    }

    private void disableVerboseJNativeHookLogging() {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(java.util.logging.Level.OFF);
    }

    private void loadFonts() {
        log.debug("loading fonts");
        FontManager fontManager = injector.getInstance(FontManager.class);
        fontManager.loadFonts();
    }

    /**
     * Create settings, options and movie stage and register in {@link io.github.vincemann.subtitlebuddy.gui.WindowManager}.
     *
     * @return
     */
    private WindowManager createStages(Stage primaryStage) throws IOException {
        if (isMacOS()) {
            IconUtil.setMacOSDockIcon("icon.png");
        }

        WindowManager windowManager = injector.getInstance(WindowManager.class);

        SettingsStageFactory settingsStageFactory = injector.getInstance(SettingsStageFactory.class);
        SettingsStageController settingsStageController = injector.getInstance(SettingsStageController.class);
        Stage settingsStage = settingsStageFactory.create(primaryStage);
        windowManager.registerWindow(new Window(Windows.SETTINGS, settingsStage, settingsStageController));

        OptionsStageFactory optionsStageFactory = injector.getInstance(OptionsStageFactory.class);
        Stage optionsStage = optionsStageFactory.create();
        OptionsStageController optionsStageController = injector.getInstance(OptionsStageController.class);
        windowManager.registerWindow(new Window(Windows.OPTIONS, optionsStage, optionsStageController));

        MovieStageFactory movieStageFactory = injector.getInstance(MovieStageFactory.class);
        Stage movieStage = movieStageFactory.create();
        MovieStageController movieStageController = injector.getInstance(MovieStageController.class);
        windowManager.registerWindow(new Window(Windows.MOVIE, movieStage, movieStageController));

        return windowManager;
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
    private Injector createInjector(PropertiesFile properties, MessageSource strings) {
        if (injector == null) {
            // use default modules
            return Guice.createInjector(createDefaultModules(properties, strings));
        } else {
            // injector is already set via test, use it instead
            // use external modules
            return injector;
        }
    }

    private List<Module> createDefaultModules(PropertiesFile properties, MessageSource strings) {
        List<Module> modules = Arrays.asList(
                new ClassPathFileModule(),
                new ConfigFileModule(strings, properties),
                new OptionsModule(strings, properties),
                new FileChooserModule(strings, properties),
                new FontModule(strings, properties),
                new ParserModule(strings, properties),
                new GuiModule(strings, properties),
                new UserInputHandlerModule()
        );
        if (setupTest) {
            replaceFileChooserModule(properties, strings, modules);
        }
        return modules;
    }

    // replace javafx gui prompt file chooser with hard coded selected file from cli arg
    // used for setup testing
    private void replaceFileChooserModule(PropertiesFile propertiesFile, MessageSource messageSource, List<Module> modules) {
        modules.replaceAll(module -> {
            if (module instanceof FileChooserModule) {
                return new SetupTestFileChooserModule(messageSource, propertiesFile, setupTestFile);
            }
            return module;
        });
    }

    private ConfigDirectory createConfigDir() throws IOException {
        ConfigDirectory configDirectory = new ConfigDirectoryImpl();
        configDirectory.create();
        return configDirectory;
    }

    private ApachePropertiesFile createPropertiesFile(ConfigDirectory configDirectory) throws IOException, ConfigurationException {
        // init config dir and files
        ClassPathFileExtractor classPathFileExtractor = new ClassPathFileExtractorImpl();
        ConfigFileLoader configFileLoader = new ConfigFileLoaderImpl(configDirectory, classPathFileExtractor);
        File configFile = configFileLoader.findOrCreateConfigFile(CONFIG_FILE_NAME);
        return new ApachePropertiesFile(configFile);
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
