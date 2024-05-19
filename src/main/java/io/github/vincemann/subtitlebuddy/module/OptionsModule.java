package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.options.*;

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

        // Bind Options and other singletons
        bind(Options.class).toInstance(createOptions());
        bind(SrtOptions.class).in(Singleton.class);
        bind(SrtDisplayerOptions.class).in(Singleton.class);
        bind(FontOptions.class).in(Singleton.class);
    }

    private Options createOptions() {
        // Ensure OptionsManagerImpl is a singleton and already bound
        OptionsManager manager = getProvider(OptionsManager.class).get();
        return manager.parseOptions();
    }

}
