package io.github.vincemann.subtitlebuddy.test.srt.parser;

import io.github.vincemann.subtitlebuddy.srt.SrtDelimiter;
import io.github.vincemann.subtitlebuddy.srt.parser.InvalidDelimiterException;
import io.github.vincemann.subtitlebuddy.srt.parser.SubtitleTextParserImpl;
import org.junit.Assert;
import org.junit.Test;

public class RightDelimiterTest {

    @Test
    public void testFindItalicStartDelimiterType() throws InvalidDelimiterException {
        String testData = "<i>alksmfalksdm";
        SrtDelimiter srtDelimiter = new SubtitleTextParserImpl().findDelimiterType(testData);
        Assert.assertEquals(SrtDelimiter.ITALIC_START_DELIMITER, srtDelimiter);
    }

    @Test
    public void testFindItalicEndDelimiterType() throws InvalidDelimiterException {
        String testData = "</i>p,ewfpos,földs";
        SrtDelimiter srtDelimiter = new SubtitleTextParserImpl().findDelimiterType(testData);
        Assert.assertEquals(SrtDelimiter.ITALIC_END_DELIMITER, srtDelimiter);
    }

    @Test
    public void testFindNewLineDelimiterType() throws InvalidDelimiterException {
        String testData = "<n>asölfma,ös,";
        SrtDelimiter srtDelimiter = new SubtitleTextParserImpl().findDelimiterType(testData);
        Assert.assertEquals(SrtDelimiter.NEW_LINE_DELIMITER, srtDelimiter);
    }


}
