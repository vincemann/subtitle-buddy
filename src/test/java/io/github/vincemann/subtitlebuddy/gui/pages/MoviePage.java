package io.github.vincemann.subtitlebuddy.gui.pages;

import io.github.vincemann.subtitlebuddy.gui.GuiTest;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


public class MoviePage extends AbstractPage{

    public MoviePage(GuiTest driver) {
        super(driver);
    }


    public SettingsPage switchToSettingsPage()  {
        TextFlow movieTextFlow = getDriver().find(FxTestConstants.MOVIE_TEXT_FLOW_ID);
        System.out.println(movieTextFlow.isVisible());
        System.out.println(movieTextFlow.getLayoutX());
        System.out.println(movieTextFlow.getLayoutY());
        System.out.println(movieTextFlow.getWidth());
        System.out.println(movieTextFlow.getHeight());
        getDriver().moveTo(movieTextFlow).doubleClickOn();
        return new SettingsPage(getDriver());
    }

    public String findDisplayedSubtitleText(){
        TextFlow settingsTextFlow =  getDriver().find(FxTestConstants.MOVIE_TEXT_FLOW_ID);
        StringBuilder textFlowText = new StringBuilder();
        for(Node n : settingsTextFlow.getChildren()){
            textFlowText.append(((Text)n).getText());
        }
        return textFlowText.toString();
    }


}
