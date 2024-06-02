package io.github.vincemann.subtitlebuddy.util.fx;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class DrawingUtil {

    // Static method to draw a blue point on a new full-screen stage at the given coordinates
    public static void drawTemporaryBluePoint(Stage parentStage, double x, double y, double durationSeconds) {
        // Create a new full-screen Stage
        Stage tempStage = new Stage(StageStyle.TRANSPARENT);
        tempStage.initOwner(parentStage);

        // Get screen dimensions
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        // Create a Canvas that covers the entire screen
        Canvas canvas = new Canvas(screenWidth, screenHeight);

        // Get the GraphicsContext to draw
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillOval(x - 2.5, y - 2.5, 5, 5); // Draw a small circle to represent the point

        // Create a layout and add the canvas to it
        Pane root = new Pane();
        root.getChildren().add(canvas);

        // Set up the Scene and Stage
        Scene scene = new Scene(root, screenWidth, screenHeight);
        scene.setFill(null); // Make the scene transparent
        tempStage.setScene(scene);

        // Show the temporary full-screen Stage
        tempStage.setFullScreen(true);
        tempStage.show();

        // Set up a PauseTransition to close the temporary stage after the specified duration
        PauseTransition pause = new PauseTransition(Duration.seconds(durationSeconds));
        pause.setOnFinished(event -> {
            tempStage.close();
            parentStage.requestFocus(); // Restore focus to the parent stage
        });
        pause.play();
    }
}

