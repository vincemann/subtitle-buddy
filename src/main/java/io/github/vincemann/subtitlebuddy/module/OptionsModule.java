package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.config.strings.MessageSource;
import io.github.vincemann.subtitlebuddy.options.*;

public class OptionsModule extends PropertyFilesModule {

    public OptionsModule(MessageSource stringsFile, PropertiesFile propertiesFile) {
        super(stringsFile, propertiesFile);
    }

    @Override
    protected void configurePropertyBindings() {

    }

    @Override
    protected void configureClassBindings() {
        // Bind the OptionsManager as a singleton
        bind(OptionsManager.class).to(OptionsManagerImpl.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public Options provideOptions(OptionsManager optionsManager) {
        return optionsManager.parseOptions();
    }

}
