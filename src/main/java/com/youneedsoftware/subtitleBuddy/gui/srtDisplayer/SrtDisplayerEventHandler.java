package com.youneedsoftware.subtitleBuddy.gui.srtDisplayer;

import com.youneedsoftware.subtitleBuddy.events.MovieTextPositionChangedEvent;
import com.youneedsoftware.subtitleBuddy.events.SrtFontChangeEvent;
import com.youneedsoftware.subtitleBuddy.events.SrtFontColorChangeEvent;
import com.youneedsoftware.subtitleBuddy.events.SwitchSrtDisplayerEvent;

public interface SrtDisplayerEventHandler {
    public void handleFontColorChangedEvent(SrtFontColorChangeEvent e);
    public void handleFontChangedEvent(SrtFontChangeEvent e);
    public void handleMovieTextPositionChangedEvent(MovieTextPositionChangedEvent e);
    public void handleSwitchSrtDisplayerEvent(SwitchSrtDisplayerEvent e);
}
