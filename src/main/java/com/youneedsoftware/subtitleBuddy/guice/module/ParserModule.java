package com.youneedsoftware.subtitleBuddy.guice.module;

import com.google.common.eventbus.EventBus;
import com.google.inject.name.Names;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertyFileKeys;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertiesFile;
import com.youneedsoftware.subtitleBuddy.config.uiStringsFile.UIStringsFile;
import com.youneedsoftware.subtitleBuddy.events.eventBus.EventBusImpl;
import com.youneedsoftware.subtitleBuddy.srt.parser.SrtParserEventHandler;
import com.youneedsoftware.subtitleBuddy.srt.parser.SrtParserEventHandlerImpl;
import com.youneedsoftware.subtitleBuddy.filechooser.lastPathhandler.LastPathHandler;
import com.youneedsoftware.subtitleBuddy.filechooser.lastPathhandler.LastPathHandlerImpl;
import com.youneedsoftware.subtitleBuddy.srt.font.fontsLocationManager.FontsLocationManager;
import com.youneedsoftware.subtitleBuddy.srt.font.fontsLocationManager.ExternalFolderFontsLocationManager;
import com.youneedsoftware.subtitleBuddy.srt.subtitleFile.SubtitleFileProvider;
import com.youneedsoftware.subtitleBuddy.srt.font.fontManager.SrtFontManager;
import com.youneedsoftware.subtitleBuddy.srt.font.fontManager.FontManagerImpl;
import com.youneedsoftware.subtitleBuddy.srt.parser.SrtParser;
import com.youneedsoftware.subtitleBuddy.srt.parser.SrtParserImpl;
import com.youneedsoftware.subtitleBuddy.srt.stopWatch.StopWatch;
import com.youneedsoftware.subtitleBuddy.srt.stopWatch.StopWatchImpl;
import com.youneedsoftware.subtitleBuddy.srt.subtitleFile.SubtitleFile;
import com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformer;
import com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformerImpl;
import com.youneedsoftware.subtitleBuddy.srt.updater.SrtParserUpdater;
import com.youneedsoftware.subtitleBuddy.srt.updater.SrtParserUpdaterImpl;

public class ParserModule extends PropertyFilesModule {

    public ParserModule(UIStringsFile stringConfiguration, PropertiesFile propertiesConfiguration) {
        super(stringConfiguration, propertiesConfiguration);
    }


    @Override
    protected void initPropertyBindings() {
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.UPDATER_DELAY_KEY)).to(getPropertiesFile().getLong(PropertyFileKeys.UPDATER_DELAY_KEY));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.USER_DEFAULT_FONT_PATH)).to(getPropertiesFile().getString(PropertyFileKeys.USER_DEFAULT_FONT_PATH));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.USER_FONT_SIZE_KEY)).to(getPropertiesFile().getString(PropertyFileKeys.USER_FONT_SIZE_KEY));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.USER_FONT_COLOR_KEY)).to(getPropertiesFile().getString(PropertyFileKeys.USER_FONT_COLOR_KEY));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.DEFAULT_SUBTITLE_KEY)).to(getPropertiesFile().getString(PropertyFileKeys.DEFAULT_SUBTITLE_KEY));
    }

    @Override
    protected void initClassBindings() {
        bind(LastPathHandler.class).to(LastPathHandlerImpl.class);
        bind(SrtFileTransformer.class).to(SrtFileTransformerImpl.class);
        bind(StopWatch.class).to(StopWatchImpl.class);
        bind(SubtitleFile.class).toProvider(SubtitleFileProvider.class);
        bind(SrtParser.class).to(SrtParserImpl.class);
        bind(EventBus.class).to(EventBusImpl.class);
        bind(SrtParserUpdater.class).to(SrtParserUpdaterImpl.class);
        bind(SrtFontManager.class).to(FontManagerImpl.class);
        bind(FontsLocationManager.class).to(ExternalFolderFontsLocationManager.class);
        bind(SrtParserEventHandler.class).to(SrtParserEventHandlerImpl.class);
    }
}
