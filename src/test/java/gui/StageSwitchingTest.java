package gui;

import com.youneedsoftware.subtitleBuddy.gui.srtDisplayer.MovieSrtDisplayer;
import com.youneedsoftware.subtitleBuddy.gui.srtDisplayer.SettingsSrtDisplayer;
import com.youneedsoftware.subtitleBuddy.gui.stages.MovieStage;
import gui.pages.MoviePage;
import gui.pages.SettingsPage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeoutException;

public class StageSwitchingTest extends StageTest {

    private SettingsPage settingsPage;

    @Override
    public void setUpClass() throws Exception {
        super.setUpClass();
        this.settingsPage = new SettingsPage(this);
    }

    @Test
    public void testSwitchToMovieMode() throws TimeoutException {
        settingsPage.switchToMovieMode();
        Assert.assertTrue(findSrtDisplayer(MovieSrtDisplayer.class).isDisplaying());
    }


    @Test
    public void testSwitchToMovieModeAndBack() throws TimeoutException {
        MoviePage moviePage = settingsPage.switchToMovieMode();
        refreshGui();
        focusStage(MovieStage.class);
        moviePage.switchToSettingsPage();
        refreshGui();
        Assert.assertTrue(findSrtDisplayer(SettingsSrtDisplayer.class).isDisplaying());
        Assert.assertFalse(findSrtDisplayer(MovieSrtDisplayer.class).isDisplaying());
    }

    @Test
    public void testSwitchToMovieModeWhenRunning() throws TimeoutException {
        settingsPage.pressStart();
        settingsPage.switchToMovieMode();
        Assert.assertTrue(findSrtDisplayer(MovieSrtDisplayer.class).isDisplaying());
        Assert.assertFalse(findSrtDisplayer(SettingsSrtDisplayer.class).isDisplaying());
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
