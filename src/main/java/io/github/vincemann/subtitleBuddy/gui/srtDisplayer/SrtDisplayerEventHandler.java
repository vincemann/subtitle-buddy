package io.github.vincemann.subtitleBuddy.gui.srtDisplayer;

import io.github.vincemann.subtitleBuddy.events.MovieTextPositionChangedEvent;
import io.github.vincemann.subtitleBuddy.events.SrtFontChangeEvent;
import io.github.vincemann.subtitleBuddy.events.SrtFontColorChangeEvent;
import io.github.vincemann.subtitleBuddy.events.SwitchSrtDisplayerEvent;

public interface SrtDisplayerEventHandler {
    public void handleFontColorChangedEvent(SrtFontColorChangeEvent e);
    public void handleFontChangedEvent(SrtFontChangeEvent e);
    public void handleMovieTextPositionChangedEvent(MovieTextPositionChangedEvent e);
    public void handleSwitchSrtDisplayerEvent(SwitchSrtDisplayerEvent e);
}
