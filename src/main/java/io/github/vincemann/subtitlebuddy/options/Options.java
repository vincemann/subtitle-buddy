package io.github.vincemann.subtitlebuddy.options;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.scene.paint.Color;

/**
 * Global options/state that can be configured and is shared across the application.
 */
public class Options {

    private String currentFontPath;
    private Color fontColor;
    private long settingsFontSize;
    private long movieFontSize;
    private Vector2D subtitlePosition;

    Options() {
    }

    public String getCurrentFontPath() {
        return currentFontPath;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public long getSettingsFontSize() {
        return settingsFontSize;
    }

    public long getMovieFontSize() {
        return movieFontSize;
    }

    public Vector2D getSubtitlePosition() {
        return subtitlePosition;
    }

    void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    void setSettingsFontSize(long settingsFontSize) {
        this.settingsFontSize = settingsFontSize;
    }

    void setMovieFontSize(long movieFontSize) {
        this.movieFontSize = movieFontSize;
    }

    void setSubtitlePosition(Vector2D subtitlePosition) {
        this.subtitlePosition = subtitlePosition;
    }

    void setCurrentFontPath(String currentFontPath) {
        this.currentFontPath = currentFontPath;
    }
}
