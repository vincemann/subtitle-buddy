package io.github.vincemann.subtitlebuddy.test.srt.parser;

import io.github.vincemann.subtitlebuddy.srt.parser.SrtFileParserImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class RemoveUnsupportedTagsTest {

    private String input;

    private String expected;

    public RemoveUnsupportedTagsTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                    "<font color=\"#808080\">JAMES :</font> We're building a house on the cliff!\n",
                        "JAMES : We're building a house on the cliff!\n"
                },
                {
                    "<font color=\"#808080\">PHILLIPA :</font> Come on, Daddy!\n",
                        "PHILLIPA : Come on, Daddy!\n"
                },
                {
                        "<other=\"#808080\">PHILLIPA :</another> Come on, Daddy!\n",
                        "PHILLIPA : Come on, Daddy!\n"
                },
                {
                        "PHILLIPA :</font> Come on, Daddy!\n",
                        "PHILLIPA : Come on, Daddy!\n"
                },
                {
                        "<font color=\"#808080\"><i>JAMES :</font> We're building </i>a house on the cliff!\n",
                        "<i>JAMES : We're building </i>a house on the cliff!\n"
                },
                {
                        "<font color=\"#808080\"><i>JAMES :</font> We're building </i>a house on the cliff!<n>",
                        "<i>JAMES : We're building </i>a house on the cliff!<n>"
                },
        });
    }

    @Test
    public void testRemoveTags() {
        String result = new SrtFileParserImpl(null).removeUnsupportedTags(input);
        Assert.assertEquals(expected,result);
    }
}
