package io.github.vincemann.subtitlebuddy.gui.event;

import io.github.vincemann.subtitlebuddy.events.UpdateSubtitlePosEvent;
import io.github.vincemann.subtitlebuddy.events.UpdateCurrentFontEvent;
import io.github.vincemann.subtitlebuddy.events.UpdateFontColorEvent;
import io.github.vincemann.subtitlebuddy.events.SwitchSrtDisplayerEvent;

public interface SrtDisplayerEventHandler {
    public void handleUpdateFontColorEvent(UpdateFontColorEvent e);
    public void handleUpdateCurrentFontEvent(UpdateCurrentFontEvent e);
    public void handleUpdateSubtitlePosEvent(UpdateSubtitlePosEvent e);
    public void handleSwitchSrtDisplayerEvent(SwitchSrtDisplayerEvent e);
}
