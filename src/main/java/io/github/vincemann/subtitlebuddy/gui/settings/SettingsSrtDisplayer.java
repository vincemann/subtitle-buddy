package io.github.vincemann.subtitlebuddy.gui.settings;

import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.srt.Timestamp;
import javafx.scene.paint.Color;


/**
 * Interface for interactions with settings window.
 */
public interface SettingsSrtDisplayer extends SrtDisplayer {
    Color DEFAULT_FONT_COLOR = Color.BLACK;

    void displayTime(Timestamp time);
}
