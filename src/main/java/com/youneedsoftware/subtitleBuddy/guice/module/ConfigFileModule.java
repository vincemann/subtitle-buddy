package com.youneedsoftware.subtitleBuddy.guice.module;

import com.google.inject.AbstractModule;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertiesFile;
import com.youneedsoftware.subtitleBuddy.config.uiStringsFile.UIStringsFile;

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
    }
}
