package gui.pages;

import com.youneedsoftware.subtitleBuddy.util.vec2d.Vector2D;
import gui.GuiTest;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.concurrent.TimeoutException;

import static gui.pages.FxTestConstants.CURRENT_TIME_STAMP_TEXT_ID;
import static gui.pages.FxTestConstants.SETTINGS_TEXT_FLOW_ID;


public class SettingsPage extends AbstractPage{

    public SettingsPage(GuiTest driver) {
        super(driver);
    }

    public SettingsPage pressStart(){
         getDriver().clickOn(FxTestConstants.START_BUTTON_ID);
        return this;
    }

    public SettingsPage pressStop(){
         getDriver().clickOn(FxTestConstants.STOP_BUTTON_ID);
        return this;
    }

    public SettingsPage enterTimeStamp(String timeStamp){
         getDriver().clickOn(FxTestConstants.TIME_FIELD_ID).write(timeStamp).type(KeyCode.ENTER);
        return this;
    }

    public boolean isTimeStampWarningShowing(){
        Text text =  getDriver().find(FxTestConstants.WRONG_FORMAT_TEXT_WARNING__ID);
        return text.isVisible();
    }

    public String findDisplayedSubtitleText(){
        TextFlow settingsTextFlow =  getDriver().find(SETTINGS_TEXT_FLOW_ID);
        StringBuilder textFlowText = new StringBuilder();
        for(Node n : settingsTextFlow.getChildren()){
            textFlowText.append(((Text)n).getText());
        }
        return textFlowText.toString();
    }

    public String findDisplayedTimeStamp(){
        Text currentTimeStamp =  getDriver().find(CURRENT_TIME_STAMP_TEXT_ID);
        return currentTimeStamp.getText();
    }
    
    public MoviePage switchToMovieMode() throws TimeoutException {
        getDriver().focusNode(FxTestConstants.MOVIE_MODE_BUTTON_ID);
        getDriver().clickOn(FxTestConstants.MOVIE_MODE_BUTTON_ID);
        return new MoviePage(getDriver());
    }

    public OptionsPage openOptionsWindow(Stage optionsStage, Vector2D vector2D) throws TimeoutException {
        getDriver().focusNode(FxTestConstants.OPTIONS_BUTTON_ID);
        getDriver().refreshGui();
        optionsStage.setX(vector2D.getX());
        optionsStage.setY(vector2D.getY());
        getDriver().clickOn(FxTestConstants.OPTIONS_BUTTON_ID);
        return new OptionsPage(getDriver());
    }


}
