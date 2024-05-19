package io.github.vincemann.subtitlebuddy.module;

import com.google.common.eventbus.EventBus;
import com.google.inject.name.Names;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.events.EventBusImpl;
import io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath.LastPathRegistry;
import io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath.PropertiesFileLastPathRegistry;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;
import io.github.vincemann.subtitlebuddy.options.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.srt.engine.SrtParserEngine;
import io.github.vincemann.subtitlebuddy.srt.engine.SrtParserEngineImpl;
import io.github.vincemann.subtitlebuddy.srt.parser.*;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFile;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFileProvider;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.StopWatch;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.StopWatchImpl;

public class ParserModule extends PropertyFilesModule {

    public ParserModule(UIStringsFile stringConfiguration, PropertiesFile propertiesConfiguration) {
        super(stringConfiguration, propertiesConfiguration);
    }


    @Override
    protected void configurePropertyBindings() {
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.UPDATER_DELAY))
                .to(getPropertiesFile().getLong(PropertyFileKeys.UPDATER_DELAY));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.ENCODING))
                .to(getPropertiesFile().getString(PropertyFileKeys.ENCODING));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.DEFAULT_SUBTITLE))
                .to(getPropertiesFile().getString(PropertyFileKeys.DEFAULT_SUBTITLE));
    }

    @Override
    protected void configureClassBindings() {
        bind(LastPathRegistry.class).to(PropertiesFileLastPathRegistry.class);
        bind(SrtFileParser.class).to(SrtFileParserImpl.class);
        bind(StopWatch.class).to(StopWatchImpl.class);
        bind(SubtitleFile.class).toProvider(SubtitleFileProvider.class);
        bind(SrtParser.class).to(SrtParserImpl.class);
        bind(EventBus.class).to(EventBusImpl.class);
        bind(SrtParserEngine.class).to(SrtParserEngineImpl.class);

        bind(SrtParserEventHandler.class).to(SrtParserEventHandlerImpl.class);
    }
}
