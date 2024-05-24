package io.github.vincemann.subtitlebuddy.test.gui;

import io.github.vincemann.subtitlebuddy.gui.Windows;
import io.github.vincemann.subtitlebuddy.test.gui.pages.MoviePage;
import io.github.vincemann.subtitlebuddy.test.gui.pages.SettingsPage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeoutException;

public class StageSwitchingTest extends GuiTest {

    private SettingsPage settingsPage;

    @Override
    public void beforeEach() throws Exception {
        super.beforeEach();
        this.settingsPage = new SettingsPage(this);
    }

    @Test
    public void testSwitchToMovieMode() throws TimeoutException {
        settingsPage.switchToMovieMode();
        WaitForAsyncUtils.waitForFxEvents();
        Assert.assertTrue(isStageShowing(Windows.MOVIE));
    }


    @Test
    public void testSwitchToMovieModeAndBack() throws TimeoutException {
        MoviePage moviePage = settingsPage.switchToMovieMode();
        refreshGui();
        focusStage(Windows.MOVIE);
        moviePage.switchToSettingsPage();
        refreshGui();
        Assert.assertTrue(isStageShowing(Windows.SETTINGS));
        Assert.assertFalse(isStageShowing(Windows.MOVIE));
    }


    @Test
    public void testSwitchToMovieModeWhenRunning() throws TimeoutException {
        settingsPage.pressStart();
        settingsPage.switchToMovieMode();
        Assert.assertTrue(isStageShowing(Windows.MOVIE));
        Assert.assertFalse(isStageShowing(Windows.SETTINGS));
    }

    @Test
    public void testTextShowingWhenSwitchingToMovieMode() throws TimeoutException {
        settingsPage.pressStart();
        settingsPage.enterTimeStamp("00:12:13");
        WaitForAsyncUtils.waitForFxEvents();
        sleep(400); // wait until event is handled and sub updated
        String settingsText = settingsPage.findDisplayedSubtitleText();
        Assert.assertEquals("But I don't get it." + System.lineSeparator(), settingsText);
        MoviePage moviePage = settingsPage.switchToMovieMode();
        WaitForAsyncUtils.waitForFxEvents();
        String movieText = moviePage.findDisplayedSubtitleText();
        Assert.assertEquals(movieText, settingsText);
    }
}
