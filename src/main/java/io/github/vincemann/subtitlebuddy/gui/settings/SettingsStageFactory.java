package io.github.vincemann.subtitlebuddy.gui.settings;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.util.fx.IconUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Singleton
public class SettingsStageFactory {

    private SettingsStageController controller;

    @Inject
    public SettingsStageFactory(SettingsStageController controller) {
        this.controller = controller;
    }

    public Stage create(Stage stage) throws IOException {
        // for some reason it wont work when setting loader.setControllerFactory( clazz -> injector.getInstance(clazz))) - the factory is never called
        // thats why I do it the less clean way
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(controller);
        InputStream is = getClass().getClassLoader().getResourceAsStream("settings-stage.fxml");
        if (is == null) {
            throw new FileNotFoundException("Cannot find settings-stage.fxml file");
        }
        Parent parent = fxmlLoader.load(is);
//        Stage stage = new Stage();
        stage.setScene(new Scene(parent, 280, 340));
        stage.setTitle("Subtitle Buddy");
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        controller.setStage(stage);
        IconUtil.attachApplicationIconTo(stage);
        return stage;
    }
}
