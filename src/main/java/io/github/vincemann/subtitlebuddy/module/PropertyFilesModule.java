package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;
import io.github.vincemann.subtitlebuddy.config.strings.MessageSource;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class PropertyFilesModule extends AbstractModule {
    private MessageSource stringsFile;
    private PropertiesFile properties;


    @Override
    protected final void configure() {
        configurePropertyBindings();
        configureClassBindings();
    }

    protected abstract void configurePropertyBindings();

    protected abstract void configureClassBindings();
}
