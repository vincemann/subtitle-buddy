package io.github.vincemann.subtitlebuddy.util;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class ScreenUtils {
    public static Vector2D getScreenBoundsVector(){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        return new Vector2D(screenBounds.getWidth()-1,screenBounds.getHeight()-1);
    }
}
