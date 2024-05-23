package io.github.vincemann.subtitlebuddy.test.srt.parser;

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
    private File endIdSrtFile;
    private File emptyLinesAtEndFile;

    // example files

    private File matrix;
    private File avengers;
    private File shutterIsland;
    private File titanicPart1;
    private File titanicPart2;
    private File titanicPart3;
    private File avatar;

    @Before
    public void init() {
        this.srtFileParser = new SrtFileParserImpl(new SubtitleTextParserImpl());
        loadFiles();
    }

    private void loadFiles(){
        this.endIdSrtFile = new File(TestFiles.CORRUPTED_END_SRT_FILE_PATH);
        this.emptyLinesAtEndFile = new File(TestFiles.EMPTY_LINES_AT_END_SRT_FILE_PATH);
        this.invalidSrtFile = new File(TestFiles.INVALID_SRT_FILE_PATH);
        this.validSrtFile = new File(TestFiles.VALID_SRT_FILE_PATH);

        this.matrix = new File("src/test/resources/srt/example/matrix.srt");
        this.avengers = new File("src/test/resources/srt/example/avengers.srt");
        this.shutterIsland = new File("src/test/resources/srt/example/shutter-island.srt");
        this.titanicPart1 = new File("src/test/resources/srt/example/titanic-part1.srt");
        this.titanicPart2 = new File("src/test/resources/srt/example/titanic-part2.srt");
        this.titanicPart3 = new File("src/test/resources/srt/example/titanic-part3.srt");
        this.avatar = new File("src/test/resources/srt/example/avatar.srt");
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
        performBasicChecks(subtitles,1123);

        int id = 5;
        SubtitleParagraph paragraph = createParagraph(id, "00:00:41,916", "00:00:44,084",
                new Subtitle(SubtitleType.NORMAL, "Whoa, Tappy.")
        );
        assertParagraphEqual(subtitles, paragraph);

        /*
            87
            00:08:19,332 --> 00:08:22,960
            Why don't you tell already the police?
            Maybe they could talk to Harry?
         */
        id = 87;
        paragraph = createParagraph(id, "00:08:19,332", "00:08:22,960",
                new Subtitle(SubtitleType.NORMAL, "Why don't you tell already the police?"),
                Subtitle.NEWLINE,
                new Subtitle(SubtitleType.NORMAL, "Maybe they could talk to Harry?")
        );
        assertParagraphEqual(subtitles, paragraph);

        /*
            92
            00:08:32,429 --> 00:08:34,555
            Thank you, <i>Mr. Rabinowitz.</i>
         */
        id = 92;
        paragraph = createParagraph(id, "00:08:32,429", "00:08:34,555",
                new Subtitle(SubtitleType.NORMAL, "Thank you, "),
                new Subtitle(SubtitleType.ITALIC, "Mr. Rabinowitz.")
        );
        assertParagraphEqual(subtitles, paragraph);

        /*
            1032
            01:28:50,038 --> 01:28:51,622
            You all right?<n>Whats Wrong?
         */
        id = 1032;
        paragraph = createParagraph(id, "01:28:50,038", "01:28:51,622",
                new Subtitle(SubtitleType.NORMAL, "You all right?"),
                Subtitle.NEWLINE,
                new Subtitle(SubtitleType.NORMAL, "Whats Wrong?")
        );
        assertParagraphEqual(subtitles, paragraph);
        /*
            1079
            01:31:43,586 --> 01:31:45,879
            <i>
            You got a rotten attitude,</i>
            you know that, man?<n>
         */
        id = 1079;
        paragraph = createParagraph(id, "01:31:43,586", "01:31:45,879",
                Subtitle.NEWLINE,
                new Subtitle(SubtitleType.ITALIC, "You got a rotten attitude,"),
                Subtitle.NEWLINE,
                new Subtitle(SubtitleType.NORMAL, "you know that, man?"),
                Subtitle.NEWLINE
        );
        assertParagraphEqual(subtitles, paragraph);
    }

    @Test
    public void testCorruptedEndFile() throws FileNotFoundException, InvalidTimestampFormatException, CorruptedSrtFileException {
        // when only the end is corrupted the parser usually can deal with that
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(endIdSrtFile);
        performBasicChecks(subtitles,560);

        /*
        557
        00:44:08,160 --> 00:44:11,630
        It's just part of the game.
        You wait right here, and....
         */
        int id = 557;
        SubtitleParagraph paragraph = createParagraph(id, "00:44:08,160", "00:44:11,630",
                new Subtitle(SubtitleType.NORMAL, "It's just part of the game."),
                Subtitle.NEWLINE,
                new Subtitle(SubtitleType.NORMAL, "You wait right here, and....")
        );
        assertParagraphEqual(subtitles, paragraph);

        // last paragraph will be screwed then - fair enough
         /*
            560
            00:44:26,040 --> 00:44:29,510
            You have a good
            rest of your life, kid.

            9999
            00:00:0,500 --> 00:00:2,00
            bs here
         */
        id = 560;
        paragraph = createParagraph(id, "00:44:26,040", "00:44:29,510",
                new Subtitle(SubtitleType.NORMAL, "You have a good"),
                Subtitle.NEWLINE,
                new Subtitle(SubtitleType.NORMAL, "rest of your life, kid.")
        );
        assertParagraphEqual(subtitles, paragraph);

        // make sure special end of file was not read to avoid false corruption messages
        Assert.assertEquals(560, subtitles.get(subtitles.size() - 1).getId());
    }


    @Test
    public void testEmptyLinesAtAnd() throws FileNotFoundException, CorruptedSrtFileException, InvalidTimestampFormatException {
        // parser can deal with it
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(emptyLinesAtEndFile);
        performBasicChecks(subtitles,1123);

        /*
        1123
        01:37:07,369 --> 01:37:09,161
        I love you, too, Ma.
         */
        int id = 1123;
        SubtitleParagraph paragraph = createParagraph(id, "01:37:07,369", "01:37:09,161",
                new Subtitle(SubtitleType.NORMAL, "I love you, too, Ma.")
        );
        assertParagraphEqual(subtitles, id - 1, paragraph);

    }

    @Test
    public void testMatrix() throws CorruptedSrtFileException, FileNotFoundException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(matrix);
        performBasicChecks(subtitles,1341,0);
    }

    @Test
    public void testAvengers() throws CorruptedSrtFileException, FileNotFoundException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(avengers);
        performBasicChecks(subtitles,1760);
    }

    @Test
    public void testAvatar() throws CorruptedSrtFileException, FileNotFoundException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(avatar);
        performBasicChecks(subtitles,1217);
    }

    private void performBasicChecks(List<SubtitleParagraph> subtitles, int lastId, int... startsWith){
        int start;
        if (startsWith.length == 0)
            start = 1;
        else
            start = startsWith[0];

        Assert.assertNotNull(subtitles);
        Assert.assertEquals(start == 1 ? lastId : lastId+1, subtitles.size());
        Assert.assertEquals(start, subtitles.get(0).getId());
        for (int i = 1; i <= lastId; i++) {
            SubtitleParagraph sub = subtitles.get(i - start);
            System.err.println("checking sub at pos: " + String.valueOf(i-start));
            System.err.println(sub);
            Assert.assertEquals(i, subtitles.get(i - start).getId());
        }
    }

    private SubtitleParagraph createParagraph(int id, String start, String end, Subtitle... subs) throws InvalidTimestampFormatException {
        SubtitleTimestamps timestamps = new SubtitleTimestamps(new Timestamp(start), new Timestamp(end));
        List<Subtitle> expectedSubs = Lists.newArrayList(subs);
        return new SubtitleParagraph(id, timestamps, new SubtitleText(expectedSubs));
    }

    private void assertParagraphEqual(List<SubtitleParagraph> paragraphs, SubtitleParagraph expected) {
        assertParagraphEqual(paragraphs, expected.getId()-1, expected);
    }

    private void assertParagraphEqual(List<SubtitleParagraph> paragraphs, int pos, SubtitleParagraph expected) {
        SubtitleParagraph actual = paragraphs.get(pos);
        System.err.println("expected paragraph at pos: " + pos + ": \n" + expected);
        System.err.println("actual paragraph at pos: " + pos + ": \n" + actual);
        Assert.assertEquals(expected, actual);
    }

}
