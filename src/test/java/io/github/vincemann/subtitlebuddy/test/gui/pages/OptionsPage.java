package io.github.vincemann.subtitlebuddy.test.gui.pages;

import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.test.gui.GuiTest;
import io.github.vincemann.subtitlebuddy.util.fx.FxThreadUtils;
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

    public Color selectRandomColorThatIsNot(Color defaultColor) throws TimeoutException {
        getDriver().waitForVisibleNode(FxIds.COLOR_CHOOSER_ID);
        ColorPicker colorPicker = openColorPicker(FxIds.COLOR_CHOOSER_ID);
        return pickRandomColorThatIsNot(colorPicker, defaultColor);
    }

    private Color pickRandomColorThatIsNot(ColorPicker colorPicker, Color defaultColor) {
        int random = new Random().nextInt(6) + 1; // Random number between 1 and 6
        for (int i = 0; i < random; i++) {
            getDriver().type(KeyCode.TAB);
        }
        getDriver().type(KeyCode.ENTER);
        Color pickedColor = colorPicker.getValue();

        // Recursively select a new color if the picked color matches the default
        if (pickedColor.equals(defaultColor)) {
            return pickRandomColorThatIsNot(colorPicker, defaultColor);
        }
        return pickedColor;
    }

    public FontBundle selectNewFont(FontBundle oldFont) throws TimeoutException {
       return selectNewFont(oldFont,1);
    }

    private FontBundle selectNewFont(FontBundle oldFont, int amountKeyDown) throws TimeoutException {
        getDriver().waitForVisibleNode(FxIds.FONT_CHOICE_BOX_ID);

        ChoiceBox<FontBundle> fontChoiceBox = openChoiceBox(FxIds.FONT_CHOICE_BOX_ID);
        for (int i = 0; i < amountKeyDown; i++) {
            getDriver().type(KeyCode.TAB);
        }
        getDriver().type(KeyCode.ENTER);
        FontBundle newFont = fontChoiceBox.getSelectionModel().getSelectedItem();
        if(newFont.equals(oldFont)){
            return selectNewFont(oldFont,amountKeyDown+1);
        }else {
            return newFont;
        }
    }

    private ColorPicker openColorPicker(String nodeId) throws TimeoutException {
        getDriver().focusNode(nodeId);
        ColorPicker colorPicker = getDriver().find(nodeId);
        if (!colorPicker.isFocused()) {
            getDriver().clickOn(colorPicker);
        }
        FxThreadUtils.runOnFxThreadAndWait(colorPicker::show);
        return colorPicker;
    }

    private <T> ChoiceBox<T> openChoiceBox(String nodeId) throws TimeoutException {
        getDriver().focusNode(nodeId);
        ChoiceBox<T> choiceBox = getDriver().find(nodeId);
        if (!choiceBox.isFocused()) {
            getDriver().clickOn(choiceBox);
        }
        FxThreadUtils.runOnFxThreadAndWait(choiceBox::show);
        return choiceBox;
    }

}
