package srtParser;

import com.youneedsoftware.subtitleBuddy.srt.subtitleFile.TimeStampOutOfBoundsException;
import com.youneedsoftware.subtitleBuddy.srt.Subtitle;
import com.youneedsoftware.subtitleBuddy.srt.Timestamp;
import com.youneedsoftware.subtitleBuddy.srt.subtitleFile.SubtitleFile;
import com.youneedsoftware.subtitleBuddy.srt.subtitleFile.SubtitleFileImpl;
import com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformerImpl;
import constants.TestConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Optional;

public class SubtitleFileImplTest {

    private SubtitleFile subtitleFile;

    @Before
    public void setUp() throws Exception {
        this.subtitleFile= new SubtitleFileImpl(
                new SrtFileTransformerImpl().transformFileToSubtitles(
                        new File(TestConstants.VALID_SRT_FILE_PATH))
        );
    }

    @Test(expected = TimeStampOutOfBoundsException.class)
    public void testTooHighTimeStamp() throws TimeStampOutOfBoundsException {
        //knapp außerhalb der bounds
        Timestamp invalidTimeStamp = new Timestamp(1,38,0,0);
        subtitleFile.getSubtitleAtTimeStamp(invalidTimeStamp);
    }

    @Test(expected = TimeStampOutOfBoundsException.class)
    public void testNegativeTimeStamp() throws TimeStampOutOfBoundsException {
        Timestamp negativeTimeStamp = new Timestamp(0,-1,0,0);
        subtitleFile.getSubtitleAtTimeStamp(negativeTimeStamp);
    }

    @Test
    public void testEarlyTimeStamp() throws TimeStampOutOfBoundsException {
        //timestamp der vor dem ersten Timestamp kommt
        Timestamp earlyTimeStamp = new Timestamp(0,0,3,12);
        Optional<Subtitle> subtitle = subtitleFile.getSubtitleAtTimeStamp(earlyTimeStamp);
        Assert.assertFalse(subtitle.isPresent());
    }

    @Test
    public void testValidTimeStamp() throws TimeStampOutOfBoundsException {
        Timestamp timestamp = new Timestamp(0,55,8,12);
        Optional<Subtitle> subtitle = subtitleFile.getSubtitleAtTimeStamp(timestamp);
        Assert.assertTrue(subtitle.isPresent());
        Assert.assertEquals("We'll fill it up again.",subtitle.get().getSubtitleText().getSubtitleSegments().get(0).get(0).getText());
    }

    @Test
    public void testGapTimeStamp() throws TimeStampOutOfBoundsException {
        Timestamp gapTimeStamp = new Timestamp(1,10,29,0);
        Optional<Subtitle> subtitle = subtitleFile.getSubtitleAtTimeStamp(gapTimeStamp);
        Assert.assertFalse(subtitle.isPresent());
    }
}
