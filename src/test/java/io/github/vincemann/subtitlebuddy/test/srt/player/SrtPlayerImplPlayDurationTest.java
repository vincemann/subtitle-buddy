package io.github.vincemann.subtitlebuddy.test.srt.player;

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
import io.github.vincemann.subtitlebuddy.test.LongTest;
import io.github.vincemann.subtitlebuddy.test.TestFiles;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;

/**
 * Component test.
 * Very slow.
 */
public class SrtPlayerImplPlayDurationTest {

    private SrtPlayer player;

    @Before
    public void setUp() throws Exception {
        SubtitleFile subtitleFile = new SubtitleFileImpl(
                new SrtFileParserImpl(new SubtitleTextParserImpl()).parseFile(
                        new File(TestFiles.VALID_SRT_FILE_PATH)));
        this.player = new SrtPlayerImpl(subtitleFile,new StopWatchImpl(),"###");
    }

    @Test
    public void testPlayUntilNewSub() throws TimeStampOutOfBoundsException, InterruptedException {
        // given
        setTime(new Timestamp(1,10,3,0));
        assertSubtitleIs("I'm sure he'll be here soon.");

        // when
        player.start();
        waitFor(1800);

        // then
        assertSubtitleIs("It's almost that time. Are you ready?");
    }

    @Test
    public void testPlayUntil2NewSubs() throws TimeStampOutOfBoundsException, InterruptedException {
        // given
        setTime(new Timestamp(1,9,56,80));
        assertSubtitleIs("I hope you're in love.");

        // when
        player.start();
        waitFor(2000);

        // then
        assertSubtitleIs("Please come and see me.");
        waitFor(2500);
        assertSubtitleIs("And bring Marion, won't you?");
    }

    @Test
    public void testPlayUntilNNewSubs() throws TimeStampOutOfBoundsException, InterruptedException {
        // given
        setTime(new Timestamp(1,20,48,0));
        assertSubtitleIs("Oh, I know it's pretty, baby.");

        // when
        player.start();
        waitFor(13700);

        // then
        assertSubtitleIs("Yeah.");
    }

    @Test
    public void testSwitchBeforeAndBack() throws TimeStampOutOfBoundsException, InterruptedException {
        setTime(new Timestamp(1,5,29,100));
        assertSubtitleIs("Man, we got nothing to lose.");
        player.start();
        waitFor(1500);
        player.stop();
        assertSubtitleIs("It's wide open,");
        setTime(new Timestamp(1,5,29,100));
        assertSubtitleIs("Man, we got nothing to lose.");
        player.start();
        waitFor(1500);
        player.stop();
        assertSubtitleIs("It's wide open,");
    }

    @Test
    public void testStartStopWithinSubtitle() throws TimeStampOutOfBoundsException, InterruptedException {
        /*
        stop at subtitle
        play for x millis
        still same
        stop
        still same
        play for x millis
        other subtitle
         */
        // given
        setTime(new Timestamp(1,10,4,750));
        assertSubtitleIs("It's almost that time. Are you ready?");
        player.start();
        waitFor(1100);
        assertSubtitleIs("It's almost that time. Are you ready?");
        player.stop();
        assertSubtitleIs("It's almost that time. Are you ready?");

        player.start();
        waitFor(2100);
        assertSubtitleIs("I'm ready, Tappy.");

    }

    private void assertSubtitleIs(String expectedSub) throws TimeStampOutOfBoundsException {
        this.player.updateCurrentSubtitle();
        SubtitleText currentSubtitle = this.player.getCurrentSubtitleText();
        Assert.assertNotNull(currentSubtitle);
        Assert.assertEquals(expectedSub,currentSubtitle.getSubtitles().get(0).getText());
    }

    private void setTime(Timestamp timestamp) {
        this.player.setTime(timestamp);
    }

    private void waitFor(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
