package com.youneedsoftware.subtitleBuddy.srt.parser;

import com.youneedsoftware.subtitleBuddy.events.DoneParsingEvent;
import com.youneedsoftware.subtitleBuddy.events.RequestSrtParserUpdateEvent;

public interface SrtParserEventHandler {

    void handleDoneParsingEvent(DoneParsingEvent doneParsingEvent);
    void handleRequestUpdateEvent(RequestSrtParserUpdateEvent e);
}
