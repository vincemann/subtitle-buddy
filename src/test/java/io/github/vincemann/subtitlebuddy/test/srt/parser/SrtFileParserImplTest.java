package io.github.vincemann.subtitlebuddy.test.srt.parser;

import com.google.common.collect.Lists;
import io.github.vincemann.subtitlebuddy.srt.*;
import io.github.vincemann.subtitlebuddy.srt.parser.*;
import io.github.vincemann.subtitlebuddy.test.TestFiles;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SrtFileParserImplTest {

    private SrtFileParser srtFileParser;

    private File validSrtFile;

    private File idInPayload;

    // fully corrupted
    private File emptyFile;
    private File invalidTimestampFile;
    private File missingTimestampFile;
    private File missingIdFile;
    private File missingEmptyLineFile;


    // special meta information
    private File endIdSrtFile;

    // kinda corrupted files, that should still work
    private File emptyLinesAtEndFile;
    private File emptyLinesInBetweenFile;
    private File lenientTimestampFile;

    // example files

    private File matrix;
    private File avengers;
    private File shutterIsland;
    private File titanicPart1;
    private File titanicPart2;
    private File titanicPart3;
    private File avatar;

    private File badnaam;
    private File fanny;
    private File godzilla;
    private File goodsam;
    private File inception;
    private File kgf;
    private File potter2;

    @Before
    public void init() {
        this.srtFileParser = new SrtFileParserImpl(new SubtitleTextParserImpl());
        loadFiles();
    }

    private void loadFiles() {
        // meta data
        this.endIdSrtFile = new File("src/test/resources/srt/end-id.srt");

        // valid
        this.validSrtFile = new File(TestFiles.VALID_SRT_FILE_PATH);
        this.idInPayload = new File("src/test/resources/srt/id-in-payload.srt");

        // kinda corrupted but still acceptable
        this.lenientTimestampFile = new File("src/test/resources/srt/lenient-timestamp.srt");
        this.emptyLinesAtEndFile = new File("src/test/resources/srt/empty-lines-at-end.srt");
        this.emptyLinesInBetweenFile = new File("src/test/resources/srt/empty-lines-in-between.srt");

        // invalid
        this.emptyFile = new File("src/test/resources/srt/invalid/empty.srt");
        this.invalidTimestampFile = new File("src/test/resources/srt/invalid/invalid-timestamp.srt");
        this.missingTimestampFile = new File("src/test/resources/srt/invalid/missing-timestamp.srt");
        this.missingIdFile = new File("src/test/resources/srt/invalid/missing-id.srt");
        this.missingEmptyLineFile = new File("src/test/resources/srt/invalid/missing-empty-line.srt");

        // example
        this.matrix = new File("src/test/resources/srt/example/matrix.srt");
        this.avengers = new File("src/test/resources/srt/example/avengers.srt");
        this.shutterIsland = new File("src/test/resources/srt/example/shutter-island.srt");
        this.titanicPart1 = new File("src/test/resources/srt/example/titanic-part1.srt");
        this.titanicPart2 = new File("src/test/resources/srt/example/titanic-part2.srt");
        this.titanicPart3 = new File("src/test/resources/srt/example/titanic-part3.srt");
        this.avatar = new File("src/test/resources/srt/example/avatar.srt");
        this.badnaam = new File("src/test/resources/srt/example/badnaam.srt");
        this.fanny = new File("src/test/resources/srt/example/fanny.srt");
        this.godzilla = new File("src/test/resources/srt/example/godzilla.srt");
        this.goodsam = new File("src/test/resources/srt/example/goodsam.srt");
        this.inception = new File("src/test/resources/srt/example/inception.srt");
        this.kgf = new File("src/test/resources/srt/example/kgf.srt");
        this.potter2 = new File("src/test/resources/srt/example/potter2.srt");
    }


    @Test
    public void testEmptyFile() throws IOException, InvalidTimestampFormatException {
        try {
            srtFileParser.parseFile(emptyFile);
            Assert.fail();
        } catch (CorruptedSrtFileException e) {
            Assert.assertEquals(0, e.getLinesRead());
            Assert.assertEquals(0, e.getReadSubtitles().size());
        }
    }

    /*
    0:6:12 will also work not only 00:06:12
     */
    @Test
    public void testLenientTimestampPolicy() throws IOException, InvalidTimestampFormatException, CorruptedSrtFileException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(lenientTimestampFile);
        performBasicChecks(subtitles, 1123);

        int id = 63;
        SubtitleParagraph paragraph = createParagraph(id, "00:06:16,459", "00:06:19,253",
                new Subtitle(SubtitleType.NORMAL, "This is some boss skag, baby.")
        );
        assertParagraphEqual(subtitles, paragraph);
    }

    @Test
    public void testInvalidTimestamp() throws IOException {
        try {
            srtFileParser.parseFile(invalidTimestampFile);
        } catch (CorruptedSrtFileException e) {
            Assert.assertEquals(InvalidTimestampFormatException.class, e.getCause().getClass());
            Assert.assertEquals(270, e.getLinesRead());
            Assert.assertEquals(62, e.getReadSubtitles().size());
        }
    }

    @Test
    public void testMissingTimestamp() throws IOException {
        try {
            srtFileParser.parseFile(missingTimestampFile);
        } catch (CorruptedSrtFileException e) {
            Assert.assertEquals(InvalidTimestampFormatException.class, e.getCause().getClass());
            Assert.assertEquals(270, e.getLinesRead());
            Assert.assertEquals(62, e.getReadSubtitles().size());
        }
    }

    @Test
    public void testMissingId() throws IOException {
        try {
            srtFileParser.parseFile(missingIdFile);
        } catch (CorruptedSrtFileException e) {
            Assert.assertEquals(InvalidTimestampFormatException.class, e.getCause().getClass());
            Assert.assertEquals(2929, e.getLinesRead());
            Assert.assertEquals(663, e.getReadSubtitles().size());
        }
    }

    @Test
    public void testMissingEmptyLine() throws IOException {
        try {
            srtFileParser.parseFile(missingEmptyLineFile);
        } catch (CorruptedSrtFileException e) {
            Assert.assertEquals(InvalidTimestampFormatException.class, e.getCause().getClass());
            Assert.assertEquals(3140, e.getLinesRead());
            Assert.assertEquals(709, e.getReadSubtitles().size());
        }
    }

    @Test
    public void testNextIdInPayloadGetsIgnored() throws IOException, CorruptedSrtFileException, InvalidTimestampFormatException {
        List<SubtitleParagraph> subtitles =srtFileParser.parseFile(idInPayload);
        performBasicChecks(subtitles, 1123);

        /*
        3
        00:00:37,203 --> 00:00:38,245
        Juice by Tappy.
        4
         */
        int id = 3;
        SubtitleParagraph paragraph = createParagraph(id, "00:00:37,203", "00:00:38,245",
                new Subtitle(SubtitleType.NORMAL, "Juice by Tappy."),
                Subtitle.NEWLINE,
                new Subtitle(SubtitleType.NORMAL, "4")
        );
        assertParagraphEqual(subtitles, paragraph);

        /*
        4
        00:00:39,330 --> 00:00:41,749
        Tappy got juice. Tappy got juice.
         */
        id = 4;
        paragraph = createParagraph(id, "00:00:39,330", "00:00:41,749",
                new Subtitle(SubtitleType.NORMAL, "Tappy got juice. Tappy got juice.")
        );
        assertParagraphEqual(subtitles, paragraph);
    }


    @Test
    public void testValidFile() throws IOException, CorruptedSrtFileException, InvalidTimestampFormatException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(validSrtFile);
        performBasicChecks(subtitles, 1123);

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
    public void testEndMetadataTagFile() throws IOException, InvalidTimestampFormatException, CorruptedSrtFileException {
        // when only the end is corrupted the parser usually can deal with that
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(endIdSrtFile);
        performBasicChecks(subtitles, 560);

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
    public void testEmptyLinesAtAndShouldWork() throws IOException, CorruptedSrtFileException, InvalidTimestampFormatException {
        // parser can deal with it
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(emptyLinesAtEndFile);
        performBasicChecks(subtitles, 1123);

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
    public void testEmptyInBetweenShouldWork() throws IOException, CorruptedSrtFileException, InvalidTimestampFormatException {
        // parser can deal with it
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(emptyLinesInBetweenFile);
        performBasicChecks(subtitles, 1123);

        /*
        1123
        01:37:07,369 --> 01:37:09,161
        I love you, too, Ma.
         */
        int id = 1123;
        SubtitleParagraph paragraph = createParagraph(id, "01:37:07,369", "01:37:09,161",
                new Subtitle(SubtitleType.NORMAL, "I love you, too, Ma.")
        );
        assertParagraphEqual(subtitles, paragraph);

        /*
        1087
        01:32:23,251 --> 01:32:24,418
        That's it.
         */
        id = 1087;
        paragraph = createParagraph(id, "01:32:23,251", "01:32:24,418",
                new Subtitle(SubtitleType.NORMAL, "That's it.")
        );
        assertParagraphEqual(subtitles, paragraph);


        /*
        1088
        01:32:25,337 --> 01:32:27,546
        - Nice and easy.
        - Keep it in.
         */
        id = 1088;
        paragraph = createParagraph(id, "01:32:25,337", "01:32:27,546",
                new Subtitle(SubtitleType.NORMAL, "- Nice and easy."),
                Subtitle.NEWLINE,
                new Subtitle(SubtitleType.NORMAL, "- Keep it in.")
        );
        assertParagraphEqual(subtitles, paragraph);


    }

    @Test
    public void testMatrix() throws CorruptedSrtFileException, IOException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(matrix);
        performBasicChecks(subtitles, 1341, 0);
    }

    @Test
    public void testAvengers() throws CorruptedSrtFileException, IOException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(avengers);
        performBasicChecks(subtitles, 1760);
    }

    @Test
    public void testAvatar() throws CorruptedSrtFileException, IOException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(avatar);
        performBasicChecks(subtitles, 1217);
    }

    @Test
    public void testShutterIsland() throws CorruptedSrtFileException, IOException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(shutterIsland);
        performBasicChecks(subtitles, 1575);
    }

    @Test
    public void testTitanic1() throws CorruptedSrtFileException, IOException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(titanicPart1);
        performBasicChecks(subtitles, 826);
    }

    @Test
    public void testTitanic2() throws CorruptedSrtFileException, IOException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(titanicPart2);
        performBasicChecks(subtitles, 777);
    }

    @Test
    public void testTitanic3() throws CorruptedSrtFileException, IOException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(titanicPart3);
        performBasicChecks(subtitles, 570);
    }

    @Test
    public void testBaadnam() throws CorruptedSrtFileException, IOException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(badnaam);
        performBasicChecks(subtitles, 1514);
    }

    @Test
    public void testFanny() throws CorruptedSrtFileException, IOException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(fanny);
        performBasicChecks(subtitles, 1577);
    }

    @Test
    public void testGodzilla() throws CorruptedSrtFileException, IOException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(godzilla);
        performBasicChecks(subtitles, 902);
    }

    @Test
    public void testGoodSam() throws CorruptedSrtFileException, IOException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(goodsam);
        performBasicChecks(subtitles, 1838);
    }

    @Test
    public void testInception() throws CorruptedSrtFileException, IOException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(inception);
        performBasicChecks(subtitles, 1738);
    }

    @Test
    public void testKgf() throws CorruptedSrtFileException, IOException, InvalidTimestampFormatException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(kgf);
        performBasicChecks(subtitles, 2245);

        int id = 2229;
        SubtitleParagraph paragraph = createParagraph(id, "02:30:34,105", "02:30:38,485",
                new Subtitle(SubtitleType.NORMAL, "Si 400 personnes armées étaient prêtes à tuer comme l'avait ordonné Vanaram,")
        );
        assertParagraphEqual(subtitles, paragraph);
    }

    @Test
    public void testPotter2() throws CorruptedSrtFileException, IOException {
        List<SubtitleParagraph> subtitles = srtFileParser.parseFile(potter2);
        performBasicChecks(subtitles, 592);
    }


    private void performBasicChecks(List<SubtitleParagraph> subtitles, int lastId, int... startsWith) {
        int start;
        if (startsWith.length == 0)
            start = 1;
        else
            start = startsWith[0];

        Assert.assertNotNull(subtitles);
        Assert.assertEquals(start == 1 ? lastId : lastId + 1, subtitles.size());
        Assert.assertEquals(start, subtitles.get(0).getId());
        for (int i = 1; i <= lastId; i++) {
            SubtitleParagraph sub = subtitles.get(i - start);
            // maybe need to adjust this value in the future - when paragraphs are found with many subs, its a sign that it failed
            Assert.assertTrue(sub.getText().getSubtitles().size() <= 8);
            Assert.assertEquals(i, subtitles.get(i - start).getId());
        }
    }

    private SubtitleParagraph createParagraph(int id, String start, String end, Subtitle... subs) throws InvalidTimestampFormatException {
        SubtitleTimestamps timestamps = new SubtitleTimestamps(new Timestamp(start), new Timestamp(end));
        List<Subtitle> expectedSubs = Lists.newArrayList(subs);
        return new SubtitleParagraph(id, timestamps, new SubtitleText(expectedSubs));
    }

    private void assertParagraphEqual(List<SubtitleParagraph> paragraphs, SubtitleParagraph expected) {
        assertParagraphEqual(paragraphs, expected.getId() - 1, expected);
    }

    private void assertParagraphEqual(List<SubtitleParagraph> paragraphs, int pos, SubtitleParagraph expected) {
        SubtitleParagraph actual = paragraphs.get(pos);
//        System.err.println("expected paragraph at pos: " + pos + ": \n" + expected);
//        System.err.println("actual paragraph at pos: " + pos + ": \n" + actual);
//        System.err.println("");
        Assert.assertEquals(expected, actual);
    }

}
