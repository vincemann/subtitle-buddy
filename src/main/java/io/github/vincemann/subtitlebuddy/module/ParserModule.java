package io.github.vincemann.subtitlebuddy.module;

import com.google.common.eventbus.EventBus;
import com.google.inject.name.Names;
import io.github.vincemann.subtitlebuddy.options.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.events.EventBusImpl;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtParserEventHandler;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtParserEventHandlerImpl;
import io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath.LastPathRegistry;
import io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath.PropertiesFileLastPathRegistry;
import io.github.vincemann.subtitlebuddy.srt.font.FontsDirectory;
import io.github.vincemann.subtitlebuddy.srt.font.FontsDirectoryImpl;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFileProvider;
import io.github.vincemann.subtitlebuddy.srt.font.SrtFontManager;
import io.github.vincemann.subtitlebuddy.srt.font.SrtFontManagerImpl;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtParser;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtParserImpl;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.StopWatch;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.StopWatchImpl;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFile;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtFileParser;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtFileParserImpl;
import io.github.vincemann.subtitlebuddy.srt.engine.SrtParserEngine;
import io.github.vincemann.subtitlebuddy.srt.engine.SrtParserEngineImpl;

public class ParserModule extends PropertyFilesModule {

    public ParserModule(UIStringsFile stringConfiguration, PropertiesFile propertiesConfiguration) {
        super(stringConfiguration, propertiesConfiguration);
    }


    @Override
    protected void initPropertyBindings() {
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.UPDATER_DELAY))
                .to(getPropertiesFile().getLong(PropertyFileKeys.UPDATER_DELAY));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.USER_CURRENT_FONT))
                .to(getPropertiesFile().getString(PropertyFileKeys.USER_CURRENT_FONT));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.USER_FONT_SIZE))
                .to(getPropertiesFile().getString(PropertyFileKeys.USER_FONT_SIZE));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.ENCODING))
                .to(getPropertiesFile().getString(PropertyFileKeys.ENCODING));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.USER_FONT_COLOR))
                .to(getPropertiesFile().getString(PropertyFileKeys.USER_FONT_COLOR));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.DEFAULT_SUBTITLE))
                .to(getPropertiesFile().getString(PropertyFileKeys.DEFAULT_SUBTITLE));
    }

    @Override
    protected void initClassBindings() {
        bind(LastPathRegistry.class).to(PropertiesFileLastPathRegistry.class);
        bind(SrtFileParser.class).to(SrtFileParserImpl.class);
        bind(StopWatch.class).to(StopWatchImpl.class);
        bind(SubtitleFile.class).toProvider(SubtitleFileProvider.class);
        bind(SrtParser.class).to(SrtParserImpl.class);
        bind(EventBus.class).to(EventBusImpl.class);
        bind(SrtParserEngine.class).to(SrtParserEngineImpl.class);
        bind(SrtFontManager.class).to(SrtFontManagerImpl.class);
        bind(FontsDirectory.class).to(FontsDirectoryImpl.class);
        bind(SrtParserEventHandler.class).to(SrtParserEventHandlerImpl.class);
    }
}
