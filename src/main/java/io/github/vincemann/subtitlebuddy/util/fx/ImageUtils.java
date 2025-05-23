package io.github.vincemann.subtitlebuddy.util.fx;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    public static ImageView loadImageView(String resourcePath, Vector2D size) {
        ImageView imageView = null;
        try (InputStream is = ImageUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }
            Image image = new Image(is);
            imageView = new ImageView(image);
            imageView.setFitHeight(size.getX());
            imageView.setFitWidth(size.getY());
            imageView.setCache(true);
            imageView.setPreserveRatio(true);
        } catch (IOException e) {
            // Handle IO exceptions or other issues here
            e.printStackTrace();
        }
        return imageView;
    }
}
