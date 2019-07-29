package io.github.vincemann.subtitleBuddy.util.FxUtils;

import javafx.scene.Node;

public interface ResizeFunction {

    public void onResize(Node node, double h, double w, double deltaH, double deltaW);
}
