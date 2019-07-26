package gui;

import com.google.common.eventbus.EventBus;
import com.youneedsoftware.subtitleBuddy.events.ToggleHotKeyEvent;
import com.youneedsoftware.subtitleBuddy.gui.stages.MovieStage;
import com.youneedsoftware.subtitleBuddy.gui.stages.SettingsStage;
import com.youneedsoftware.subtitleBuddy.inputListeners.keyListener.HotKey;
import com.youneedsoftware.subtitleBuddy.srt.parser.SrtParser;
import com.youneedsoftware.subtitleBuddy.srt.stopWatch.RunningState;
import gui.pages.SettingsPage;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeoutException;

public class ParserHotKeyTest extends StageTest {


    private SrtParser applicationSrtParser;
    private SettingsPage settingsPage;
    private EventBus applicationEventBus;


    @Override
    public void setUpClass() throws Exception {
        super.setUpClass();
        this.applicationSrtParser = getApplicationInjector().getInstance(SrtParser.class);
        this.settingsPage=new SettingsPage(this);
        this.applicationEventBus = getApplicationInjector().getInstance(EventBus.class);
    }

    @Test
    public void testStartParserBySpace(){
        applicationEventBus.post(new ToggleHotKeyEvent(HotKey.START_STOP,false));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_UNSTARTED,applicationSrtParser.getCurrentState());
        type(KeyCode.SPACE);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING,applicationSrtParser.getCurrentState());
    }

    @Test
    public void testStartStopParserBySpace(){
        applicationEventBus.post(new ToggleHotKeyEvent(HotKey.START_STOP,false));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_UNSTARTED,applicationSrtParser.getCurrentState());

        type(KeyCode.SPACE);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING,applicationSrtParser.getCurrentState());

        type(KeyCode.SPACE);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_SUSPENDED,applicationSrtParser.getCurrentState());


        type(KeyCode.SPACE);
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING,applicationSrtParser.getCurrentState());
    }

    @Test
    public void testNextClickCountsSettingsMode() throws TimeoutException {
        applicationEventBus.post(new ToggleHotKeyEvent(HotKey.NEXT_CLICK,false));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_UNSTARTED,applicationSrtParser.getCurrentState());

        //todo change sodass screen mitte genommen wird oder so
        press(KeyCode.ALT).type(KeyCode.N).release(KeyCode.ALT);
        clickOn(new Point2D(700,100));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING,applicationSrtParser.getCurrentState());

        focusStage(SettingsStage.class);
        refreshGui();
        press(KeyCode.ALT).type(KeyCode.N).release(KeyCode.ALT);
        clickOn(new Point2D(900,100));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_SUSPENDED,applicationSrtParser.getCurrentState());
    }

    @Test
    public void testNextClickCountsMovieMode() throws TimeoutException {
        applicationEventBus.post(new ToggleHotKeyEvent(HotKey.NEXT_CLICK,false));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_UNSTARTED,applicationSrtParser.getCurrentState());
        settingsPage.switchToMovieMode();
        refreshGui();

        press(KeyCode.ALT).type(KeyCode.N).release(KeyCode.ALT);
        //todo change das er screen /2 nimmt oder so
        clickOn(new Point2D(700,100));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_RUNNING,applicationSrtParser.getCurrentState());
        refreshGui();
        focusStage(MovieStage.class);
        refreshGui();
        press(KeyCode.ALT).type(KeyCode.N).release(KeyCode.ALT);
        clickOn(new Point2D(900,100));
        refreshGui();
        Assert.assertEquals(RunningState.STATE_SUSPENDED,applicationSrtParser.getCurrentState());
    }


}
