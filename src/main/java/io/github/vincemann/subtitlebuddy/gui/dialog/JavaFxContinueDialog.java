package io.github.vincemann.subtitlebuddy.gui.dialog;

import com.google.inject.Singleton;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

@Singleton
public class JavaFxContinueDialog implements ContinueDialog {

    @Override
    public boolean askUserToContinue(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Please confirm");

        ButtonType buttonType = alert.showAndWait().get();
        return buttonType == ButtonType.YES;
    }
}
