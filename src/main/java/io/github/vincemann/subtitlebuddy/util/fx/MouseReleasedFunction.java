package io.github.vincemann.subtitlebuddy.util.fx;

import javafx.scene.input.MouseEvent;

@FunctionalInterface
public interface MouseReleasedFunction {

    void handleMouseEvent(MouseEvent mouseEvent, double deltaX, double deltaY);
}
