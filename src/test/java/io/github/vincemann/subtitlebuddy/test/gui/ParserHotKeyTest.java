package io.github.vincemann.subtitlebuddy.test.gui;


import com.google.common.eventbus.EventBus;
import io.github.vincemann.subtitlebuddy.events.ToggleNextClickHotkeyEvent;
import io.github.vincemann.subtitlebuddy.events.ToggleSpaceHotkeyEvent;
import io.github.vincemann.subtitlebuddy.gui.Windows;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtPlayer;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.RunningState;
import io.github.vincemann.subtitlebuddy.test.gui.pages.SettingsPage;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.concurrent.TimeoutException;

public class ParserHotKeyTest extends GuiTest {


    private SrtPlayer srtPlayer;
    private SettingsPage settingsPage;
    private EventBus eventBus;

    @Before
    @Override
    public void beforeEach() throws Exception {
        super.beforeEach();
        this.srtPlayer = getInstance(SrtPlayer.class);
        this.eventBus = getInstance(EventBus.class);
        this.settingsPage = new SettingsPage(this);
    }

    @Test
    public void testStartParserBySpace() throws InterruptedException {
        eventBus.post(new ToggleSpaceHotkeyEvent(true));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_UNSTARTED, srtPlayer.getCurrentState());
        type(KeyCode.SPACE);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtPlayer.getCurrentState());
    }

    @Test
    public void testStartStopParserBySpace() throws InterruptedException {
        eventBus.post(new ToggleSpaceHotkeyEvent(true));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_UNSTARTED, srtPlayer.getCurrentState());

        type(KeyCode.SPACE);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtPlayer.getCurrentState());

        type(KeyCode.SPACE);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_SUSPENDED, srtPlayer.getCurrentState());


        type(KeyCode.SPACE);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtPlayer.getCurrentState());
    }

    @Test
    public void testNextClickCountsSettingsMode() throws TimeoutException, InterruptedException {
        eventBus.post(new ToggleNextClickHotkeyEvent(true));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_UNSTARTED, srtPlayer.getCurrentState());


        typeAltN();
        clickNextToSettingsStage();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtPlayer.getCurrentState());

        safeFocusStage(Windows.SETTINGS);
        refreshGui();
        typeAltN();
        clickNextToSettingsStage();
        Assert.assertEquals(RunningState.STATE_SUSPENDED, srtPlayer.getCurrentState());
    }

    @Test
    public void testNextClickCountsMovieMode() throws TimeoutException, InterruptedException {
        eventBus.post(new ToggleNextClickHotkeyEvent(true));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_UNSTARTED, srtPlayer.getCurrentState());
        settingsPage.switchToMovieMode();
        refreshGui();

        typeAltN();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point2D middleOfScreen = new Point2D(screenSize.getWidth()/2,screenSize.getHeight()/2);
        clickOn(middleOfScreen);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtPlayer.getCurrentState());
        refreshGui();
        focusStage(Windows.MOVIE);
        refreshGui();
        typeAltN();
        clickOn(middleOfScreen);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_SUSPENDED, srtPlayer.getCurrentState());
    }

    @Test
    public void testNavigateBackToSettingsModeViaAltEscape() throws TimeoutException, InterruptedException {
        settingsPage.switchToMovieMode();
        refreshGui();

        focusStage(Windows.MOVIE);
        refreshGui();

        typeAltEscape();
        refreshGui();

        Assert.assertTrue(isStageShowing(Windows.SETTINGS));
        Assert.assertFalse(isStageShowing(Windows.MOVIE));
    }


}
