package io.github.vincemann.subtitlebuddy.test.srt.fileparser;

import com.google.common.collect.Lists;
import io.github.vincemann.subtitlebuddy.srt.*;
import io.github.vincemann.subtitlebuddy.srt.parser.*;
import io.github.vincemann.subtitlebuddy.test.TestFiles;
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
    private File corruptedEndSrtFile;
    private File halfCorruptedSrtFile;

    @Before
    public void init(){
        this.corruptedEndSrtFile = new File(TestFiles.CORRUPTED_END_SRT_FILE_PATH);
        this.halfCorruptedSrtFile= new File(TestFiles.EMPTY_LINES_AT_END_SRT_FILE_PATH);
        this.invalidSrtFile = new File(TestFiles.INVALID_SRT_FILE_PATH);
        this.validSrtFile= new File(TestFiles.VALID_SRT_FILE_PATH);
        this.srtFileParser = new SrtFileParserImpl(new SubtitleTextParserImpl());
    }


    @Test
    public void testEmptyFile() throws FileNotFoundException, InvalidTimestampFormatException {
        try {
            srtFileParser.parseFile(invalidSrtFile);
            Assert.fail();
        } catch (CorruptedSrtFileException e) {
            Assert.assertEquals(0, e.getLinesRead());
            Assert.assertEquals(0, e.getReadSubtitles().size());
        }
    }

    @Test
    public void testValidFile() throws FileNotFoundException, CorruptedSrtFileException, InvalidTimestampFormatException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(validSrtFile);
        Assert.assertNotNull(subtitles);
        Assert.assertEquals(1123,subtitles.size());
        Assert.assertEquals(1,subtitles.get(0).getId());
        for (int i = 1; i<=1123; i++){
            Assert.assertEquals(i,subtitles.get(i-1).getId());
        }
        SubtitleParagraph paragraph = createParagraph(5, "00:00:41,916", "00:00:44,084", new Subtitle(SubtitleType.NORMAL, "Whoa, Tappy."), Subtitle.NEWLINE);
        assertParagraphEqual(subtitles,4,paragraph);
    }

    private SubtitleParagraph createParagraph(int id, String start, String end, Subtitle... subs) throws InvalidTimestampFormatException {
        SubtitleTimestamps timestamps = new SubtitleTimestamps(new Timestamp(start),new Timestamp(end));
        List<Subtitle> expectedSubs = Lists.newArrayList(subs);
        return new SubtitleParagraph(id,timestamps,new SubtitleText(expectedSubs));
    }

    private void assertParagraphEqual(List<SubtitleParagraph> paragraphs, SubtitleParagraph expected){
        assertParagraphEqual(paragraphs,expected.getId(),expected);
    }
    private void assertParagraphEqual(List<SubtitleParagraph> paragraphs,int pos, SubtitleParagraph expected){
        SubtitleParagraph actual = paragraphs.get(pos);
        System.err.println("expected paragraph at pos: " + pos + ": \n" + expected);
        System.err.println("actual paragraph at pos: " + pos + ": \n" + actual);
        Assert.assertEquals(expected,actual);
    }

    private void checkSubtitles(List<SubtitleParagraph> paragraphs, int pos, List<Subtitle> expectedSubs){
        SubtitleParagraph paragraph = paragraphs.get(pos);
        List<Subtitle> actualSubs = paragraph.getText().getSubtitles();
        Assert.assertEquals(expectedSubs.size(),actualSubs.size());
        for (int i = 0; i<expectedSubs.size(); i++){
            Subtitle expected = expectedSubs.get(i);
            Subtitle actual = actualSubs.get(i);
            Assert.assertEquals(expected,actual);
        }
    }

    @Test
    public void testEndCorruptedFile() throws FileNotFoundException, InvalidTimestampFormatException {
        try {
            List<SubtitleParagraph> paragraphs = srtFileParser.parseFile(corruptedEndSrtFile);
            System.err.println(paragraphs);
            Assert.fail();
        } catch (CorruptedSrtFileException e) {
            Assert.assertEquals(2503, e.getLinesRead());
            Assert.assertTrue(e.getReadSubtitles().get(e.getReadSubtitles().size() - 1).toString().contains("You have a good"));
        }
    }



    @Test
    public void testFullyCorruptedFile() throws FileNotFoundException, CorruptedSrtFileException {
        srtFileParser.parseFile(halfCorruptedSrtFile);
    }


}
