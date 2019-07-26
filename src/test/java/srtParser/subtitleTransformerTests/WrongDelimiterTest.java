package srtParser.subtitleTransformerTests;

import com.youneedsoftware.subtitleBuddy.srt.SrtDelimiterType;
import com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer.InvalidDelimiterException;
import com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformerImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class WrongDelimiterTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "<nn>", null }, { "<>", null}, { ">i<", null }, { "", null },
                {" ", null}, { "<<n>>", null }, { "<i/>", null }, {"</>", null},
                {"</n>",null}, {null,null}
        });
    }

    private String input;

    private SrtDelimiterType expected;

    public WrongDelimiterTest(String input, SrtDelimiterType expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test(expected = InvalidDelimiterException.class)
    public void testWrongDelimter() throws InvalidDelimiterException{
        SrtFileTransformerImpl.findDelimiterType(input);
    }
}
