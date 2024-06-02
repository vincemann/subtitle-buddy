package io.github.vincemann.subtitlebuddy.gui.movie;

import io.github.vincemann.subtitlebuddy.util.fx.DrawingUtil;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class StageResizer {
    @Setter
    private Stage stage;

    // internal

    private Vector2D stagePos;

    private Vector2D stageSize;

    private VBox vbox;

    private AnchorPane anchorPane;

    private boolean initialized = false;

    public StageResizer(Vector2D stagePos, Vector2D stageSize, VBox vbox, AnchorPane anchorPane) {
        this.stagePos = stagePos;
        this.stageSize = stageSize;
        this.vbox = vbox;
        this.anchorPane = anchorPane;
    }

    public void adjust() {
        asserInitialized();
        adjustSize();
        adjustPos();
    }

    public void adjustSize(){
        asserInitialized();
        log.debug("settings stage size: w/h " + stageSize.getX() +" " + stageSize.getY());
        stage.setWidth(stageSize.getX());
        stage.setHeight(stageSize.getY());
    }

    public void adjustPos(){
        asserInitialized();
        log.debug("settings stage pos: x/y " + stagePos.getX() +" " + stagePos.getY());
        stage.setX(stagePos.getX());
        stage.setY(stagePos.getY());
        Platform.runLater(this::centerVBoxInPane);
    }

    private void asserInitialized(){
        if (stage == null)
            throw new IllegalStateException("stage not set");
        if (!initialized)
            throw new IllegalStateException("bindings not initialized");
    }


    private void centerVBoxInPane() {
        // Log dimensions
        log.debug("AnchorPane height: " + anchorPane.getHeight());
        log.debug("AnchorPane width: " + anchorPane.getWidth());
        log.debug("VBox height: " + vbox.getHeight());
        log.debug("VBox width: " + vbox.getWidth());

        // Center the VBox within the AnchorPane
        double anchorTop = (anchorPane.getHeight() - vbox.getHeight()) / 2;
        double anchorLeft = (anchorPane.getWidth() - vbox.getWidth()) / 2;

        log.debug("vbox top anchor: " + anchorTop);
        log.debug("vbox left anchor: " + anchorLeft);

        if (anchorTop >= 0 && anchorLeft >= 0) {
            AnchorPane.setTopAnchor(vbox, anchorTop);
            AnchorPane.setLeftAnchor(vbox, anchorLeft);
        } else {
            log.error("Calculated anchors are out of bounds: Top=" + anchorTop + ", Left=" + anchorLeft);
        }
    }
    private void bindAnchorPaneAndBoxToStage(){
        if (stage == null)
            throw new IllegalStateException("stage not set");
        // Bind the movieBox size to the scene size
        // Add listeners to update the size of movieBox when the stage is resized
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double anchorWidth = newVal.doubleValue();
            double vboxWidth = anchorWidth - anchorWidth/3;
            log.debug("setting anchor width to: " + anchorWidth);
            log.debug("setting vbox width to: " + vboxWidth);
            anchorPane.setPrefWidth(anchorWidth);
            vbox.setPrefWidth(vboxWidth);
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double anchorHeight = newVal.doubleValue();
            double vboxHeight = anchorHeight - anchorHeight/3;
            log.debug("setting anchor height to: " + anchorHeight);
            log.debug("setting vbox height to: " + vboxHeight);
            anchorPane.setPrefHeight(anchorHeight);
            vbox.setPrefHeight(vboxHeight);
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        bindAnchorPaneAndBoxToStage();
        initialized = true;
    }

    public void updatePos(Vector2D pos) {
        stagePos = pos;
        anchorPane.setLayoutX(0);
        anchorPane.setLayoutY(0);
        adjustPos();
    }

    public void updateSize(Vector2D size) {
        stageSize = size;
        adjustSize();
    }
}
