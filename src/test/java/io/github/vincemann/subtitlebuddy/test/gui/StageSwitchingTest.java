package io.github.vincemann.subtitlebuddy.test.gui;

import io.github.vincemann.subtitlebuddy.gui.Stages;
import io.github.vincemann.subtitlebuddy.test.gui.pages.MoviePage;
import io.github.vincemann.subtitlebuddy.test.gui.pages.SettingsPage;
import io.github.vincemann.subtitlebuddy.gui.stages.MovieStageController;
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
        Assert.assertTrue(isStageShowing(Stages.MOVIE));
    }


    @Test
    public void testSwitchToMovieModeAndBack() throws TimeoutException {
        MoviePage moviePage = settingsPage.switchToMovieMode();
        refreshGui();
        focusStage(Stages.MOVIE);
        moviePage.switchToSettingsPage();
        refreshGui();
        Assert.assertTrue(isStageShowing(Stages.SETTINGS));
        Assert.assertFalse(isStageShowing(Stages.MOVIE));
    }


    @Test
    public void testSwitchToMovieModeWhenRunning() throws TimeoutException {
        settingsPage.pressStart();
        settingsPage.switchToMovieMode();
        Assert.assertTrue(isStageShowing(Stages.MOVIE));
        Assert.assertFalse(isStageShowing(Stages.SETTINGS));
    }

    @Test
    public void testTextShowingWhenSwitchingToMovieMode() throws TimeoutException {
        settingsPage.pressStart();
        settingsPage.enterTimeStamp("00:12:13");
        WaitForAsyncUtils.waitForFxEvents();
        String settingsText = settingsPage.findDisplayedSubtitleText();
        Assert.assertEquals("But I don't get it." + System.lineSeparator(), settingsText);
        MoviePage moviePage = settingsPage.switchToMovieMode();
        WaitForAsyncUtils.waitForFxEvents();
        String movieText = moviePage.findDisplayedSubtitleText();
        Assert.assertEquals(movieText, settingsText);
    }
}
