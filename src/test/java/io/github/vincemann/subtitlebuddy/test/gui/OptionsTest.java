package io.github.vincemann.subtitlebuddy.test.gui;

import io.github.vincemann.subtitlebuddy.events.EventBus;
import io.github.vincemann.subtitlebuddy.events.UpdateFontColorEvent;
import io.github.vincemann.subtitlebuddy.font.FontManager;
import io.github.vincemann.subtitlebuddy.font.FontOptions;
import io.github.vincemann.subtitlebuddy.gui.Windows;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.test.gui.pages.OptionsPage;
import io.github.vincemann.subtitlebuddy.test.gui.pages.SettingsPage;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeoutException;


public class OptionsTest extends GuiTest {

    private SettingsPage settingsPage;
    private FontManager fontManager;
    private EventBus eventBus;
    private FontOptions fontOptions;

    @Override
    public void beforeEach() throws Exception {
        super.beforeEach();
        this.settingsPage = new SettingsPage(this);
        this.fontManager = getInstance(FontManager.class);
        this.fontOptions = getInstance(FontOptions.class);
        this.eventBus = getInstance(EventBus.class);
    }

    private void setFontColor(Color color) {
        // color is already set
        if (fontOptions.getFontColor().equals(color))
            return;
        eventBus.post(new UpdateFontColorEvent(color));
        // wait until color changed
        while (true){
            try {
                Thread.sleep(50);
                if (fontOptions.getFontColor().equals(color)){
                    break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void testChangeColorOnSettingsMode() throws TimeoutException {
        setFontColor(Color.WHITE);
        focusStage(Windows.SETTINGS);
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        waitForGuiEvents();
        focusStage(Windows.OPTIONS);
        Color selectedColor = optionsPage.selectRandomColorThatIsNot(Color.WHITE);
        Assert.assertNotEquals(Color.WHITE, selectedColor);
        waitForGuiEvents();
        Assert.assertEquals(selectedColor, fontOptions.getFontColor());
    }


    @Test
    public void testShowOptions() throws TimeoutException {
        settingsPage.openOptionsWindow();
        waitForGuiEvents();
        Assert.assertTrue(isStageShowing(Windows.OPTIONS));
        Assert.assertTrue(isStageShowing(Windows.SETTINGS));
        Assert.assertTrue(isStageShowing(Windows.OPTIONS));
    }

    @Test
    public void testChangeColorOnMovieMode() throws TimeoutException {
        setFontColor(Color.WHITE);
        focusStage(Windows.SETTINGS);
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        focusStage(Windows.SETTINGS);
        settingsPage.switchToMovieMode();


        waitForGuiEvents();
        focusStage(Windows.OPTIONS);
        Color selectedColor = optionsPage.selectRandomColorThatIsNot(Color.WHITE);
        Assert.assertNotEquals(Color.WHITE, selectedColor);
        waitForGuiEvents();
        Assert.assertEquals(selectedColor, fontOptions.getFontColor());
    }

    @Test
    public void testChangeFontInSettingsMode() throws TimeoutException {
        focusStage(Windows.SETTINGS);
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        focusStage(Windows.OPTIONS);
        FontBundle currFont = fontManager.getCurrentFont();
        FontBundle newFont = optionsPage.selectNewFont(currFont);
        Assert.assertNotEquals(newFont, currFont);
        waitForGuiEvents();
        Assert.assertEquals(fontManager.getCurrentFont(), newFont);
    }

    @Test
    public void testChangeFontInMovieMode() throws TimeoutException {
        focusStage(Windows.SETTINGS);
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        focusStage(Windows.OPTIONS);
        FontBundle currFont = fontManager.getCurrentFont();
        focusStage(Windows.SETTINGS);
        settingsPage.switchToMovieMode();
        focusStage(Windows.OPTIONS);
        FontBundle newFont = optionsPage.selectNewFont(currFont);
        Assert.assertNotEquals(newFont, currFont);
        waitForGuiEvents();
        Assert.assertEquals(fontManager.getCurrentFont(), newFont);
    }
}
