package io.github.vincemann.subtitlebuddy.test.gui;

import io.github.vincemann.subtitlebuddy.gui.Windows;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.srt.font.SrtFontManager;
import io.github.vincemann.subtitlebuddy.test.gui.pages.OptionsPage;
import io.github.vincemann.subtitlebuddy.test.gui.pages.SettingsPage;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import java.util.concurrent.TimeoutException;


public class OptionsTest extends GuiTest {



    private SettingsPage settingsPage;
    private SrtFontManager srtFontManager;

    @Override
    public void beforeEach() throws Exception {
        super.beforeEach();
        this.settingsPage = new SettingsPage(this);
        this.srtFontManager= getInstance(SrtFontManager.class);
    }


    @Test
    public void testNoColorChangeOnSettingsText() throws TimeoutException {
        focusStage(Windows.SETTINGS);
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        refreshGui();
        focusStage(Windows.OPTIONS);
        Color selectedColor = optionsPage.selectRandomColor(SettingsSrtDisplayer.DEFAULT_FONT_COLOR);
        Assert.assertNotEquals(SettingsSrtDisplayer.DEFAULT_FONT_COLOR, selectedColor);
        refreshGui();
        //es darf keinen change gegeben haben
        Assert.assertEquals(SettingsSrtDisplayer.DEFAULT_FONT_COLOR, findSrtDisplayer(SettingsSrtDisplayer.class).getFontColor());
    }


    @Test
    public void testShowOptions() throws TimeoutException {
        settingsPage.openOptionsWindow();
        refreshGui();
        Assert.assertTrue(isStageShowing(Windows.OPTIONS));
        Assert.assertTrue(isStageShowing(Windows.SETTINGS));
        Assert.assertTrue(isStageShowing(Windows.OPTIONS));
    }

    @Test
    public void testChangeColorOnMovieMode() throws TimeoutException {
        // Assume that the OS is not Mac OS X
        // this test wont work on osx, because when in movie mode it cannot focus back on options
        String osName = System.getProperty("os.name").toLowerCase();
        Assume.assumeFalse(osName.contains("mac"));

        focusStage(Windows.SETTINGS);
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        focusStage(Windows.SETTINGS);
        settingsPage.switchToMovieMode();
        findSrtDisplayer(MovieSrtDisplayer.class).setFontColor(Color.WHITE);
        refreshGui();
        focusStage(Windows.OPTIONS);
        Color selectedColor = optionsPage.selectRandomColor(Color.WHITE);
        Assert.assertNotEquals(Color.WHITE, selectedColor);
        refreshGui();
        Assert.assertEquals(selectedColor, findSrtDisplayer(MovieSrtDisplayer.class).getFontColor());
    }

    @Test
    public void testChangeFontInSettingsMode() throws TimeoutException {
        focusStage(Windows.SETTINGS);
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        focusStage(Windows.OPTIONS);
        FontBundle firstFont = srtFontManager.loadDefaultFont();
        findSrtDisplayer(SettingsSrtDisplayer.class).setCurrentFont(firstFont);
        FontBundle currFont = findSrtDisplayer(SettingsSrtDisplayer.class).getCurrentFont();
        Assert.assertEquals(currFont, firstFont);
        FontBundle newFont = optionsPage.selectNewFont(currFont);
        Assert.assertNotEquals(newFont, currFont);
        refreshGui();
        Assert.assertEquals(findSrtDisplayer(SettingsSrtDisplayer.class).getCurrentFont(), newFont);
    }

    @Test
    public void testChangeFontInMovieMode() throws TimeoutException {
        // Assume that the OS is not Mac OS X
        // this test wont work on osx, because when in movie mode it cannot focus back on options
        String osName = System.getProperty("os.name").toLowerCase();
        Assume.assumeFalse(osName.contains("mac"));

        focusStage(Windows.SETTINGS);
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        focusStage(Windows.OPTIONS);
        FontBundle firstFont = srtFontManager.loadDefaultFont();
        focusStage(Windows.SETTINGS);
        settingsPage.switchToMovieMode();
        findSrtDisplayer(MovieSrtDisplayer.class).setCurrentFont(firstFont);
        FontBundle currFont = findSrtDisplayer(MovieSrtDisplayer.class).getCurrentFont();
        Assert.assertEquals(currFont, firstFont);
        focusStage(Windows.OPTIONS);
        FontBundle newFont = optionsPage.selectNewFont(currFont);
        Assert.assertNotEquals(newFont, currFont);
        refreshGui();
        Assert.assertEquals(findSrtDisplayer(MovieSrtDisplayer.class).getCurrentFont(), newFont);
    }
}
