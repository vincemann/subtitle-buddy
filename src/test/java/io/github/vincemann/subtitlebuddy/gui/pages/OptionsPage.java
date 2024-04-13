package io.github.vincemann.subtitlebuddy.gui.pages;

import io.github.vincemann.subtitlebuddy.srt.SrtFonts;
import io.github.vincemann.subtitlebuddy.gui.GuiTest;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.concurrent.TimeoutException;


public class OptionsPage extends AbstractPage {

    public OptionsPage(GuiTest driver) {
        super(driver);
    }

    /**
     * selects a random color thats not the default color
     *
     * @return
     */
    public Color selectRandomColor(Color defaultColor) throws TimeoutException {
        getDriver().waitForVisibleNode(FxTestConstants.COLOR_CHOOSE_ID);
        getDriver().focusNode(FxTestConstants.COLOR_CHOOSE_ID);
        ColorPicker colorPicker = getDriver().find(FxTestConstants.COLOR_CHOOSE_ID);
        getDriver().clickOn(colorPicker);
        //number between 1 and 12
        Color pickedColor;
        int random = ((int) (Math.random() * 12)) + 1;
        for (int i = 0; i < random; i++) {
            getDriver().type(KeyCode.TAB);
        }
        getDriver().type(KeyCode.ENTER);
        pickedColor = colorPicker.getValue();
        if (pickedColor.equals(defaultColor)) {
            return selectRandomColor(defaultColor);
        }else {
            return pickedColor;
        }
    }


    public SrtFonts selectNewFont(SrtFonts oldFont) throws TimeoutException {
        getDriver().waitForVisibleNode(FxTestConstants.FONT_CHOICE_BOX_ID);
        getDriver().focusNode(FxTestConstants.FONT_CHOICE_BOX_ID);
        ChoiceBox<SrtFonts> fontChoiceBox = getDriver().find(FxTestConstants.FONT_CHOICE_BOX_ID);
        getDriver().clickOn(fontChoiceBox);
        getDriver().type(KeyCode.TAB);
        getDriver().type(KeyCode.ENTER);
        SrtFonts newFont = fontChoiceBox.getSelectionModel().getSelectedItem();
        if(newFont.equals(oldFont)){
            return selectNewFont(oldFont);
        }else {
            return newFont;
        }
    }

}
