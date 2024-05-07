package io.github.vincemann.subtitlebuddy.gui.dialog;

import com.google.inject.Singleton;
import javafx.scene.control.Alert;

@Singleton
public class JavaFxAlertDialog implements AlertDialog {

    @Override
    public void tellUser(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle("Information");
        alert.showAndWait();
    }
}
