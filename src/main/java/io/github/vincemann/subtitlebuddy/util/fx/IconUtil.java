package io.github.vincemann.subtitlebuddy.util.fx;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;

@Log4j2
public class IconUtil {

        public static void attachApplicationIconTo(Stage stage) {
            try {
                String iconPath;
                if (isMacOS()) {
                    iconPath = "icon.icns";
                } else {
                    iconPath = "icon.png";
                }

                // Load and set the application icon
                InputStream inputStream = IconUtil.class.getClassLoader().getResourceAsStream(iconPath);
                if (inputStream == null) {
                    throw new IllegalArgumentException("Icon not found: " + iconPath);
                }
                Image icon = new Image(inputStream);
                stage.getIcons().add(icon);
            } catch (Exception e) {
                log.error("could not load icon", e);
            }
        }

        private static boolean isMacOS() {
            String osName = System.getProperty("os.name").toLowerCase();
            return osName.contains("mac");
        }
    }

}
