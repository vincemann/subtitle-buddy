package gui;

import com.youneedsoftware.subtitleBuddy.srt.parser.SrtParser;
import com.youneedsoftware.subtitleBuddy.srt.stopWatch.RunningState;
import gui.pages.SettingsPage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;

public class ParserGuiTest extends GuiTest {

    private SrtParser applicationSrtParser;
    private SettingsPage settingsPage;



    @Override
    public void setUpClass() throws Exception {
        super.setUpClass();
        this.applicationSrtParser = getApplicationInjector().getInstance(SrtParser.class);
        this.settingsPage = new SettingsPage(this);
    }

    @Test
    public void testStartParser() {
        settingsPage.pressStart();
        Assert.assertEquals(RunningState.STATE_RUNNING,applicationSrtParser.getCurrentState());
    }

    @Test
    public void testStopParser(){
        settingsPage.pressStart();
        settingsPage.pressStop();
        Assert.assertEquals(RunningState.STATE_SUSPENDED,applicationSrtParser.getCurrentState());
    }

    @Test
    public void testSetTimeBeforeStart() {
        String timeStampInput = "00:12:13";
        settingsPage.enterTimeStamp(timeStampInput);
        WaitForAsyncUtils.waitForFxEvents();
        Assert.assertEquals(RunningState.STATE_SUSPENDED,applicationSrtParser.getCurrentState());

        Assert.assertEquals("But I don't get it."+System.lineSeparator(),settingsPage.findDisplayedSubtitleText());
        Assert.assertEquals(timeStampInput,settingsPage.findDisplayedTimeStamp());
    }


    @Test
    public void testSetTimeAfterStart() {
        settingsPage.pressStart();
        Assert.assertEquals(RunningState.STATE_RUNNING,applicationSrtParser.getCurrentState());

        String timeStampInput = "00:12:13";
        settingsPage.enterTimeStamp(timeStampInput);
        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertEquals(RunningState.STATE_SUSPENDED,applicationSrtParser.getCurrentState());

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
