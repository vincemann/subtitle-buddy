package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectory;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectoryImpl;
import io.github.vincemann.subtitlebuddy.config.ConfigFileManager;
import io.github.vincemann.subtitlebuddy.properties.PropertiesFile;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.cp.ConfigFileManagerImpl;


public class ConfigFileModule extends AbstractModule {

    private PropertiesFile propertiesManager;
    private UIStringsFile stringConfiguration;


    public ConfigFileModule(PropertiesFile propertiesManager, UIStringsFile stringConfiguration) {
        this.propertiesManager = propertiesManager;
        this.stringConfiguration = stringConfiguration;
    }

    @Override
    protected void configure() {
        bind(ConfigDirectory.class).to(ConfigDirectoryImpl.class);
        bind(PropertiesFile.class).toInstance(propertiesManager);
        bind(UIStringsFile.class).toInstance(stringConfiguration);
        bind(ConfigFileManager.class).to(ConfigFileManagerImpl.class);
    }
}
