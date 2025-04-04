package io.github.vincemann.subtitlebuddy.test.gui;


import io.github.vincemann.subtitlebuddy.events.EventBus;
import io.github.vincemann.subtitlebuddy.events.ToggleEndMovieModeHotkeyEvent;
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
        waitForGuiEvents();
        Assert.assertEquals(RunningState.STATE_UNSTARTED, srtPlayer.getCurrentState());
        type(KeyCode.SPACE);
        waitForGuiEvents();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtPlayer.getCurrentState());
    }

    @Test
    public void testStartStopParserBySpace() throws InterruptedException {
        eventBus.post(new ToggleSpaceHotkeyEvent(true));
        waitForGuiEvents();
        Assert.assertEquals(RunningState.STATE_UNSTARTED, srtPlayer.getCurrentState());

        type(KeyCode.SPACE);
        waitForGuiEvents();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtPlayer.getCurrentState());

        type(KeyCode.SPACE);
        waitForGuiEvents();
        Assert.assertEquals(RunningState.STATE_SUSPENDED, srtPlayer.getCurrentState());


        type(KeyCode.SPACE);
        waitForGuiEvents();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtPlayer.getCurrentState());
    }

    @Test
    public void testNextClickCountsSettingsMode() throws TimeoutException, InterruptedException {
        eventBus.post(new ToggleNextClickHotkeyEvent(true));
        waitForGuiEvents();
        Assert.assertEquals(RunningState.STATE_UNSTARTED, srtPlayer.getCurrentState());


        typeAltN();
        clickNextToSettingsStage();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtPlayer.getCurrentState());

        safeFocusStage(Windows.SETTINGS);
        waitForGuiEvents();
        typeAltN();
        clickNextToSettingsStage();
        Assert.assertEquals(RunningState.STATE_SUSPENDED, srtPlayer.getCurrentState());
    }

    @Test
    public void testNextClickCountsMovieMode() throws TimeoutException, InterruptedException {
        eventBus.post(new ToggleNextClickHotkeyEvent(true));
        waitForGuiEvents();
        Assert.assertEquals(RunningState.STATE_UNSTARTED, srtPlayer.getCurrentState());
        settingsPage.switchToMovieMode();
        waitForGuiEvents();

        typeAltN();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point2D middleOfScreen = new Point2D(screenSize.getWidth()/2,screenSize.getHeight()/2);
        clickOn(middleOfScreen);
        waitForGuiEvents();
        Assert.assertEquals(RunningState.STATE_RUNNING, srtPlayer.getCurrentState());
        waitForGuiEvents();
        typeAltN();
        clickOn(middleOfScreen);
        waitForGuiEvents();
        Assert.assertEquals(RunningState.STATE_SUSPENDED, srtPlayer.getCurrentState());
    }

    @Test
    public void testNavigateBackToSettingsModeViaEscape() throws TimeoutException, InterruptedException {
        eventBus.post(new ToggleEndMovieModeHotkeyEvent(true));
        settingsPage.switchToMovieMode();
        waitForGuiEvents();

        focusStage(Windows.MOVIE);
        waitForGuiEvents();

        typeEscape();
        waitForGuiEvents();

        Assert.assertTrue(isStageShowing(Windows.SETTINGS));
        Assert.assertFalse(isStageShowing(Windows.MOVIE));
    }


}
