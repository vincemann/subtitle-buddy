package srtParser;

import com.youneedsoftware.subtitleBuddy.srt.subtitleFile.TimeStampOutOfBoundsException;
import com.youneedsoftware.subtitleBuddy.srt.Subtitle;
import com.youneedsoftware.subtitleBuddy.srt.SubtitleText;
import com.youneedsoftware.subtitleBuddy.srt.Timestamp;
import com.youneedsoftware.subtitleBuddy.srt.parser.SrtParser;
import com.youneedsoftware.subtitleBuddy.srt.parser.SrtParserImpl;
import com.youneedsoftware.subtitleBuddy.srt.stopWatch.RunningState;
import com.youneedsoftware.subtitleBuddy.srt.stopWatch.StopWatchImpl;
import com.youneedsoftware.subtitleBuddy.srt.subtitleFile.SubtitleFile;
import com.youneedsoftware.subtitleBuddy.srt.subtitleFile.SubtitleFileImpl;
import com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformerImpl;
import constants.TestConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Optional;

public class SrtParserImplTest {

    private SrtParser parser;

    @Before
    public void setUp() throws Exception {
        SubtitleFile subtitleFile = new SubtitleFileImpl(
                new SrtFileTransformerImpl().transformFileToSubtitles(
                        new File(TestConstants.VALID_SRT_FILE_PATH)));

        this.parser= new SrtParserImpl(subtitleFile,new StopWatchImpl(),"###");
    }

    @Test
    public void testSetTime() throws TimeStampOutOfBoundsException {
        this.parser.setTime(new Timestamp(0,1,10,0));
        this.parser.updateCurrentSubtitle();
        SubtitleText currentSubtitle = this.parser.getCurrentSubtitleText();
        Assert.assertNotNull(currentSubtitle);
        Assert.assertEquals("Juice by Mary. Juice by Mary.",currentSubtitle.getSubtitleSegments().get(0).get(0).getText());
    }

    @Test(expected = IllegalStateException.class)
    public void testMultipleStarting(){
        try {
            this.parser.start();
        }catch (IllegalStateException e){
            Assert.fail();
        }

        this.parser.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testMultipleStopping(){
        try {
            this.parser.start();
            this.parser.stop();
        }catch (IllegalStateException e){
            Assert.fail();
        }
        this.parser.stop();
    }

    @Test
    public void testPauseResuming(){
        for(int i = 0;i<10;i++){
            this.parser.start();
            Assert.assertEquals(RunningState.STATE_RUNNING,this.parser.getCurrentState());
            this.parser.stop();
            Assert.assertEquals(RunningState.STATE_SUSPENDED,this.parser.getCurrentState());
        }
    }

    @Test
    public void testStartParser() {
        this.parser.start();
        Assert.assertEquals(RunningState.STATE_RUNNING,this.parser.getCurrentState());
    }

    @Test
    public void testPauseParser(){
        this.parser.start();
        this.parser.stop();
        Assert.assertEquals(RunningState.STATE_SUSPENDED,this.parser.getCurrentState());
    }

    @Test
    public void testStartStartPause(){
        this.parser.start();
        for (int i = 0; i<10;i++) {
            try {
                this.parser.start();
                Assert.fail();
            } catch (IllegalStateException e) {
            }
        }
        Assert.assertEquals(RunningState.STATE_RUNNING,this.parser.getCurrentState());
        this.parser.stop();
        Assert.assertEquals(RunningState.STATE_SUSPENDED,this.parser.getCurrentState());
    }


    @Test
    public void testStartPausePause(){
        this.parser.start();
        this.parser.stop();
        for (int i = 0; i<10;i++) {
            try {
                this.parser.stop();
                Assert.fail();
            } catch (IllegalStateException e) {
            }
        }
        Assert.assertEquals(RunningState.STATE_SUSPENDED,this.parser.getCurrentState());
        this.parser.start();
        Assert.assertEquals(RunningState.STATE_RUNNING,this.parser.getCurrentState());
    }


    @Test
    public void testSettingRandomTime() throws TimeStampOutOfBoundsException {
        this.parser.start();
        this.parser.stop();

        for(int i = 0 ; i<50;i++) {
            Timestamp randomTimeStamp = new Timestamp((int) (Math.random() * 1), (int) (Math.random() * 60), (int) (Math.random() * 60), (int) (Math.random() * 1000));
            if (randomTimeStamp.getHours() == 1) {
                randomTimeStamp.setMinutes((int) (Math.random() * 36));
            }
            this.parser.setTime(randomTimeStamp);
            this.parser.updateCurrentSubtitle();
            Optional<Subtitle> subtitle = this.parser.getCurrentSubtitle();
            if(subtitle.isPresent()) {
                Assert.assertTrue(subtitle.get().getStartTime().compareTo(randomTimeStamp) <= 0);
                Assert.assertTrue(subtitle.get().getEndTime().compareTo(randomTimeStamp) >= 0);
            }
        }
    }

    @Test(expected = TimeStampOutOfBoundsException.class)
    public void testSettingInvalidTime() throws TimeStampOutOfBoundsException {
        this.parser.start();
        this.parser.stop();

        Timestamp invalidTimeStamp = new Timestamp(1,40,0,0);
        this.parser.setTime(invalidTimeStamp);
        this.parser.updateCurrentSubtitle();
    }



}