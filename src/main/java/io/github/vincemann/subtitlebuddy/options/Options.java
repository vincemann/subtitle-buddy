package io.github.vincemann.subtitlebuddy.options;

import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.scene.paint.Color;
import lombok.Getter;

/**
 * Global options/state that can be configured and is shared across the application.
 */
@Getter
public class Options {

    private FontBundle font;
    private Color fontColor;
    private long settingsFontSize;
    private long movieFontSize;
    private Vector2D subtitlePosition;

    void setFont(FontBundle font) {
        this.font = font;
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
}
