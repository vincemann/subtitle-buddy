package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectory;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectoryImpl;
import io.github.vincemann.subtitlebuddy.config.ConfigFileLoader;
import io.github.vincemann.subtitlebuddy.config.ConfigFileLoaderImpl;
import io.github.vincemann.subtitlebuddy.config.strings.MessageSource;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;


public class ConfigFileModule extends AbstractModule {

    private PropertiesFile properties;
    private MessageSource stringConfiguration;



    public ConfigFileModule(MessageSource stringConfiguration, PropertiesFile properties) {
        this.properties = properties;
        this.stringConfiguration = stringConfiguration;
    }

    @Override
    protected void configure() {
        bind(ConfigDirectory.class).to(ConfigDirectoryImpl.class);
        bind(PropertiesFile.class).toInstance(properties);
        bind(MessageSource.class).toInstance(stringConfiguration);
        bind(ConfigFileLoader.class).to(ConfigFileLoaderImpl.class);
    }
}
