package io.github.vincemann.subtitlebuddy.srt.parser;

import com.google.common.eventbus.Subscribe;
import io.github.vincemann.subtitlebuddy.events.DoneParsingEvent;
import io.github.vincemann.subtitlebuddy.events.RequestSubtitleUpdateEvent;
import io.github.vincemann.subtitlebuddy.srt.engine.SrtParserEngine;
import lombok.extern.log4j.Log4j2;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Log4j2
@Singleton
public class SrtParserEventHandlerImpl implements SrtParserEventHandler {

    private SrtParserEngine srtParserEngine;
    private SrtPlayer srtPlayer;

    @Inject
    public SrtParserEventHandlerImpl(SrtParserEngine srtParserEngine, SrtPlayer srtPlayer) {
        this.srtParserEngine = srtParserEngine;
        this.srtPlayer = srtPlayer;
    }

    @Override
    @Subscribe
    public void handleDoneParsingEvent(DoneParsingEvent doneParsingEvent) {
        log.info("DoneParsingEvent arrived -> resetting srt player and stopping");
        // dont stop engine just pause player and reset clock to beginning
//        srtParserEngine.stop();
        srtPlayer.reset();
    }

    @Subscribe
    @Override
    public void handleRequestUpdateEvent(RequestSubtitleUpdateEvent e) {
        srtParserEngine.update();
    }
}
