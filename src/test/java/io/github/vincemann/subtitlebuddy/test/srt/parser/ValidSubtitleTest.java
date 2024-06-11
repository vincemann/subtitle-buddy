package io.github.vincemann.subtitlebuddy.test.srt.parser;

import io.github.vincemann.subtitlebuddy.srt.Subtitle;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import io.github.vincemann.subtitlebuddy.srt.SubtitleType;
import io.github.vincemann.subtitlebuddy.srt.parser.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class ValidSubtitleTest {

    private static final String TEST_TEXT = "Hallo Frauke Co von radius";
    private static final String TEST_TEXT2 = "Was geht docccc";
    private static final String TEST_TEXT3 = "I bims 1 dritter Text";

    public ValidSubtitleTest(String input, List<Subtitle> expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // 0
                {"<i>" + TEST_TEXT + "</i><n>",
                        Arrays.asList(new Subtitle(SubtitleType.ITALIC, TEST_TEXT),Subtitle.NEWLINE)},
                // 1
                {TEST_TEXT + "<n>",
                        Arrays.asList(new Subtitle(SubtitleType.NORMAL, TEST_TEXT),Subtitle.NEWLINE)},
                // 2
                {TEST_TEXT + "<n>" + TEST_TEXT2 + "<n>",
                        Arrays.asList(new Subtitle(SubtitleType.NORMAL, TEST_TEXT),Subtitle.NEWLINE, new Subtitle(SubtitleType.NORMAL, TEST_TEXT2),Subtitle.NEWLINE)},
                // 3
                {"<i>" + TEST_TEXT + "</i><n><i>" + TEST_TEXT2 + "</i><n>",
                        Arrays.asList(new Subtitle(SubtitleType.ITALIC, TEST_TEXT),Subtitle.NEWLINE, new Subtitle(SubtitleType.ITALIC, TEST_TEXT2),Subtitle.NEWLINE)},
                // 4
                {"<i>" + TEST_TEXT + "</i><n>" + TEST_TEXT2 + "<n>",
                        Arrays.asList(new Subtitle(SubtitleType.ITALIC, TEST_TEXT),Subtitle.NEWLINE, new Subtitle(SubtitleType.NORMAL, TEST_TEXT2),Subtitle.NEWLINE)},
                // 5
                {"<i>" + TEST_TEXT + "</i><n>" + TEST_TEXT2 + "<n>" + "<i>" + TEST_TEXT3 + "</i><n>",
                        Arrays.asList(new Subtitle(SubtitleType.ITALIC, TEST_TEXT),Subtitle.NEWLINE, new Subtitle(SubtitleType.NORMAL, TEST_TEXT2),Subtitle.NEWLINE, new Subtitle(SubtitleType.ITALIC, TEST_TEXT3),Subtitle.NEWLINE)},
                // 6
                {"<i><i>" + TEST_TEXT + "</i><n>",
                        Arrays.asList(new Subtitle(SubtitleType.ITALIC, TEST_TEXT),Subtitle.NEWLINE)},
                // 7
                {"<i></i><n>",
                        Arrays.asList(Subtitle.NEWLINE)},
                // 8
                {"<n>",
                        Arrays.asList(Subtitle.NEWLINE)},
                // 9
                {TEST_TEXT + "<i>"+TEST_TEXT2 + "</i>" + TEST_TEXT3,
                        Arrays.asList(new Subtitle(SubtitleType.NORMAL, TEST_TEXT), new Subtitle(SubtitleType.ITALIC, TEST_TEXT2), new Subtitle(SubtitleType.NORMAL, TEST_TEXT3))},
                // 10
                {TEST_TEXT,
                        Arrays.asList(new Subtitle(SubtitleType.NORMAL, TEST_TEXT))},
                // 11
                {TEST_TEXT + "<n>",
                        Arrays.asList(new Subtitle(SubtitleType.NORMAL, TEST_TEXT),Subtitle.NEWLINE)},

                {"<i><n>"+TEST_TEXT+"</i><n>"+TEST_TEXT2,
                        Arrays.asList(
                                Subtitle.NEWLINE,
                                new Subtitle(SubtitleType.ITALIC, TEST_TEXT)
                                ,Subtitle.NEWLINE,
                                new Subtitle(SubtitleType.NORMAL, TEST_TEXT2)
                        )
                },
                // 12
                {"<n>" + TEST_TEXT + "<n>",
                        Arrays.asList(Subtitle.NEWLINE,new Subtitle(SubtitleType.NORMAL, TEST_TEXT),Subtitle.NEWLINE)},
                // 13
                {TEST_TEXT + "<n>" + TEST_TEXT2,
                        Arrays.asList(new Subtitle(SubtitleType.NORMAL, TEST_TEXT),Subtitle.NEWLINE, new Subtitle(SubtitleType.NORMAL, TEST_TEXT2))},
                // 14
                {TEST_TEXT + "<n>" + TEST_TEXT2 + "<i>" + TEST_TEXT3 + "</i>",
                        Arrays.asList(new Subtitle(SubtitleType.NORMAL, TEST_TEXT),Subtitle.NEWLINE, new Subtitle(SubtitleType.NORMAL, TEST_TEXT2),new Subtitle(SubtitleType.ITALIC, TEST_TEXT3))},
                // 15
                {"<i>" + TEST_TEXT + "</i><n><n>",
                        Arrays.asList(new Subtitle(SubtitleType.ITALIC, TEST_TEXT), Subtitle.NEWLINE, Subtitle.NEWLINE)},
                // 16
                {"<i>" + TEST_TEXT + "<n>" + TEST_TEXT2 + "</i><n>",
                        Arrays.asList(new Subtitle(SubtitleType.ITALIC, TEST_TEXT),Subtitle.NEWLINE, new Subtitle(SubtitleType.ITALIC, TEST_TEXT2),Subtitle.NEWLINE)},
                // 17
                {TEST_TEXT + "<n>" + TEST_TEXT2 + "<n>",
                        Arrays.asList(new Subtitle(SubtitleType.NORMAL, TEST_TEXT),Subtitle.NEWLINE, new Subtitle(SubtitleType.NORMAL, TEST_TEXT2),Subtitle.NEWLINE)},
                // 18
                {TEST_TEXT + "<n>" + TEST_TEXT2 + "<n>",
                        Arrays.asList(new Subtitle(SubtitleType.NORMAL, TEST_TEXT),Subtitle.NEWLINE, new Subtitle(SubtitleType.NORMAL, TEST_TEXT2),Subtitle.NEWLINE)},
                // 19
                {"<i>" + TEST_TEXT + "<n>" + TEST_TEXT2 + "</i>" + TEST_TEXT3 + "<n>",
                        Arrays.asList(new Subtitle(SubtitleType.ITALIC, TEST_TEXT),Subtitle.NEWLINE, new Subtitle(SubtitleType.ITALIC, TEST_TEXT2), new Subtitle(SubtitleType.NORMAL, TEST_TEXT3),Subtitle.NEWLINE)},
                // 20
                {"<i><n><n><n>" + TEST_TEXT + "</i><n>",
                        Arrays.asList(Subtitle.NEWLINE,Subtitle.NEWLINE,Subtitle.NEWLINE,new Subtitle(SubtitleType.ITALIC, TEST_TEXT), Subtitle.NEWLINE)},
        });
    }

    private String input;

    private List<Subtitle> expected;


    @Test
    public void testCreateSubtitleSegments() throws InvalidDelimiterException {
        SubtitleText result = new SubtitleTextParserImpl().parse(input);
        List<Subtitle> subs = result.getSubtitles();
        Assert.assertEquals(expected.size(), subs.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), subs.get(i));
        }
    }
}
