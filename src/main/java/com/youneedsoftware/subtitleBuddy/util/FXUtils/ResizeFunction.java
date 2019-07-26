package com.youneedsoftware.subtitleBuddy.util.FXUtils;

import javafx.scene.Node;

public interface ResizeFunction {

    public void onResize(Node node, double h, double w, double deltaH, double deltaW);
}
