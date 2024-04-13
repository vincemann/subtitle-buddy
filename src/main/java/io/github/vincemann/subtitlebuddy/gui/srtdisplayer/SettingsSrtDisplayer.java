package io.github.vincemann.subtitlebuddy.gui.srtdisplayer;

import io.github.vincemann.subtitlebuddy.srt.Timestamp;
import javafx.scene.paint.Color;


public interface SettingsSrtDisplayer extends SrtDisplayer{
    Color DEFAULT_FONT_COLOR = Color.BLACK;

    public void setTime(Timestamp time);

}
