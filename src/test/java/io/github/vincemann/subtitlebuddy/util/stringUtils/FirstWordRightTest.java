package io.github.vincemann.subtitlebuddy.util.stringUtils;

import io.github.vincemann.subtitlebuddy.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class FirstWordRightTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"    Hallo ich bin ein Satz","Hallo"},
                {"Hallo wie gehts?","Hallo"},
                {"__I bims 1 word", "__I"},
                {"langesSoloWort", "langesSoloWort"}
        });
    }

    public FirstWordRightTest(String inputSequence, String outputFirstWord) {
        this.inputSequence = inputSequence;
        this.outputFirstWord = outputFirstWord;
    }

    private String inputSequence;
    private String outputFirstWord;

    @Test()
    public void testRightSequences(){
        Assert.assertEquals(outputFirstWord,StringUtils.findFirstWord(inputSequence));
    }
}
