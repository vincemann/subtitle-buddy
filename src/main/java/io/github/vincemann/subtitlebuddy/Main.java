package io.github.vincemann.subtitlebuddy;

import com.github.kwhat.jnativehook.DefaultLibraryLocator;
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
import io.github.vincemann.subtitlebuddy.listeners.key.GlobalHotKeyListener;
import io.github.vincemann.subtitlebuddy.listeners.mouse.GlobalMouseListener;
import io.github.vincemann.subtitlebuddy.module.*;
import io.github.vincemann.subtitlebuddy.properties.ApachePropertiesFile;
import io.github.vincemann.subtitlebuddy.properties.PropertiesFile;
import io.github.vincemann.subtitlebuddy.srt.engine.SrtParserEngine;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.List;

@Log4j2
@NoArgsConstructor
@Singleton
public class Main extends Application {

    // fixing classloader issues with fat jar
    static {
        System.setProperty("jnativehook.lib.locator", DefaultLibraryLocator.class.getName());
    }

    private BooleanProperty readyProperty = new SimpleBooleanProperty(false);

    public static final String CONFIG_FILE_NAME = "application.properties";
    public static final String UI_STRINGS_CONFIG_FILE_PATH = "application.string.properties";
    private static Injector injector;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // disable very verbose jnativehook logging
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(java.util.logging.Level.OFF);

        ClassPathFileExtractor classPathFileExtractor = new ClassPathFileExtractorImpl();
        ConfigFileManager configFileManager =  new ConfigFileManagerImpl(new ConfigDirectoryImpl(), classPathFileExtractor);
        PropertiesFile propertiesManager = new ApachePropertiesFile(configFileManager.findOrCreateConfigFile(CONFIG_FILE_NAME));
        UIStringsFile stringConfiguration = new ApacheUIStringsFile(classPathFileExtractor.findOnClassPath(UI_STRINGS_CONFIG_FILE_PATH).getFile());
        injector = createInjector(propertiesManager,stringConfiguration,primaryStage, classPathFileExtractor);
        injector.getInstance(EventHandlerRegistrar.class).registerEventHandlers();

        SettingsStageController settingsStageController = injector.getInstance(SettingsStageController.class);
        settingsStageController.open();

        // start parser
        getInjector().getInstance(SrtParserEngine.class).start();
        Platform.runLater(this::registerHook);
    }

    private void registerHook(){
        try {
            GlobalScreen.registerNativeHook();
            // needs to be done here so it does not trigger a race condition
            GlobalHotKeyListener hotKeyListener = injector.getInstance(GlobalHotKeyListener.class);
            GlobalMouseListener mouseListener = injector.getInstance(GlobalMouseListener.class);

            GlobalScreen.addNativeMouseListener(mouseListener);
            GlobalScreen.addNativeKeyListener(hotKeyListener);
            setReady(true);
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }
    }

    private static Injector createInjector(PropertiesFile propertiesManager,
                                           UIStringsFile stringConfiguration,
                                           Stage primaryStage,
                                           ClassPathFileExtractor classPathFileExtractor){
        if(injector==null) {
            //use default modules
            List<Module> moduleList = Arrays.asList(
                    new ClassPathFileExtractorModule(classPathFileExtractor),
                    new ConfigFileModule(propertiesManager, stringConfiguration),
                    new FileChooserModule(stringConfiguration, propertiesManager) ,
                    new ParserModule(stringConfiguration, propertiesManager) ,
                    new GuiModule(stringConfiguration, propertiesManager, primaryStage),
                    new UserInputHandlerModule()
            );
           return Guice.createInjector(moduleList);
        }else {
            //use external modules
            return injector;
        }
    }

    // Getter for the property
    public BooleanProperty readyProperty() {
        return readyProperty;
    }

    public boolean isReady() {
        return readyProperty.get();
    }

    private void setReady(boolean ready) {
        this.readyProperty.set(ready);
    }



    //only for testing Purposes
    public static void createInjector(Module... modules){
        injector = Guice.createInjector(Arrays.asList(modules));
    }

    public static Injector getInjector()  {
        return injector;
    }

}
