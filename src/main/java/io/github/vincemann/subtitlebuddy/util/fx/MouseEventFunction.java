package io.github.vincemann.subtitlebuddy.util.fx;

import javafx.scene.input.MouseEvent;

@FunctionalInterface
public interface MouseEventFunction {

    void handleMouseEvent(MouseEvent mouseEvent);
}
