package io.github.vincemann.subtitlebuddy.test.util.fx;

import io.github.vincemann.subtitlebuddy.util.fx.FontUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class FontUtilsTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"abc "+ FontUtils.FONT_SIZE_STRING_PREFIX+12.0+FontUtils.FONT_SIZE_STRING_SUFFIX+"\n some bs here",22.0,"abc "+FontUtils.FONT_SIZE_STRING_PREFIX+22.0+FontUtils.FONT_SIZE_STRING_SUFFIX+"\n some bs here"},
                {FontUtils.FONT_SIZE_STRING_PREFIX+12.0+FontUtils.FONT_SIZE_STRING_SUFFIX+"\n some bs here",22.0,FontUtils.FONT_SIZE_STRING_PREFIX+22.0+FontUtils.FONT_SIZE_STRING_SUFFIX+"\n some bs here"},
                {"abc "+FontUtils.FONT_SIZE_STRING_PREFIX+12.0+FontUtils.FONT_SIZE_STRING_SUFFIX,22.0,"abc " + FontUtils.FONT_SIZE_STRING_PREFIX+22.0+FontUtils.FONT_SIZE_STRING_SUFFIX},
                {FontUtils.FONT_SIZE_STRING_PREFIX+12.0+FontUtils.FONT_SIZE_STRING_SUFFIX,22.0,FontUtils.FONT_SIZE_STRING_PREFIX+22.0+FontUtils.FONT_SIZE_STRING_SUFFIX},
                {".custom-button {\n" +
                        "    -fx-font: 16px \"Serif\";\n" +
                        "    -fx-padding: 10;\n" +
                        "    -fx-background-color: #CCFF99;\n" +
                        "}",312.0,".custom-button {\n" +
                        "    -fx-font: 312.0px \"Serif\";\n" +
                        "    -fx-padding: 10;\n" +
                        "    -fx-background-color: #CCFF99;\n" +
                        "}"},
                {".root{\n" +
                        "    -fx-font-size: 16px;\n" +
                        "    -fx-font-family: \"Courier New\";\n" +
                        "    -fx-base: rgb(132, 145, 47);\n" +
                        "    -fx-background: rgb(22.05, 22.08, 203);\n" +
                        "}",455.0, ".root{\n" +
                        "    -fx-font-size: 455.0px;\n" +
                        "    -fx-font-family: \"Courier New\";\n" +
                        "    -fx-base: rgb(132, 145, 47);\n" +
                        "    -fx-background: rgb(22.05, 22.08, 203);\n" +
                        "}"},
                {".root{\n" +
                        "    -fx-font-size: 16pt;\n" +
                        "    -fx-font-family: \"Courier New\";\n" +
                        "    -fx-base: rgb(132, 145, 47);\n" +
                        "    -fx-background: rgb(22.05, 22.08, 203);\n" +
                        "}",455.0, ".root{\n" +
                        "    -fx-font-size: 455.0pt;\n" +
                        "    -fx-font-family: \"Courier New\";\n" +
                        "    -fx-base: rgb(132, 145, 47);\n" +
                        "    -fx-background: rgb(22.05, 22.08, 203);\n" +
                        "}"},
                {".custom-button {\n" +
                        "    -fx-font: 12.0pt \"Serif\";\n" +
                        "    -fx-padding: 10;\n" +
                        "    -fx-background-color: #CCFF99;\n" +
                        "}",235.0,".custom-button {\n" +
                        "    -fx-font: 235.0pt \"Serif\";\n" +
                        "    -fx-padding: 10;\n" +
                        "    -fx-background-color: #CCFF99;\n" +
                        "}"}




        });
    }

    private String inputStyle;
    private double newSize;
    private String outputStyle;

    public FontUtilsTest(String inputStyle, double newSize, String outputStyle) {
        this.inputStyle = inputStyle;
        this.newSize = newSize;
        this.outputStyle = outputStyle;
    }

    @Test
    public void testChangeFontSize(){
        Assert.assertEquals(outputStyle,FontUtils.resizeStyle(inputStyle,newSize));
    }

}
