package io.github.vincemann.subtitlebuddy.gui.options;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.util.fx.IconUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
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
        InputStream is = getClass().getClassLoader().getResourceAsStream("options-stage.fxml");
        if (is == null) {
            throw new FileNotFoundException("Cannot find options-stage.fxml file");
        }
        Parent parent = fxmlLoader.load(is);
        Stage stage = new Stage();
        stage.setScene(new Scene(parent, 450, 200));
        stage.setTitle("Options");
        stage.initModality(Modality.NONE);
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(e -> {
            stage.hide();
        });
        controller.setStage(stage);
        IconUtil.attachApplicationIconTo(stage);
        return stage;
    }
}
