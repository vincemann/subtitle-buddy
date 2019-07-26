package com.youneedsoftware.subtitleBuddy.srt.parser;

import com.google.common.eventbus.Subscribe;
import com.youneedsoftware.subtitleBuddy.events.DoneParsingEvent;
import com.youneedsoftware.subtitleBuddy.events.RequestSrtParserUpdateEvent;
import com.youneedsoftware.subtitleBuddy.srt.updater.SrtParserUpdater;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log4j
@Singleton
public class SrtParserEventHandlerImpl implements SrtParserEventHandler {

    private SrtParserUpdater srtParserUpdater;
    private SrtParser srtParser;

    @Inject
    public SrtParserEventHandlerImpl(SrtParserUpdater srtParserUpdater, SrtParser srtParser) {
        this.srtParserUpdater = srtParserUpdater;
        this.srtParser = srtParser;
    }

    @Override
    @Subscribe
    public void handleDoneParsingEvent(DoneParsingEvent doneParsingEvent) {
        log.info("DoneParsingEvent arrived -> stopping srtParser und updater");
        srtParserUpdater.stop();
        srtParser.reset();
    }

    @Subscribe
    @Override
    public void handleRequestUpdateEvent(RequestSrtParserUpdateEvent e) {
        srtParserUpdater.update();
    }
}
