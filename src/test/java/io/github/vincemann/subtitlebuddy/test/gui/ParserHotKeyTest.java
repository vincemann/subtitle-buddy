package io.github.vincemann.subtitlebuddy.test.gui;


import com.google.common.eventbus.EventBus;
import io.github.vincemann.subtitlebuddy.events.ToggleHotKeyEvent;
import io.github.vincemann.subtitlebuddy.test.gui.pages.SettingsPage;
import io.github.vincemann.subtitlebuddy.gui.stages.MovieStageController;
import io.github.vincemann.subtitlebuddy.gui.stages.SettingsStageController;
import io.github.vincemann.subtitlebuddy.listeners.key.HotKey;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtParser;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.RunningState;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.concurrent.TimeoutException;

public class ParserHotKeyTest extends GuiTest {


    private SrtParser srtParser;
    private SettingsPage settingsPage;

    private EventBus eventBus;


    @Override
    public void setUpClass() throws Exception {
        super.setUpClass();
        this.srtParser = getApplicationInjector().getInstance(SrtParser.class);
        this.eventBus = getApplicationInjector().getInstance(EventBus.class);
        this.settingsPage = new SettingsPage(this);
    }

    @Test
    public void testStartParserBySpace(){
        eventBus.post(new ToggleHotKeyEvent(HotKey.START_STOP,false));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_UNSTARTED, srtParser.getCurrentState());
        type(KeyCode.SPACE);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtParser.getCurrentState());
    }

    @Test
    public void testStartStopParserBySpace(){
        eventBus.post(new ToggleHotKeyEvent(HotKey.START_STOP,false));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_UNSTARTED, srtParser.getCurrentState());

        type(KeyCode.SPACE);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtParser.getCurrentState());

        type(KeyCode.SPACE);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_SUSPENDED, srtParser.getCurrentState());


        type(KeyCode.SPACE);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtParser.getCurrentState());
    }

    @Test
    public void testNextClickCountsSettingsMode() throws TimeoutException {
        eventBus.post(new ToggleHotKeyEvent(HotKey.NEXT_CLICK,false));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_UNSTARTED, srtParser.getCurrentState());

        press(KeyCode.ALT).type(KeyCode.N).release(KeyCode.ALT);
        Stage settingsStage = findStageController(SettingsStageController.class).getStage();
        Point2D nextToSettingsStage = new Point2D(settingsStage.getX()+settingsStage.getWidth()+1,settingsStage.getY());
        clickOn(nextToSettingsStage);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtParser.getCurrentState());

        focusStage(SettingsStageController.class);
        refreshGui();
        press(KeyCode.ALT).type(KeyCode.N).release(KeyCode.ALT);
        clickOn(nextToSettingsStage);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_SUSPENDED, srtParser.getCurrentState());
    }

    @Test
    public void testNextClickCountsMovieMode() throws TimeoutException {
        eventBus.post(new ToggleHotKeyEvent(HotKey.NEXT_CLICK,false));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_UNSTARTED, srtParser.getCurrentState());
        settingsPage.switchToMovieMode();
        refreshGui();

        press(KeyCode.ALT).type(KeyCode.N).release(KeyCode.ALT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point2D middleOfScreen = new Point2D(screenSize.getWidth()/2,screenSize.getWidth()/2);
        clickOn(middleOfScreen);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtParser.getCurrentState());
        refreshGui();
        focusStage(MovieStageController.class);
        refreshGui();
        press(KeyCode.ALT).type(KeyCode.N).release(KeyCode.ALT);
        clickOn(middleOfScreen);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_SUSPENDED, srtParser.getCurrentState());
    }


}
