package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.Singleton;
import com.google.inject.name.Names;
import io.github.vincemann.subtitlebuddy.config.strings.MessageSource;
import io.github.vincemann.subtitlebuddy.events.EventBus;
import io.github.vincemann.subtitlebuddy.events.EventBusImpl;
import io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath.LastPathRegistry;
import io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath.PropertiesFileLastPathRegistry;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;
import io.github.vincemann.subtitlebuddy.options.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.srt.SrtOptions;
import io.github.vincemann.subtitlebuddy.srt.engine.SrtParserEngine;
import io.github.vincemann.subtitlebuddy.srt.engine.SrtParserEngineImpl;
import io.github.vincemann.subtitlebuddy.srt.parser.*;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFile;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFileProvider;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.StopWatch;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.StopWatchImpl;

public class ParserModule extends PropertyFilesModule {

    public ParserModule(MessageSource stringConfiguration, PropertiesFile propertiesConfiguration) {
        super(stringConfiguration, propertiesConfiguration);
    }


    @Override
    protected void configurePropertyBindings() {
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.UPDATER_DELAY))
                .to(getProperties().getLong(PropertyFileKeys.UPDATER_DELAY));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.ENCODING))
                .to(getProperties().getString(PropertyFileKeys.ENCODING));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.DEFAULT_SUBTITLE))
                .to(getProperties().getString(PropertyFileKeys.DEFAULT_SUBTITLE));
    }

    @Override
    protected void configureClassBindings() {
        bind(SrtOptions.class).in(Singleton.class);
        bind(LastPathRegistry.class).to(PropertiesFileLastPathRegistry.class);
        bind(SubtitleTextParser.class).to(SubtitleTextParserImpl.class);
        bind(SrtFileParser.class).to(SrtFileParserImpl.class);
        bind(StopWatch.class).to(StopWatchImpl.class);
        bind(SubtitleFile.class).toProvider(SubtitleFileProvider.class);
        bind(SrtPlayer.class).to(SrtPlayerImpl.class);
        bind(EventBus.class).to(EventBusImpl.class);
        bind(SrtParserEngine.class).to(SrtParserEngineImpl.class);

        bind(SrtParserEventHandler.class).to(SrtParserEventHandlerImpl.class);
    }
}
