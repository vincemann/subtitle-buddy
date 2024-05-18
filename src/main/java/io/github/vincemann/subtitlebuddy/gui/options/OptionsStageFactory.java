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

    private OptionsStageController controller;

    @Inject
    public OptionsStageFactory(OptionsStageController controller) {
        this.controller = controller;
    }

    public Stage create() throws IOException {
        // for some reason it wont work when setting loader.setControllerFactory( clazz -> injector.getInstance(clazz))) - the factory is never called
        // thats why I do it the less clean way
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(controller);
        InputStream is = ClassLoader.getSystemResourceAsStream("options-stage.fxml");
        Parent parent = fxmlLoader.load(is);
        Stage stage = new Stage();
        stage.setScene(new Scene(parent, 450, 200));
        stage.setTitle("Options");
        stage.initModality(Modality.NONE);
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(e -> {
            stage.hide();
        });
        return stage;
    }
}
