package io.github.vincemann.subtitlebuddy.gui.movie;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

/**
 * Component used for adjusting size and pos of vbox within anchor pane within stage
 * and keep all components in sync in terms of size and position.
 */
@Log4j2
public class UpdateStageManager {
    private Stage stage;
    private VBox vbox;

    private AnchorPane anchorPane;

    private TextFlow textFlow;

    public UpdateStageManager(Stage stage, VBox vbox, AnchorPane anchorPane, TextFlow textFlow) {
        this.textFlow = textFlow;
        if (stage == null)
            throw new IllegalStateException("stage cannot be null");
        this.stage = stage;
        this.vbox = vbox;
        this.anchorPane = anchorPane;
        bindComponentsToStageSize();
    }


    public void updateSize(Vector2D stageSize){
        log.debug("settings stage size: w/h " + stageSize.getX() +" " + stageSize.getY());
        stage.setWidth(stageSize.getX());
        stage.setHeight(stageSize.getY());
        updateAnchorPane();
        Platform.runLater(this::centerVBoxInPane);
    }

    public void updatePos(Vector2D stagePos){
        log.debug("settings stage pos: x/y " + stagePos.getX() +" " + stagePos.getY());
        stage.setX(stagePos.getX());
        stage.setY(stagePos.getY());
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
            log.error("Calculated anchors are out of bounds: Top=" + anchorTop + ", Left=" + anchorLeft);
        }
    }
    private void bindComponentsToStageSize(){
        if (stage == null)
            throw new IllegalStateException("stage not set");
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double anchorWidth = newVal.doubleValue();
            double vboxWidth = anchorWidth;
            double textFlowWidth = vboxWidth;
//            log.debug("setting anchor width to: " + anchorWidth);
//            log.debug("setting vbox width to: " + vboxWidth);
            anchorPane.setPrefWidth(anchorWidth);
            vbox.setPrefWidth(vboxWidth);
            textFlow.setPrefWidth(textFlowWidth);
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            double anchorHeight = newVal.doubleValue();
            double vboxHeight = anchorHeight;
            double textFlowHeight = vboxHeight;
//            log.debug("setting anchor height to: " + anchorHeight);
//            log.debug("setting vbox height to: " + vboxHeight);
            anchorPane.setPrefHeight(anchorHeight);
            vbox.setPrefHeight(vboxHeight);
            textFlow.setPrefHeight(textFlowHeight);
        });
    }
}
