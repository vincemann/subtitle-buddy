package io.github.vincemann.subtitlebuddy;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectoryImpl;
import io.github.vincemann.subtitlebuddy.config.ConfigFileManager;
import io.github.vincemann.subtitlebuddy.config.strings.ApacheUIStringsFile;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractor;
import io.github.vincemann.subtitlebuddy.config.ConfigFileManagerImpl;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractorImpl;
import io.github.vincemann.subtitlebuddy.gui.stages.controller.SettingsStageController;
import io.github.vincemann.subtitlebuddy.module.*;
import io.github.vincemann.subtitlebuddy.properties.ApachePropertiesFile;
import io.github.vincemann.subtitlebuddy.properties.PropertiesFile;
import io.github.vincemann.subtitlebuddy.service.EventHandlerRegistrar;
import io.github.vincemann.subtitlebuddy.service.SrtService;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.DefaultLibraryLocator;
import org.jnativehook.GlobalScreen;

import java.util.Arrays;
import java.util.List;

@Slf4j
@NoArgsConstructor
@Singleton
public class Main extends Application {

    // somehow manually setting the locator fixes something in jnativehook
    // otherwise the lib cant be found at runtime when running jar
    static {
        System.setProperty("jnativehook.lib.locator", DefaultLibraryLocator.class.getName());
    }

    public static final String CONFIG_FILE_NAME = "application.properties";
    public static final String UI_STRINGS_CONFIG_FILE_PATH = "application.string.properties";

    private SrtService srtService;
    private EventHandlerRegistrar eventHandlerRegistrar;
    private static Injector injector;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // disable jnativehook logging
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(java.util.logging.Level.OFF);
        ClassPathFileExtractor classPathFileExtractor = new ClassPathFileExtractorImpl();
        ConfigFileManager configFileManager =  new ConfigFileManagerImpl(new ConfigDirectoryImpl(), classPathFileExtractor);
        PropertiesFile propertiesManager = new ApachePropertiesFile(configFileManager.findOrCreateConfigFile(CONFIG_FILE_NAME));
        UIStringsFile stringConfiguration = new ApacheUIStringsFile(classPathFileExtractor.findOnClassPath(UI_STRINGS_CONFIG_FILE_PATH).getFile());
        injector = createInjector(propertiesManager,stringConfiguration,primaryStage, classPathFileExtractor);
        EventBus eventBus = injector.getInstance(EventBus.class);
        srtService = injector.getInstance(SrtService.class);
        eventHandlerRegistrar = injector.getInstance(EventHandlerRegistrar.class);
        eventHandlerRegistrar.registerEventHandlers();
        eventBus.register(srtService);

        SettingsStageController settingsStageController = injector.getInstance(SettingsStageController.class);
        settingsStageController.open();
        start();
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

    //only for testing Purposes
    public static void createInjector(Module... modules){
        injector = Guice.createInjector(Arrays.asList(modules));
    }

    public static Injector getInjector()  {
        return injector;
    }

    private void start() {
        srtService.startParser();
    }
}
