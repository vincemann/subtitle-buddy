package io.github.vincemann.subtitlebuddy.srt.parser;

import com.google.common.eventbus.Subscribe;
import io.github.vincemann.subtitlebuddy.events.DoneParsingEvent;
import io.github.vincemann.subtitlebuddy.events.RequestSrtParserUpdateEvent;
import io.github.vincemann.subtitlebuddy.srt.engine.SrtParserEngine;
import lombok.extern.slf4j.Slf4j;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Slf4j
@Singleton
public class SrtParserEventHandlerImpl implements SrtParserEventHandler {

    private SrtParserEngine srtParserEngine;
    private SrtParser srtParser;

    @Inject
    public SrtParserEventHandlerImpl(SrtParserEngine srtParserEngine, SrtParser srtParser) {
        this.srtParserEngine = srtParserEngine;
        this.srtParser = srtParser;
    }

    @Override
    @Subscribe
    public void handleDoneParsingEvent(DoneParsingEvent doneParsingEvent) {
        log.info("DoneParsingEvent arrived -> stopping io.github.vincemann.srtParser und updater");
        srtParserEngine.stop();
        srtParser.reset();
    }

    @Subscribe
    @Override
    public void handleRequestUpdateEvent(RequestSrtParserUpdateEvent e) {
        srtParserEngine.update();
    }
}
