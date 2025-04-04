package io.github.vincemann.subtitlebuddy.srt.parser;

import io.github.vincemann.subtitlebuddy.events.DoneParsingEvent;
import io.github.vincemann.subtitlebuddy.events.RequestSubtitleUpdateEvent;

public interface SrtParserEventHandler {

    void handleDoneParsingEvent(DoneParsingEvent doneParsingEvent);
    void handleRequestUpdateEvent(RequestSubtitleUpdateEvent e);
}
