package com.youneedsoftware.subtitleBuddy.gui.srtDisplayer;

import com.youneedsoftware.subtitleBuddy.srt.Timestamp;
import javafx.scene.paint.Color;


public interface SettingsSrtDisplayer extends SrtDisplayer{
    Color DEFAULT_FONT_COLOR = Color.BLACK;

    public void setTime(Timestamp time);

}
