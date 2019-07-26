package util.stringUtils;

import com.youneedsoftware.subtitleBuddy.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class LastWordRightTest {

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

    public LastWordRightTest(String inputSequence, String outputFirstWord) {
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
