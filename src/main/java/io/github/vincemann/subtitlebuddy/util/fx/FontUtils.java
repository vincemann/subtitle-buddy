package io.github.vincemann.subtitlebuddy.util.fx;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class FontUtils {

    public static final String FONT_SIZE_STRING_PREFIX = "-fx-font-size: ";
    public static final String FONT_SIZE_STRING_PREFIX1 = "-fx-font: ";
    public static final String FONT_SIZE_STRING_SUFFIX = "px";
    public static final String FONT_SIZE_STRING_SUFFIX1 = "pt";

    public static String resizeStyle(String oldStyle, double newSize){
        String fontSizePrefix;
        String fontSizeSuffix;
        if(oldStyle.contains(FONT_SIZE_STRING_PREFIX)){
            fontSizePrefix = FONT_SIZE_STRING_PREFIX;
        }else if(oldStyle.contains(FONT_SIZE_STRING_PREFIX1)){
            fontSizePrefix=FONT_SIZE_STRING_PREFIX1;
        }else {
            return oldStyle+System.lineSeparator()+ FONT_SIZE_STRING_PREFIX +newSize+FONT_SIZE_STRING_SUFFIX;
        }

        if(oldStyle.contains(FONT_SIZE_STRING_SUFFIX)){
            fontSizeSuffix=FONT_SIZE_STRING_SUFFIX;
        }else if(oldStyle.contains(FONT_SIZE_STRING_SUFFIX1)){
            fontSizeSuffix=FONT_SIZE_STRING_SUFFIX1;
        }else {
            throw new IllegalArgumentException("No valid Suffix found");
        }


        int lastIndexPrefix = oldStyle.lastIndexOf(fontSizePrefix);
        int index = lastIndexPrefix-1;
        if(index<0){
            index=0;
        }
        String substring = ((oldStyle.substring(index)));
        int lastIndexSuffix = substring.indexOf(fontSizeSuffix)+(oldStyle.length()-substring.length());

        checkArgument(lastIndexPrefix>=0);
        checkArgument(lastIndexSuffix>=0);
        checkArgument(lastIndexPrefix+fontSizeSuffix.length()+fontSizePrefix.length()<=oldStyle.length());

        String firstPart = oldStyle.substring(0,lastIndexPrefix+fontSizePrefix.length());
        String secondPart = oldStyle.substring(lastIndexSuffix);

        return firstPart+newSize+secondPart;
    }

    public static void adjustTextSize(Text text, int fontSize) {
        if (text.getFont().getSize() != fontSize) {
            String newStyle = FontUtils.resizeStyle(text.getStyle(), fontSize);
            text.setStyle(newStyle);
        }
    }

    /**
     * Translates hex rgb color string to {@link Color} object.
     * Returns default color white, if fails.
     */
    public static Color toColor(String colorString){
        try {
            return javafx.scene.paint.Color.valueOf(colorString);
        }catch (Exception e){
            log.warn("user color string: " + colorString + " was not identified -> using white",e);
            return javafx.scene.paint.Color.WHITE;
        }
    }
}
