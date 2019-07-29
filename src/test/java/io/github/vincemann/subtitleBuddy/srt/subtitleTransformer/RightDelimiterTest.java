package io.github.vincemann.subtitleBuddy.srt.subtitleTransformer;

import io.github.vincemann.subtitleBuddy.srt.SrtDelimiterType;
import io.github.vincemann.subtitleBuddy.srt.subtitleTransformer.InvalidDelimiterException;
import io.github.vincemann.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformerImpl;
import org.junit.Assert;
import org.junit.Test;

public class RightDelimiterTest {

    @Test
    public void testFindItalicStartDelimiterType() throws InvalidDelimiterException {
        String testData = "<i>alksmfalksdm";
        SrtDelimiterType srtDelimiterType = SrtFileTransformerImpl.findDelimiterType(testData);
        Assert.assertEquals(SrtDelimiterType.ITALIC_START_DELIMITER,srtDelimiterType);
    }

    @Test
    public void testFindItalicEndDelimiterType() throws InvalidDelimiterException {
        String testData = "</i>p,ewfpos,földs";
        SrtDelimiterType srtDelimiterType = SrtFileTransformerImpl.findDelimiterType(testData);
        Assert.assertEquals(SrtDelimiterType.ITALIC_END_DELIMITER,srtDelimiterType);
    }

    @Test
    public void testFindNewLineDelimiterType() throws InvalidDelimiterException {
        String testData = "<n>asölfma,ös,";
        SrtDelimiterType srtDelimiterType = SrtFileTransformerImpl.findDelimiterType(testData);
        Assert.assertEquals(SrtDelimiterType.NEW_LINE_DELIMITER,srtDelimiterType);
    }


}
