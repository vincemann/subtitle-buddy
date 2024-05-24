package io.github.vincemann.subtitlebuddy.test.gui;

import io.github.vincemann.subtitlebuddy.test.gui.pages.SettingsPage;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtPlayer;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.RunningState;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

public class ParserGuiTest extends GuiTest {

    private SrtPlayer applicationSrtPlayer;
    private SettingsPage settingsPage;

    @Override
    public void beforeEach() throws Exception {
        super.beforeEach();
        this.applicationSrtPlayer = getInstance(SrtPlayer.class);
        this.settingsPage = new SettingsPage(this);
    }

    @Test
    public void testStartParser() {
        settingsPage.pressStart();
        Assert.assertEquals(RunningState.STATE_RUNNING, applicationSrtPlayer.getCurrentState());
    }

    @Test
    public void testStopParser(){
        settingsPage.pressStart();
        settingsPage.pressStop();
        Assert.assertEquals(RunningState.STATE_SUSPENDED, applicationSrtPlayer.getCurrentState());
    }

    @Test
    public void testSetTimeBeforeStart() {
        String timeStampInput = "00:12:13";
        settingsPage.enterTimeStamp(timeStampInput);
        WaitForAsyncUtils.waitForFxEvents();
        Assert.assertEquals(RunningState.STATE_SUSPENDED, applicationSrtPlayer.getCurrentState());

        Assert.assertEquals("But I don't get it."+System.lineSeparator(),settingsPage.findDisplayedSubtitleText());
        Assert.assertEquals(timeStampInput,settingsPage.findDisplayedTimeStamp());
    }


    @Test
    public void testSetTimeAfterStart() {
        settingsPage.pressStart();
        Assert.assertEquals(RunningState.STATE_RUNNING, applicationSrtPlayer.getCurrentState());

        String timeStampInput = "00:12:13";
        settingsPage.enterTimeStamp(timeStampInput);
        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertEquals(RunningState.STATE_SUSPENDED, applicationSrtPlayer.getCurrentState());

        Assert.assertEquals("But I don't get it."+System.lineSeparator(),settingsPage.findDisplayedSubtitleText());
        Assert.assertEquals(timeStampInput,settingsPage.findDisplayedTimeStamp());
    }

    @Test
    public void testWrongTimeStampEntered() {
        String timeStampInput = "00:12:13nmfalsdlm";
        settingsPage.enterTimeStamp(timeStampInput);
        WaitForAsyncUtils.waitForFxEvents();
        Assert.assertTrue(settingsPage.isTimeStampWarningShowing());
    }

}
