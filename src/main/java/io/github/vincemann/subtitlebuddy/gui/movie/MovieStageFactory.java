package io.github.vincemann.subtitlebuddy.gui.movie;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.util.ScreenUtils;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;

@Singleton
public class MovieStageFactory {

    private MovieStageController controller;

    @Inject
    public MovieStageFactory(MovieStageController controller) {
        this.controller = controller;
    }

    public Stage create() throws IOException {
        // for some reason it wont work when setting loader.setControllerFactory( clazz -> injector.getInstance(clazz))) - the factory is never called
        // thats why I do it the less clean way
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(controller);
        InputStream is = ClassLoader.getSystemResourceAsStream("movie-stage.fxml");
        Parent parent = fxmlLoader.load(is);
        Vector2D screenVec = ScreenUtils.getScreenBounds();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent,screenVec.getX(), screenVec.getY()));
        stage.setTitle("Movie Mode");
        stage.getScene().setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(e -> {
            stage.hide();
        });
        controller.setStage(stage);
        return stage;
    }
}
