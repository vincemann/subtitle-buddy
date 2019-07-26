package srtParser.subtitleTransformerTests;

import com.youneedsoftware.subtitleBuddy.srt.SubtitleSegment;
import com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer.InvalidDelimiterException;
import com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformerImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class WrongSubtitleSegmentTest {

    private static final String TEST_TEXT =  "Hallo Frauke Co von radius";

    public WrongSubtitleSegmentTest(String input) {
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
        List<List<SubtitleSegment>> result = SrtFileTransformerImpl.createSubtitleSegments(input);
    }
}
