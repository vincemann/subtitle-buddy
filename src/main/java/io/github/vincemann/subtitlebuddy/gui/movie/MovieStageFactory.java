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

    private FXMLLoader loader;

    @Inject
    public MovieStageFactory(FXMLLoader loader) {
        this.loader = loader;
    }

    public Stage create(Stage primaryStage) throws IOException {
        InputStream fxmlInputStream = ClassLoader.getSystemResourceAsStream("movie-stage.fxml");
        Parent parent = loader.load(fxmlInputStream);
        Vector2D screenVec = ScreenUtils.getScreenBoundsVector();
        primaryStage.setScene(new Scene(parent,screenVec.getX(), screenVec.getY()));
        Stage stage = new Stage();
        stage.setTitle("Movie Mode");
        stage.getScene().setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(e -> {
            stage.hide();
        });
        return stage;
    }
}
