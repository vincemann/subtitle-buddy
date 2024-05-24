package io.github.vincemann.subtitlebuddy.test.gui;

import com.google.common.eventbus.EventBus;
import io.github.vincemann.subtitlebuddy.events.UpdateFontColorEvent;
import io.github.vincemann.subtitlebuddy.font.FontManager;
import io.github.vincemann.subtitlebuddy.font.FontOptions;
import io.github.vincemann.subtitlebuddy.gui.Windows;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.test.gui.pages.OptionsPage;
import io.github.vincemann.subtitlebuddy.test.gui.pages.SettingsPage;
import javafx.scene.paint.Color;
import org.junit.Assert;
import org.junit.Assume;
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
        System.err.println("settings stage focused");
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        refreshGui();
        focusStage(Windows.OPTIONS);
        Color selectedColor = optionsPage.selectRandomColorThatIsNot(Color.WHITE);
        Assert.assertNotEquals(Color.WHITE, selectedColor);
        refreshGui();
        Assert.assertEquals(selectedColor, fontOptions.getFontColor());
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

        setFontColor(Color.WHITE);
        focusStage(Windows.SETTINGS);
        OptionsPage optionsPage = settingsPage.openOptionsWindow();
        focusStage(Windows.SETTINGS);
        settingsPage.switchToMovieMode();


        refreshGui();
        focusStage(Windows.OPTIONS);
        Color selectedColor = optionsPage.selectRandomColorThatIsNot(Color.WHITE);
        Assert.assertNotEquals(Color.WHITE, selectedColor);
        refreshGui();
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
        refreshGui();
        Assert.assertEquals(fontManager.getCurrentFont(), newFont);
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
        FontBundle currFont = fontManager.getCurrentFont();
        focusStage(Windows.SETTINGS);
        settingsPage.switchToMovieMode();
        focusStage(Windows.OPTIONS);
        FontBundle newFont = optionsPage.selectNewFont(currFont);
        Assert.assertNotEquals(newFont, currFont);
        refreshGui();
        Assert.assertEquals(fontManager.getCurrentFont(), newFont);
    }
}
