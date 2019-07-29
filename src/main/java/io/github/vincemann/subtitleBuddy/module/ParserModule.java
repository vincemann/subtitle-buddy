package io.github.vincemann.subtitleBuddy.module;

import com.google.common.eventbus.EventBus;
import com.google.inject.name.Names;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyFileKeys;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertiesFile;
import io.github.vincemann.subtitleBuddy.config.uiStringsFile.UIStringsFile;
import io.github.vincemann.subtitleBuddy.events.eventBus.EventBusImpl;
import io.github.vincemann.subtitleBuddy.srt.parser.SrtParserEventHandler;
import io.github.vincemann.subtitleBuddy.srt.parser.SrtParserEventHandlerImpl;
import io.github.vincemann.subtitleBuddy.filechooser.lastPathhandler.LastPathHandler;
import io.github.vincemann.subtitleBuddy.filechooser.lastPathhandler.LastPathHandlerImpl;
import io.github.vincemann.subtitleBuddy.srt.font.fontsLocationManager.FontsLocationManager;
import io.github.vincemann.subtitleBuddy.srt.font.fontsLocationManager.ExternalFolderFontsLocationManager;
import io.github.vincemann.subtitleBuddy.srt.subtitleFile.SubtitleFileProvider;
import io.github.vincemann.subtitleBuddy.srt.font.fontManager.SrtFontManager;
import io.github.vincemann.subtitleBuddy.srt.font.fontManager.FontManagerImpl;
import io.github.vincemann.subtitleBuddy.srt.parser.SrtParser;
import io.github.vincemann.subtitleBuddy.srt.parser.SrtParserImpl;
import io.github.vincemann.subtitleBuddy.srt.stopwatch.StopWatch;
import io.github.vincemann.subtitleBuddy.srt.stopwatch.StopWatchImpl;
import io.github.vincemann.subtitleBuddy.srt.subtitleFile.SubtitleFile;
import io.github.vincemann.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformer;
import io.github.vincemann.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformerImpl;
import io.github.vincemann.subtitleBuddy.srt.updater.SrtParserUpdater;
import io.github.vincemann.subtitleBuddy.srt.updater.SrtParserUpdaterImpl;

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
