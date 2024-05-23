package io.github.vincemann.subtitlebuddy.test.srt.fileparser;

import io.github.vincemann.subtitlebuddy.srt.Subtitle;
import io.github.vincemann.subtitlebuddy.srt.SubtitleType;
import io.github.vincemann.subtitlebuddy.srt.parser.InvalidDelimiterException;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtFileParserImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class RightSubtitleTest {

    private static final String TEST_TEXT =  "Hallo Frauke Co von radius";
    private static final String TEST_TEXT2 =  "Was geht docccc";
    private static final String TEST_TEXT3 =  "I bims 1 dritter Text";

    public RightSubtitleTest(String input, List<List<Subtitle>> expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "<i>"+TEST_TEXT+"</i><n>",
                        Arrays.asList(Arrays.asList(new Subtitle(SubtitleType.ITALIC,TEST_TEXT)))},
                { TEST_TEXT+"<n>",
                        Arrays.asList(Arrays.asList(new Subtitle(SubtitleType.NORMAL,TEST_TEXT)))},
                { TEST_TEXT+"<n>"+TEST_TEXT2+"<n>",
                        Arrays.asList(Arrays.asList(new Subtitle(SubtitleType.NORMAL,TEST_TEXT)),Arrays.asList(new Subtitle(SubtitleType.NORMAL,TEST_TEXT2)))},
                { "<i>"+TEST_TEXT+"</i><n><i>"+TEST_TEXT2+"</i><n>",
                        Arrays.asList(Arrays.asList(new Subtitle(SubtitleType.ITALIC,TEST_TEXT)),Arrays.asList(new Subtitle(SubtitleType.ITALIC,TEST_TEXT2)))},
                { "<i>"+TEST_TEXT+"</i><n>"+TEST_TEXT2+"<n>",
                        Arrays.asList(Arrays.asList(new Subtitle(SubtitleType.ITALIC,TEST_TEXT)),Arrays.asList(new Subtitle(SubtitleType.NORMAL,TEST_TEXT2)))},
                { "<i>"+TEST_TEXT+"</i><n>"+TEST_TEXT2+"<n>"+"<i>"+TEST_TEXT3+"</i><n>",
                        Arrays.asList(Arrays.asList(new Subtitle(SubtitleType.ITALIC,TEST_TEXT)),Arrays.asList(new Subtitle(SubtitleType.NORMAL,TEST_TEXT2)),Arrays.asList(new Subtitle(SubtitleType.ITALIC,TEST_TEXT3)))},
                {"<i><i>"+TEST_TEXT+"</i><n>",
                        Arrays.asList(Arrays.asList(new Subtitle(SubtitleType.ITALIC,TEST_TEXT)))},
                {"<i></i><n>",
                        Arrays.asList(Arrays.asList(new Subtitle(SubtitleType.ITALIC,"")))},
                {"<n>",
                        Arrays.asList(Collections.EMPTY_LIST)},
                { "<i>"+TEST_TEXT+"</i><n><n>",
                        Arrays.asList(Arrays.asList(new Subtitle(SubtitleType.ITALIC,TEST_TEXT)),Collections.EMPTY_LIST)},
                { "<i>"+TEST_TEXT+"<n>"+TEST_TEXT2+"</i><n>",
                        Arrays.asList(Arrays.asList(new Subtitle(SubtitleType.ITALIC,TEST_TEXT)),Arrays.asList(new Subtitle(SubtitleType.ITALIC,TEST_TEXT2)))},
                { TEST_TEXT+"<n>"+TEST_TEXT2+"<n>",
                        Arrays.asList(Arrays.asList(new Subtitle(SubtitleType.NORMAL,TEST_TEXT)),Arrays.asList(new Subtitle(SubtitleType.NORMAL,TEST_TEXT2)))},
                { "<i>"+TEST_TEXT+"<n>"+TEST_TEXT2+"</i>"+TEST_TEXT3+"<n>",
                        Arrays.asList(Arrays.asList(new Subtitle(SubtitleType.ITALIC,TEST_TEXT)),Arrays.asList(new Subtitle(SubtitleType.ITALIC,TEST_TEXT2),new Subtitle(SubtitleType.NORMAL,TEST_TEXT3)))},
                {"<i><n><n><n>"+TEST_TEXT+"</i><n>",
                        Arrays.asList(Collections.EMPTY_LIST,Collections.EMPTY_LIST,Collections.EMPTY_LIST,Arrays.asList(new Subtitle(SubtitleType.ITALIC,TEST_TEXT)))},
        });
    }

    private String input;

    private List<List<Subtitle>> expected;



    @Test
    public void testCreateSubtitleSegments() throws InvalidDelimiterException {
        List<List<Subtitle>> result = SrtFileParserImpl.createSubtitleSegments(input);
        Assert.assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i).size(), result.get(i).size());
            for (int j = 0; j < expected.get(i).size(); j++) {
                Assert.assertEquals(expected.get(i).get(j), result.get(i).get(j));
            }
        }
    }
}
