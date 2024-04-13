package io.github.vincemann.subtitlebuddy.util.fx;

import javafx.scene.Node;

@FunctionalInterface
public interface ResizeFunction {

    void onResize(Node node, double h, double w, double deltaH, double deltaW);
}
