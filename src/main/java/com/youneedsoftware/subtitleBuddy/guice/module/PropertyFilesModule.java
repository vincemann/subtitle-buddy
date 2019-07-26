package com.youneedsoftware.subtitleBuddy.guice.module;

import com.google.inject.AbstractModule;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertiesFile;
import com.youneedsoftware.subtitleBuddy.config.uiStringsFile.UIStringsFile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class PropertyFilesModule extends AbstractModule {
    private UIStringsFile stringsFile;
    private PropertiesFile propertiesFile;


    @Override
    protected final void configure() {
        initPropertyBindings();
        initClassBindings();
    }

    protected abstract void initPropertyBindings();

    protected abstract void initClassBindings();
}
