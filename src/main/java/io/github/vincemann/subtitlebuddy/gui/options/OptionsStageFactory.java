package io.github.vincemann.subtitlebuddy.gui.options;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

@Singleton
public class OptionsStageFactory {

    private FXMLLoader loader;

    @Inject
    public OptionsStageFactory(FXMLLoader loader) {
        this.loader = loader;
    }

    public Stage create(Stage primaryStage) throws IOException {
        InputStream fxmlInputStream = ClassLoader.getSystemResourceAsStream("options-stage.fxml");
        Parent parent = loader.load(fxmlInputStream);
        primaryStage.setScene(new Scene(parent, 450, 200));
        Stage stage = new Stage();
        stage.setTitle("Options");
        stage.initModality(Modality.NONE);
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(e -> {
            stage.hide();
        });
        return stage;
    }
}
