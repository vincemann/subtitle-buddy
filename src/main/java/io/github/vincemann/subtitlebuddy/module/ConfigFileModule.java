package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectory;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectoryImpl;
import io.github.vincemann.subtitlebuddy.config.ConfigFileLoader;
import io.github.vincemann.subtitlebuddy.config.ConfigFileLoaderImpl;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.options.Options;
import io.github.vincemann.subtitlebuddy.options.OptionsManager;
import io.github.vincemann.subtitlebuddy.options.OptionsManagerImpl;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;


public class ConfigFileModule extends AbstractModule {

    private PropertiesFile properties;
    private UIStringsFile stringConfiguration;



    public ConfigFileModule(PropertiesFile properties, UIStringsFile stringConfiguration) {
        this.properties = properties;
        this.stringConfiguration = stringConfiguration;
    }

    @Override
    protected void configure() {
        bind(ConfigDirectory.class).to(ConfigDirectoryImpl.class);
        bind(PropertiesFile.class).toInstance(properties);
        bind(UIStringsFile.class).toInstance(stringConfiguration);
        bind(ConfigFileLoader.class).to(ConfigFileLoaderImpl.class);
    }
}
