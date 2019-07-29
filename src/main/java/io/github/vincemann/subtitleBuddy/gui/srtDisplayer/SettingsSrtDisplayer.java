package io.github.vincemann.subtitleBuddy.gui.srtDisplayer;

import io.github.vincemann.subtitleBuddy.srt.Timestamp;
import javafx.scene.paint.Color;


public interface SettingsSrtDisplayer extends SrtDisplayer{
    Color DEFAULT_FONT_COLOR = Color.BLACK;

    public void setTime(Timestamp time);

}
