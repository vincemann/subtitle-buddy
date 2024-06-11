package io.github.vincemann.subtitlebuddy.test.util.string;

import io.github.vincemann.subtitlebuddy.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class LastWordCorrectTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"    Hallo ich bin ein Satz","Satz"},
                {"Hallo wie gehts?","gehts?"},
                {"__I bims 1 word", "word"},
                {"langesSoloWort", "langesSoloWort"},
                {"eyo was geht bar               ","bar"}
        });
    }

    public LastWordCorrectTest(String inputSequence, String outputFirstWord) {
        this.inputSequence = inputSequence;
        this.outputFirstWord = outputFirstWord;
    }

    private String inputSequence;
    private String outputFirstWord;

    @Test()
    public void testRightSequences(){
        Assert.assertEquals(outputFirstWord,StringUtils.findLastWord(inputSequence));
    }
}
