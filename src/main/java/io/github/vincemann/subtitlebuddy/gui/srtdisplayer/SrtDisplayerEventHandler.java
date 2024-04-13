package io.github.vincemann.subtitlebuddy.gui.srtdisplayer;

import io.github.vincemann.subtitlebuddy.events.MovieTextPositionChangedEvent;
import io.github.vincemann.subtitlebuddy.events.SrtFontChangeEvent;
import io.github.vincemann.subtitlebuddy.events.SrtFontColorChangeEvent;
import io.github.vincemann.subtitlebuddy.events.SwitchSrtDisplayerEvent;

public interface SrtDisplayerEventHandler {
    public void handleFontColorChangedEvent(SrtFontColorChangeEvent e);
    public void handleFontChangedEvent(SrtFontChangeEvent e);
    public void handleMovieTextPositionChangedEvent(MovieTextPositionChangedEvent e);
    public void handleSwitchSrtDisplayerEvent(SwitchSrtDisplayerEvent e);
}
