package io.github.vincemann.subtitlebuddy.test.gui.pages;

import io.github.vincemann.subtitlebuddy.srt.SrtFonts;
import io.github.vincemann.subtitlebuddy.test.gui.GuiTest;
import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.concurrent.TimeoutException;


public class OptionsPage extends AbstractPage {

    public OptionsPage(GuiTest driver) {
        super(driver);
    }

    public Color selectRandomColor(Color defaultColor) throws TimeoutException {
        // Ensure the node is visible and can receive focus
        getDriver().waitForVisibleNode(FxTestConstants.COLOR_CHOOSE_ID);
        ColorPicker colorChooser = getDriver().find(FxTestConstants.COLOR_CHOOSE_ID);
        if (!colorChooser.isFocused()) {
            getDriver().clickOn(colorChooser);
        }

        Platform.runLater(colorChooser::show);

        // Choose a random color
        return pickColor(colorChooser, defaultColor);
    }

    private Color pickColor(ColorPicker colorPicker, Color defaultColor) {
        int random = new Random().nextInt(6) + 1; // Random number between 1 and 6
        for (int i = 0; i < random; i++) {
            getDriver().type(KeyCode.DOWN);
        }
        getDriver().type(KeyCode.ENTER);
        Color pickedColor = colorPicker.getValue();

        // Recursively select a new color if the picked color matches the default
        if (pickedColor.equals(defaultColor)) {
            return pickColor(colorPicker, defaultColor);
        }
        return pickedColor;
    }



    public SrtFonts selectNewFont(SrtFonts oldFont) throws TimeoutException {
       return selectNewFont(oldFont,1);
    }

    private SrtFonts selectNewFont(SrtFonts oldFont, int amountKeyDown) throws TimeoutException {
        getDriver().waitForVisibleNode(FxTestConstants.FONT_CHOICE_BOX_ID);
        getDriver().focusNode(FxTestConstants.FONT_CHOICE_BOX_ID);
        ChoiceBox<SrtFonts> fontChoiceBox = getDriver().find(FxTestConstants.FONT_CHOICE_BOX_ID);
        getDriver().clickOn(fontChoiceBox);
        for (int i = 0; i < amountKeyDown; i++) {
            getDriver().type(KeyCode.DOWN);
        }
        getDriver().type(KeyCode.ENTER);
        SrtFonts newFont = fontChoiceBox.getSelectionModel().getSelectedItem();
        if(newFont.equals(oldFont)){
            return selectNewFont(oldFont,amountKeyDown+1);
        }else {
            return newFont;
        }
    }

}
