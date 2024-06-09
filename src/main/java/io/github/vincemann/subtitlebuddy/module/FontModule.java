package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.config.strings.MessageSource;
import io.github.vincemann.subtitlebuddy.font.*;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;

public class FontModule extends PropertyFilesModule{

    public FontModule(MessageSource stringsFile, PropertiesFile propertiesFile) {
        super(stringsFile, propertiesFile);
    }

    @Override
    protected void configurePropertyBindings() {
    }

    @Override
    protected void configureClassBindings() {
        bind(FontsDirectory.class).to(FontsDirectoryImpl.class);
        bind(DefaultFontsInstaller.class).to(DefaultFontsInstallerImpl.class);
        bind(FontBundleLoader.class).to(FontBundleLoaderImpl.class);
        bind(FontManager.class).to(FontManagerImpl.class);
        bind(FontOptions.class).in(Singleton.class);
    }
}
