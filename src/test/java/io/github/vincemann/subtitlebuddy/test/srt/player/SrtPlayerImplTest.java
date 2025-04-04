package io.github.vincemann.subtitlebuddy.test.srt.player;

import io.github.vincemann.subtitlebuddy.srt.SrtOptions;
import io.github.vincemann.subtitlebuddy.srt.SubtitleParagraph;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import io.github.vincemann.subtitlebuddy.srt.Timestamp;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtFileParserImpl;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtPlayer;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtPlayerImpl;
import io.github.vincemann.subtitlebuddy.srt.parser.SubtitleTextParserImpl;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFile;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFileImpl;
import io.github.vincemann.subtitlebuddy.srt.srtfile.TimeStampOutOfBoundsException;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.RunningState;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.StopWatchImpl;
import io.github.vincemann.subtitlebuddy.test.SrtFiles;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class SrtPlayerImplTest {

    private SrtPlayer parser;

    @Before
    public void setUp() throws Exception {
        SubtitleFile subtitleFile = new SubtitleFileImpl(
                new SrtFileParserImpl(new SubtitleTextParserImpl()).parseFile(
                        new File(SrtFiles.VALID)));

        SrtOptions options = Mockito.mock(SrtOptions.class);
        when(options.getDefaultSubtitle())
                .thenReturn("###");
        when(options.isDefaultSubtitleVisible())
                .thenReturn(true);
        this.parser= new SrtPlayerImpl(subtitleFile,new StopWatchImpl(),options);
    }

    @Test
    public void testSetTime() throws TimeStampOutOfBoundsException {
        this.parser.setTime(new Timestamp(0,1,10,0));
        this.parser.updateCurrentSubtitle();
        SubtitleText currentSubtitle = this.parser.getCurrentSubtitleText();
        Assert.assertNotNull(currentSubtitle);
        Assert.assertEquals("Juice by Mary. Juice by Mary.",currentSubtitle.getSubtitles().get(0).getText());
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
            Optional<SubtitleParagraph> subtitle = this.parser.getCurrentSubtitle();
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