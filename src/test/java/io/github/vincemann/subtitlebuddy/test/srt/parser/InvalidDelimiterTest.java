package io.github.vincemann.subtitlebuddy.test.srt.parser;

import io.github.vincemann.subtitlebuddy.srt.SrtDelimiter;
import io.github.vincemann.subtitlebuddy.srt.parser.InvalidDelimiterException;
import io.github.vincemann.subtitlebuddy.srt.parser.SubtitleTextParserImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class InvalidDelimiterTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "<nn>", null }, { "<>", null}, { ">i<", null }, { "", null },
                {" ", null}, { "<<n>>", null }, { "<i/>", null }, {"</>", null},
                {"</n>",null}, {null,null}
        });
    }

    private String input;

    private SrtDelimiter expected;

    public InvalidDelimiterTest(String input, SrtDelimiter expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test(expected = InvalidDelimiterException.class)
    public void testWrongDelimter() throws InvalidDelimiterException{
        new SubtitleTextParserImpl().findDelimiterType(input);
    }
}
