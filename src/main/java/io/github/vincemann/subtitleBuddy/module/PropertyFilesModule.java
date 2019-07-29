package io.github.vincemann.subtitleBuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertiesFile;
import io.github.vincemann.subtitleBuddy.config.uiStringsFile.UIStringsFile;
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
