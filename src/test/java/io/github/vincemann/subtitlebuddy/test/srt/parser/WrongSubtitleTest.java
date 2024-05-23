package io.github.vincemann.subtitlebuddy.test.srt.parser;

import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import io.github.vincemann.subtitlebuddy.srt.parser.InvalidDelimiterException;
import io.github.vincemann.subtitlebuddy.srt.parser.SubtitleTextParserImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class WrongSubtitleTest {

    private static final String TEST_TEXT =  "Hallo Frauke Co von radius";

    public WrongSubtitleTest(String input) {
        this.input = input;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "<i>"+TEST_TEXT+"<></i><n>"},
                { TEST_TEXT+"<"},
        });
    }

    private String input;




    @Test(expected = InvalidDelimiterException.class)
    public void testCreateSubtitleSegments() throws InvalidDelimiterException {
        SubtitleText result = new SubtitleTextParserImpl().parse(input);
    }
}
