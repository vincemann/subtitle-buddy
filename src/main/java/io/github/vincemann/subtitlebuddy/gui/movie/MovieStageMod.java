package io.github.vincemann.subtitlebuddy.gui.movie;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

/**
 * Component used for adjusting size and pos of stage and components within it.
 * Keeps all components in sync in terms of size and position.
 */
@Log4j2
public class MovieStageMod {
    private Stage stage;
    private VBox vbox;

    private AnchorPane anchorPane;

    // the mind width and height the stage must have to support displaying the current subtitles
    private double textMinWidth;
    private double textMinHeight;

    // min width and height of box manually set by user
    private static Double USER_MIN_WIDTH = null;
    private static Double USER_MIN_HEIGHT = null;

    public MovieStageMod(Stage stage, VBox vbox, AnchorPane anchorPane) {
        if (stage == null)
            throw new IllegalArgumentException("stage cannot be null");
        this.stage = stage;
        this.vbox = vbox;
        this.anchorPane = anchorPane;
        bindInnerComponentsToStageSize();
    }

    // these two are called when manually resizing only the box
    public void userUpdateWidth(double width){
        if (width > textMinWidth){
            USER_MIN_WIDTH = width;
            stage.setMinWidth(width);
            stage.setWidth(width);
            updateInnerComponents();
        }
    }

    public void userUpdateHeight(double height){
        if (height > textMinHeight){
            USER_MIN_HEIGHT = height;
            stage.setMinWidth(height);
            stage.setHeight(height);
            updateInnerComponents();
        }
    }

    /**
     * This method is only used to set the minimum size possible.
     * Manual adjustments via {@link this#userUpdateWidth(double)} and {@link this#userUpdateHeight(double)} or {@link this#updateSize(Vector2D)} wont be able to go below that.
     */
    public void updateMinimumSize(Vector2D stageSize){
        if (log.isTraceEnabled())
            log.trace("setting min stage size: w/h " + stageSize.getX() +" " + stageSize.getY());
        stage.setMinWidth(stageSize.getX());
        stage.setMinHeight(stageSize.getY());
        textMinWidth = stageSize.getX();
        textMinHeight = stageSize.getY();
        updateInnerComponents();
    }


    // set stages min width and height to values that the user manually set (if he set anything)
    public void initUserDefinedBounds(){
        if (USER_MIN_HEIGHT != null)
            stage.setMinHeight(USER_MIN_HEIGHT);
        if (USER_MIN_WIDTH != null)
            stage.setMinWidth(USER_MIN_WIDTH);
    }

    public void updateSize(Vector2D stageSize){
        if (log.isTraceEnabled())
            log.trace("setting stage size: w/h " + stageSize.getX() +" " + stageSize.getY());
        if (stageSize.getX() > textMinWidth)
            stage.setWidth(stageSize.getX());
        if (stageSize.getY() > textMinHeight)
            stage.setHeight(stageSize.getY());

        updateInnerComponents();
    }

    public void updatePos(Vector2D stagePos){
        if (log.isTraceEnabled())
            log.trace("setting stage pos: x/y " + stagePos.getX() +" " + stagePos.getY());
        stage.setX(stagePos.getX());
        stage.setY(stagePos.getY());
        updateInnerComponents();
    }

    private void updateInnerComponents(){
        updateAnchorPane();
        Platform.runLater(this::centerVBoxInPane);
    }

    private void updateAnchorPane(){
        anchorPane.setLayoutX(0);
        anchorPane.setLayoutY(0);
    }

    private void centerVBoxInPane() {
        // Log dimensions
//        log.trace("AnchorPane height: " + anchorPane.getHeight());
//        log.trace("AnchorPane width: " + anchorPane.getWidth());
//        log.trace("VBox height: " + vbox.getHeight());
//        log.trace("VBox width: " + vbox.getWidth());

        // Center the VBox within the AnchorPane
        double anchorTop = (anchorPane.getHeight() - vbox.getHeight()) / 2;
        double anchorLeft = (anchorPane.getWidth() - vbox.getWidth()) / 2;

//        log.trace("vbox top anchor: " + anchorTop);
//        log.trace("vbox left anchor: " + anchorLeft);

        if (anchorTop >= 0 && anchorLeft >= 0) {
            AnchorPane.setTopAnchor(vbox, anchorTop);
            AnchorPane.setLeftAnchor(vbox, anchorLeft);
        } else {
            log.debug("Calculated anchors are out of bounds: Top=" + anchorTop + ", Left=" + anchorLeft);
        }
    }

    private void bindInnerComponentsToStageSize(){
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double newStageWidth = newVal.doubleValue();
            anchorPane.setPrefWidth(newStageWidth);
            vbox.setPrefWidth(newStageWidth);
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double newStageHeight = newVal.doubleValue();
            anchorPane.setPrefHeight(newStageHeight);
            vbox.setPrefHeight(newStageHeight);
        });
    }
}
