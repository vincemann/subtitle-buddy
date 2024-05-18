package io.github.vincemann.subtitlebuddy.gui.settings;

import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.srt.Timestamp;
import javafx.scene.paint.Color;


public interface SettingsSrtDisplayer extends SrtDisplayer {
    Color DEFAULT_FONT_COLOR = Color.BLACK;

    void setTime(Timestamp time);

}
