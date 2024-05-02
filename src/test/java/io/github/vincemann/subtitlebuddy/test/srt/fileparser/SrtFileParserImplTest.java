package io.github.vincemann.subtitlebuddy.test.srt.fileparser;

import io.github.vincemann.subtitlebuddy.srt.SubtitleParagraph;
import io.github.vincemann.subtitlebuddy.test.TestFiles;
import io.github.vincemann.subtitlebuddy.srt.parser.CorruptedSrtFileException;
import io.github.vincemann.subtitlebuddy.srt.parser.InvalidTimestampFormatException;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtFileParser;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtFileParserImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class SrtFileParserImplTest {


    private SrtFileParser srtFileParser;
    private File invalidSrtFile;
    private File validSrtFile;
    private File endCorruptedSrtFile;
    private File halfCorruptedSrtFile;

    @Before
    public void init(){
        this.endCorruptedSrtFile = new File(TestFiles.CORRUPTED_SRT_FILE_PATH);
        this.halfCorruptedSrtFile= new File(TestFiles.EMPTY_LINES_AT_END_SRT_FILE_PATH);
        this.invalidSrtFile = new File(TestFiles.INVALID_SRT_FILE_PATH);
        this.validSrtFile= new File(TestFiles.VALID_SRT_FILE_PATH);
        this.srtFileParser = new SrtFileParserImpl();
    }


    @Test
    public void testEmptyFile() throws FileNotFoundException, InvalidTimestampFormatException {
        try {
            srtFileParser.transformFileToSubtitles(invalidSrtFile);
            Assert.fail();
        } catch (CorruptedSrtFileException e) {
            Assert.assertEquals(0, e.getLinesRead());
            Assert.assertEquals(0, e.getReadSubtitles().size());
        }
    }

    @Test
    public void testValidFile() throws FileNotFoundException, CorruptedSrtFileException {
        List<SubtitleParagraph> subtitles = srtFileParser.transformFileToSubtitles(validSrtFile);
        Assert.assertNotNull(subtitles);
        Assert.assertNotEquals(0, subtitles.size());
    }

    @Test
    public void testEndCorruptedFile() throws FileNotFoundException, InvalidTimestampFormatException {
        try {
            srtFileParser.transformFileToSubtitles(endCorruptedSrtFile);
            Assert.fail();
        } catch (CorruptedSrtFileException e) {
            Assert.assertEquals(2503, e.getLinesRead());
            Assert.assertTrue(e.getReadSubtitles().get(e.getReadSubtitles().size() - 1).toString().contains("You have a good"));
        }
    }



    @Test(expected = CorruptedSrtFileException.class)
    public void testFullyCorruptedFile() throws FileNotFoundException, CorruptedSrtFileException {
        srtFileParser.transformFileToSubtitles(halfCorruptedSrtFile);
    }


}
