package io.github.vincemann.subtitlebuddy.options;

import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.scene.paint.Color;


public interface OptionsManager {
    Options parseOptions();

    void updateCurrentFontPath(String fonts);

    void updateFontColor(Color color);

    void updateSubtitlePos(Vector2D pos);
}
