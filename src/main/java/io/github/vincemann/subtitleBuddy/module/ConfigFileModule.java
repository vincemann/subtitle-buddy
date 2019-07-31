package io.github.vincemann.subtitleBuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitleBuddy.config.configFileManager.ConfigFileManager;
import io.github.vincemann.subtitleBuddy.config.configFileManager.ExtractingConfigFileManager;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertiesFile;
import io.github.vincemann.subtitleBuddy.config.uiStringsFile.UIStringsFile;

public class ConfigFileModule extends AbstractModule {

    private PropertiesFile propertiesManager;
    private UIStringsFile stringConfiguration;


    public ConfigFileModule(PropertiesFile propertiesManager, UIStringsFile stringConfiguration) {
        this.propertiesManager = propertiesManager;
        this.stringConfiguration = stringConfiguration;
    }

    @Override
    protected void configure() {
        bind(PropertiesFile.class).toInstance(propertiesManager);
        bind(UIStringsFile.class).toInstance(stringConfiguration);
        bind(ConfigFileManager.class).to(ExtractingConfigFileManager.class);
    }
}
