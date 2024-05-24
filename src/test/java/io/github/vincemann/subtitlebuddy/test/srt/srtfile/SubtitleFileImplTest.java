package io.github.vincemann.subtitlebuddy.test.srt.srtfile;

import io.github.vincemann.subtitlebuddy.srt.SubtitleParagraph;
import io.github.vincemann.subtitlebuddy.srt.Timestamp;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtFileParserImpl;
import io.github.vincemann.subtitlebuddy.srt.parser.SubtitleTextParserImpl;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFile;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFileImpl;
import io.github.vincemann.subtitlebuddy.srt.srtfile.TimeStampOutOfBoundsException;
import io.github.vincemann.subtitlebuddy.test.TestFiles;
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
                new SrtFileParserImpl(new SubtitleTextParserImpl()).parseFile(
                        new File(TestFiles.VALID_SRT_FILE_PATH))
        );
    }

    @Test(expected = TimeStampOutOfBoundsException.class)
    public void testTooHighTimeStamp() throws TimeStampOutOfBoundsException {
        // closely out of bounds
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
        // timestamp is before fist recorded
        Timestamp earlyTimeStamp = new Timestamp(0,0,3,12);
        Optional<SubtitleParagraph> subtitle = subtitleFile.getSubtitleAtTimeStamp(earlyTimeStamp);
        Assert.assertFalse(subtitle.isPresent());
    }

    @Test
    public void testValidTimeStamp() throws TimeStampOutOfBoundsException {
        Timestamp timestamp = new Timestamp(0,55,8,12);
        Optional<SubtitleParagraph> subtitle = subtitleFile.getSubtitleAtTimeStamp(timestamp);
        Assert.assertTrue(subtitle.isPresent());
        Assert.assertEquals("We'll fill it up again.",subtitle.get().getText().getSubtitles().get(0).getText());
    }

    @Test
    public void testGapTimeStamp() throws TimeStampOutOfBoundsException {
        Timestamp gapTimeStamp = new Timestamp(1,10,29,0);
        Optional<SubtitleParagraph> subtitle = subtitleFile.getSubtitleAtTimeStamp(gapTimeStamp);
        Assert.assertFalse(subtitle.isPresent());
    }
}
