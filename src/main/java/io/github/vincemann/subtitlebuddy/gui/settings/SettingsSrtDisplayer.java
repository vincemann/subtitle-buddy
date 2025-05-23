package io.github.vincemann.subtitlebuddy.gui.settings;

import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.srt.Timestamp;
import javafx.scene.paint.Color;


/**
 * Interface for interactions with settings window.
 */
public interface SettingsSrtDisplayer extends SrtDisplayer {

    void displayTime(Timestamp time);
}
