package io.github.vincemann.subtitlebuddy.options;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.scene.paint.Color;


public interface OptionsManager {
    Options parseOptions();

    void updateCurrentFont(String fonts);

    void updateFontColor(Color color);

    void updateSubtitlePos(Vector2D pos);

    void updateSpaceHotkeyEnabled(boolean value);

    void updateNextClickHotkeyEnabled(boolean value);

    void updateMovieFontSize(int size);

    void updateEndMovieModeHotkeyEnabled(boolean value);
}
