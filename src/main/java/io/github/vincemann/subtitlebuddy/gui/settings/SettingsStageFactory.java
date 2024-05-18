package io.github.vincemann.subtitlebuddy.gui.settings;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

@Singleton
public class SettingsStageFactory {

    private FXMLLoader loader;

    @Inject
    public SettingsStageFactory(FXMLLoader loader) {
        this.loader = loader;
    }

    public Stage create(Stage primaryStage) throws IOException {
        InputStream fxmlInputStream = ClassLoader.getSystemResourceAsStream("settings-stage.fxml");
        Parent parent = loader.load(fxmlInputStream);
        primaryStage.setScene(new Scene(parent, 280, 340));
        Stage stage = new Stage();
        stage.setTitle("Subtitle Buddy");
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        return stage;
    }
}
