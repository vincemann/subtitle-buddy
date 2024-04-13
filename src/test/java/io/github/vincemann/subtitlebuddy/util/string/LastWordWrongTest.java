package io.github.vincemann.subtitlebuddy.util.string;

import io.github.vincemann.subtitlebuddy.util.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class LastWordWrongTest {


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {null},
                {" "},
                {"                                                   "}
        });
    }

    public LastWordWrongTest(String inputSequence) {
        this.inputSequence = inputSequence;
    }

    private String inputSequence;

    @Test(expected = IllegalArgumentException.class)
    public void testRightSequences(){
        StringUtils.findLastWord(inputSequence);
    }
}
