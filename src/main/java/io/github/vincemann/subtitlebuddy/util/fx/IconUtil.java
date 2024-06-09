package io.github.vincemann.subtitlebuddy.util.fx;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.lang.reflect.Method;

@Log4j2
public class IconUtil {

    public static void attachApplicationIconTo(Stage stage) {
        try {
            String iconPath = "icon.png";
            // Load and set the application icon for the window
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

    public static void setMacOSDockIcon(String iconPath) {
        try {
            InputStream inputStream = IconUtil.class.getClassLoader().getResourceAsStream(iconPath);
            if (inputStream == null) {
                throw new IllegalArgumentException("Dock icon not found: " + iconPath);
            }
            java.awt.Image dockIcon = javax.imageio.ImageIO.read(inputStream);
            if (dockIcon == null) {
                throw new RuntimeException("Failed to load dock icon image from: " + iconPath);
            }

            // Use reflection to set the dock icon to avoid compile-time dependencies
            Class<?> appClass = Class.forName("com.apple.eawt.Application");
            Method getApplicationMethod = appClass.getMethod("getApplication");
            Object application = getApplicationMethod.invoke(null);
            Method setDockIconImageMethod = appClass.getMethod("setDockIconImage", java.awt.Image.class);
            setDockIconImageMethod.invoke(application, dockIcon);
        } catch (Exception e) {
            log.error("could not set macOS dock icon", e);
        }
    }


}
