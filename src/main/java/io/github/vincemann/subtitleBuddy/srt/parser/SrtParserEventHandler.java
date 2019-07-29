package io.github.vincemann.subtitleBuddy.srt.parser;

import io.github.vincemann.subtitleBuddy.events.DoneParsingEvent;
import io.github.vincemann.subtitleBuddy.events.RequestSrtParserUpdateEvent;

public interface SrtParserEventHandler {

    void handleDoneParsingEvent(DoneParsingEvent doneParsingEvent);
    void handleRequestUpdateEvent(RequestSrtParserUpdateEvent e);
}
