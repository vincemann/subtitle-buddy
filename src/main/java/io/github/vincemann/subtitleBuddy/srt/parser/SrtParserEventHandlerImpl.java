package io.github.vincemann.subtitleBuddy.srt.parser;

import com.google.common.eventbus.Subscribe;
import io.github.vincemann.subtitleBuddy.events.DoneParsingEvent;
import io.github.vincemann.subtitleBuddy.events.RequestSrtParserUpdateEvent;
import io.github.vincemann.subtitleBuddy.srt.updater.SrtParserUpdater;
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
        log.info("DoneParsingEvent arrived -> stopping io.github.vincemann.srtParser und updater");
        srtParserUpdater.stop();
        srtParser.reset();
    }

    @Subscribe
    @Override
    public void handleRequestUpdateEvent(RequestSrtParserUpdateEvent e) {
        srtParserUpdater.update();
    }
}
