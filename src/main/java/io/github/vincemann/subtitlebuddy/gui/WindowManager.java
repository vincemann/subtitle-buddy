package io.github.vincemann.subtitlebuddy.gui;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class WindowManager {

    private Map<String, Stage> stages;
    private Map<String, Object> controllers;

    public WindowManager() {
        this.stages = new HashMap<>();
        this.controllers = new HashMap<>();
    }

    public void addStage(String name, Stage stage, Object controller) {
        stages.put(name, stage);
        controllers.put(name, controller);
    }

    public void showStage(String name) {
        Stage stage = stages.get(name);
        if (stage != null) {
            stages.values().forEach(Stage::hide); // Hide all stages
            stage.show();
            stage.toFront();
        }
    }

    public void closeStage(String name) {
        Stage stage = stages.get(name);
        if (stage != null) {
            stage.close();
        }
    }

    public Stage getStage(String name){
        return stages.get(name);
    }

    public Object getController(String name) {
        return controllers.get(name);
    }

}
