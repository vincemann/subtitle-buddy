package gui;

import com.youneedsoftware.subtitleBuddy.gui.srtDisplayer.MovieSrtDisplayer;
import com.youneedsoftware.subtitleBuddy.gui.srtDisplayer.SettingsSrtDisplayer;
import com.youneedsoftware.subtitleBuddy.gui.stages.OptionsStage;
import com.youneedsoftware.subtitleBuddy.gui.stages.SettingsStage;
import com.youneedsoftware.subtitleBuddy.gui.stages.stageController.AbstractStageController;
import com.youneedsoftware.subtitleBuddy.srt.SrtFont;
import com.youneedsoftware.subtitleBuddy.srt.font.fontManager.SrtFontManager;
import com.youneedsoftware.subtitleBuddy.util.vec2d.Vector2D;
import gui.pages.OptionsPage;
import gui.pages.SettingsPage;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeoutException;


public class OptionsTest extends StageTest {


    private static final Vector2D OPTIONS_WINDOW_START_POS = new Vector2D(1000, 1000);

    private SettingsPage settingsPage;
    private SrtFontManager srtFontManager;

    @Override
    public void setUpClass() throws Exception {
        super.setUpClass();
        this.settingsPage = new SettingsPage(this);
        this.srtFontManager= getApplicationInjector().getInstance(SrtFontManager.class);
    }


    @Test
    public void testNoColorChangeOnSettingsText() throws TimeoutException {
        focusStage(SettingsStage.class);
        OptionsPage optionsPage = settingsPage.openOptionsWindow(findStageController(OptionsStage.class).getStage(), OPTIONS_WINDOW_START_POS);
        refreshGui();
        focusStage(OptionsStage.class);
        Color selectedColor = optionsPage.selectRandomColor(SettingsSrtDisplayer.DEFAULT_FONT_COLOR);
        Assert.assertNotEquals(SettingsSrtDisplayer.DEFAULT_FONT_COLOR, selectedColor);
        refreshGui();
        //es darf keinen change gegeben haben
        Assert.assertEquals(SettingsSrtDisplayer.DEFAULT_FONT_COLOR, findSrtDisplayer(SettingsSrtDisplayer.class).getFontColor());
    }


    @Test
    public void testShowOptions() throws TimeoutException {
        settingsPage.openOptionsWindow(findStageController(OptionsStage.class).getStage(), OPTIONS_WINDOW_START_POS);
        refreshGui();
        Assert.assertTrue(isStageShowing(OptionsStage.class));
        Assert.assertTrue(isStageShowing(SettingsStage.class));
        AbstractStageController optionsStageController = findStageController(OptionsStage.class);
        Assert.assertTrue(optionsStageController.getStage().isFocused());
    }

    @Test
    public void testChangeColorOnMovieMode() throws TimeoutException {
        focusStage(SettingsStage.class);
        OptionsPage optionsPage = settingsPage.openOptionsWindow(findStageController(OptionsStage.class).getStage(), OPTIONS_WINDOW_START_POS);
        focusStage(SettingsStage.class);
        settingsPage.switchToMovieMode();
        findSrtDisplayer(MovieSrtDisplayer.class).setFontColor(Color.RED);
        refreshGui();
        focusStage(OptionsStage.class);
        Color selectedColor = optionsPage.selectRandomColor(Color.RED);
        Assert.assertNotEquals(Color.RED, selectedColor);
        refreshGui();
        Assert.assertEquals(selectedColor, findSrtDisplayer(MovieSrtDisplayer.class).getFontColor());
    }

    @Test
    public void testChangeFontInSettingsMode() throws TimeoutException {
        focusStage(SettingsStage.class);
        OptionsPage optionsPage = settingsPage.openOptionsWindow(findStageController(OptionsStage.class).getStage(), OPTIONS_WINDOW_START_POS);
        focusStage(OptionsStage.class);
        SrtFont firstFont = srtFontManager.loadDefaultFont();
        findSrtDisplayer(SettingsSrtDisplayer.class).setCurrentFont(firstFont);
        SrtFont currFont = findSrtDisplayer(SettingsSrtDisplayer.class).getCurrentFont();
        Assert.assertEquals(currFont, firstFont);
        SrtFont newFont = optionsPage.selectNewFont(currFont);
        Assert.assertNotEquals(newFont, currFont);
        refreshGui();
        Assert.assertEquals(findSrtDisplayer(SettingsSrtDisplayer.class).getCurrentFont(), newFont);
    }

    @Test
    public void testChangeFontInMovieMode() throws TimeoutException {
        focusStage(SettingsStage.class);
        OptionsPage optionsPage = settingsPage.openOptionsWindow(findStageController(OptionsStage.class).getStage(), OPTIONS_WINDOW_START_POS);
        focusStage(OptionsStage.class);
        SrtFont firstFont = srtFontManager.loadDefaultFont();
        focusStage(SettingsStage.class);
        settingsPage.switchToMovieMode();
        findSrtDisplayer(MovieSrtDisplayer.class).setCurrentFont(firstFont);
        SrtFont currFont = findSrtDisplayer(MovieSrtDisplayer.class).getCurrentFont();
        Assert.assertEquals(currFont, firstFont);
        focusStage(OptionsStage.class);
        SrtFont newFont = optionsPage.selectNewFont(currFont);
        Assert.assertNotEquals(newFont, currFont);
        refreshGui();
        Assert.assertEquals(findSrtDisplayer(MovieSrtDisplayer.class).getCurrentFont(), newFont);
    }
}
