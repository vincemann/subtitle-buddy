package io.github.vincemann.subtitlebuddy.gui.movie;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
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

    private VBox movieBox;

    public StageResizer(Vector2D stagePos, Vector2D stageSize, VBox movieBox) {
        this.stagePos = stagePos;
        this.stageSize = stageSize;
        this.movieBox = movieBox;
    }

    public void adjust() {
        if (stage == null)
            throw new IllegalStateException("stage not set");
        adjustSize();
        adjustPos();
    }

    public void adjustSize(){
        if (stage == null)
            throw new IllegalStateException("stage not set");
        log.debug("settings stage size: w/h " + stageSize.getX() +" " + stageSize.getY());
        stage.setWidth(stageSize.getX());
        stage.setHeight(stageSize.getY());
    }

    public void adjustPos(){
        if (stage == null)
            throw new IllegalStateException("stage not set");
        log.debug("settings stage pos: x/y " + stagePos.getX() +" " + stagePos.getY());
        stage.setX(stagePos.getX());
        stage.setY(stagePos.getY());
    }

    private void bindMovieBoxToScene(){
        if (stage == null)
            throw new IllegalStateException("stage not set");
        // Bind the movieBox size to the scene size
        // Add listeners to update the size of movieBox when the stage is resized
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            movieBox.setPrefWidth(newVal.doubleValue());
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            movieBox.setPrefHeight(newVal.doubleValue());
        });

    }

    public void setStage(Stage stage) {
        this.stage = stage;
        bindMovieBoxToScene();
    }

    public void updatePos(Vector2D pos) {
        this.stagePos = pos;
        adjustPos();
    }
}
