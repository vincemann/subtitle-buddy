package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.font.FontOptions;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayerOptions;
import io.github.vincemann.subtitlebuddy.options.*;
import io.github.vincemann.subtitlebuddy.srt.SrtOptions;

public class OptionsModule extends PropertyFilesModule {

    public OptionsModule(UIStringsFile stringsFile, PropertiesFile propertiesFile) {
        super(stringsFile, propertiesFile);
    }

    @Override
    protected void configurePropertyBindings() {

    }

    @Override
    protected void configureClassBindings() {
        // Bind the OptionsManager as a singleton
        bind(OptionsManager.class).to(OptionsManagerImpl.class).in(Singleton.class);
        bind(SrtOptions.class).in(Singleton.class);
        bind(SrtDisplayerOptions.class).in(Singleton.class);
        bind(FontOptions.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public Options provideOptions(OptionsManager optionsManager) {
        return optionsManager.parseOptions();
    }

}
