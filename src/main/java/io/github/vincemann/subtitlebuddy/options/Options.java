package io.github.vincemann.subtitlebuddy.options;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.scene.paint.Color;

/**
 * Global options/state that can be configured and is shared across the application.
 */
public class Options {

    // font options

    // filename of current font
    private String currentFont;
    private Color fontColor;
    private int settingsFontSize;
    private int movieFontSize;

    // srt displayer options

    private Vector2D subtitlePosition;
    private Boolean nextClickHotkeyEnabled;
    private Boolean spaceHotkeyEnabled;
    private String defaultSubtitle;
    private String encoding;

    private boolean endMovieModeHotkeyEnabled;

    private boolean defaultSubtitleVisible = true;



    Options() {
    }

    void setDefaultSubtitle(String defaultSubtitle) {
        this.defaultSubtitle = defaultSubtitle;
    }

    void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    void setNextClickHotkeyEnabled(Boolean nextClickHotkeyEnabled) {
        this.nextClickHotkeyEnabled = nextClickHotkeyEnabled;
    }

    void setSpaceHotkeyEnabled(Boolean spaceHotkeyEnabled) {
        this.spaceHotkeyEnabled = spaceHotkeyEnabled;
    }

    public String getDefaultSubtitle() {
        return defaultSubtitle;
    }

    public String getEncoding() {
        return encoding;
    }

    public boolean getEndMovieModeHotkeyEnabled() {
        return endMovieModeHotkeyEnabled;
    }

    public Boolean getNextClickHotkeyEnabled() {
        return nextClickHotkeyEnabled;
    }

    public Boolean getSpaceHotkeyEnabled() {
        return spaceHotkeyEnabled;
    }

    public String getCurrentFont() {
        return currentFont;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public int getSettingsFontSize() {
        return settingsFontSize;
    }

    public int getMovieFontSize() {
        return movieFontSize;
    }

    public Vector2D getSubtitlePosition() {
        return subtitlePosition;
    }


    void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    void setSettingsFontSize(int settingsFontSize) {
        this.settingsFontSize = settingsFontSize;
    }

    void setMovieFontSize(int movieFontSize) {
        this.movieFontSize = movieFontSize;
    }

    void setSubtitlePosition(Vector2D subtitlePosition) {
        this.subtitlePosition = subtitlePosition;
    }

    void setCurrentFont(String currentFont) {
        this.currentFont = currentFont;
    }

    void setEndMovieModeHotkeyEnabled(boolean enabled) {
        this.endMovieModeHotkeyEnabled = enabled;
    }

    public boolean isDefaultSubtitleVisible() {
        return defaultSubtitleVisible;
    }

    void setDefaultSubtitleVisible(boolean defaultSubtitleVisible) {
        this.defaultSubtitleVisible = defaultSubtitleVisible;
    }
}
