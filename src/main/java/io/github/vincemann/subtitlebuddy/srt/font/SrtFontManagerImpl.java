package io.github.vincemann.subtitlebuddy.srt.font;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.config.properties.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileLocator;
import io.github.vincemann.subtitlebuddy.srt.SrtFonts;
import io.github.vincemann.subtitlebuddy.util.fx.FontUtils;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.*;

/**
 * Component for loading srt fonts.
 * Loads font files from classpath.
 * Supports ttf and otf files.
 * For every normal font file it is expected to also find a italic font file with the same name but with the suffix "italic" before the file extension.
 */
@Log4j
@Singleton
public class SrtFontManagerImpl implements SrtFontManager {
    private static final String ITALIC_FILE_SUFFIX = "italic";

    private String userFontPath;
    @Getter
    private double userFontSize;
    @Getter
    private Color userFontColor;
    private ClassPathFileLocator classPathFileLocator;

    @Inject
    public SrtFontManagerImpl(@Named(PropertyFileKeys.USER_DEFAULT_FONT_PATH) String userFontPath,
                              @Named(PropertyFileKeys.USER_FONT_SIZE) Double userFontSize,
                              @Named(PropertyFileKeys.USER_FONT_COLOR) String userFontColorString,
                              ClassPathFileLocator classPathFileLocator) {
        this.userFontPath = userFontPath;
        this.userFontSize = userFontSize;
        this.userFontColor = FontUtils.toColor(userFontColorString);
        this.classPathFileLocator = classPathFileLocator;
    }


    @Override
    public SrtFonts loadDefaultFont() {
        return loadDefaultFont(userFontSize);
    }

    /**
     * Loads default font with size of {@link #userFontSize} from default font path specified by user via config file.
     */
    @Override
    public SrtFonts loadDefaultFont(double fontSize) {
        try {
            checkNotNull(userFontPath);
            checkState(fontSize>0);
            return loadFont(userFontPath,fontSize);
        } catch (Exception e) {
            log.error("Could not load applications default font, using System default Font instead ", e);
            return new SrtFonts(Font.getDefault(),Font.getDefault());
        }
    }

    @Override
    public void setFontColor(Color color) {
        this.userFontColor=color;
    }

    @Override
    public Color getFontColor() {
        return this.userFontColor;
    }

    /**
     * Find srt fonts with given path and size.
     */
    @Override
    public SrtFonts loadFont(String fontPath, double fontSize) throws SrtFontLoadingException {
        try {
            Font regularFont = findRegularFont(fontPath, fontSize);
            checkNotNull(regularFont);
            Font italicFont = findItalicFont(fontPath, fontSize);
            checkNotNull(italicFont);
            return new SrtFonts(regularFont,italicFont);
        }catch (Exception e){
            throw new SrtFontLoadingException(e);
        }
    }

    private Font findItalicFont(String fontPath, double fontSize) throws IOException, SrtFontLoadingException {
        String fileType = FilenameUtils.getExtension(fontPath);
        String italicFontPath = fontPath.substring(0,fontPath.length()-3)+ITALIC_FILE_SUFFIX+"."+fileType;
        log.trace("italicFontName = " + italicFontPath);
        File italicFontFile;
        if(Paths.get(fontPath).isAbsolute()){
            italicFontFile = new File(italicFontPath);
        }else {
            italicFontFile = classPathFileLocator.findOnClassPath(italicFontPath).getFile();
        }
        if(!italicFontFile.exists()){
            throw new SrtFontLoadingException("Font with name: " + italicFontPath + " not found");
        }
        Font italicFont = Font.loadFont(new FileInputStream(italicFontFile),fontSize);
        return italicFont;
    }

    private Font findRegularFont(String fontPath, double fontSize) throws IOException {
        String fileType = FilenameUtils.getExtension(fontPath);
        log.trace("fileType : " + fileType + " of filepath: " + fontPath);
        checkArgument(fileType.equals("ttf") || fileType.equals("otf"));
        File fontFile;
        if(Paths.get(fontPath).isAbsolute()){
            fontFile = new File(fontPath);
        }else {
            fontFile= classPathFileLocator.findOnClassPath(fontPath).getFile();
        }
        Font regularFont = Font.loadFont(new FileInputStream(fontFile),fontSize);
        return regularFont;
    }

}
