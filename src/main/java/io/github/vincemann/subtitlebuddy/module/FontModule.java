package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.name.Names;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;
import io.github.vincemann.subtitlebuddy.options.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.srt.font.*;

public class FontModule extends PropertyFilesModule{

    public FontModule(UIStringsFile stringsFile, PropertiesFile propertiesFile) {
        super(stringsFile, propertiesFile);
    }

    @Override
    protected void configurePropertyBindings() {
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.USER_CURRENT_FONT))
                .to(getProperties().getString(PropertyFileKeys.USER_CURRENT_FONT));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.MOVIE_FONT_SIZE))
                .to(getProperties().getString(PropertyFileKeys.MOVIE_FONT_SIZE));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.USER_FONT_COLOR))
                .to(getProperties().getString(PropertyFileKeys.USER_FONT_COLOR));
    }

    @Override
    protected void configureClassBindings() {
        bind(FontsDirectory.class).to(FontsDirectoryImpl.class);
        bind(DefaultFontsInstaller.class).to(DefaultFontsInstallerImpl.class);
        bind(FontBundleLoader.class).to(FontBundleLoaderImpl.class);
        bind(FontManager.class).to(FontManagerImpl.class);
    }
}
