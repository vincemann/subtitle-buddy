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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.*;

/**
 * Component for loading srt fonts.
 * Loads font files from fonts dir (config/fonts).
 * Supports ttf and otf files.
 * For every normal font file it is expected to also find a italic font file with the same name but with the suffix "italic" before the file extension.
 */
@Slf4j
@Singleton
public class SrtFontManagerImpl implements SrtFontManager {
    private static final String ITALIC_FILE_SUFFIX = "italic";

    private String defaultFontFilename;
    @Getter
    private double userFontSize;
    @Getter
    private Color userFontColor;
    private FontsDirectory fontsDirectory;

    @Inject
    public SrtFontManagerImpl(@Named(PropertyFileKeys.USER_DEFAULT_FONT) String defaultFontFilename,
                              @Named(PropertyFileKeys.USER_FONT_SIZE) Double userFontSize,
                              @Named(PropertyFileKeys.USER_FONT_COLOR) String userFontColorString,
                              FontsDirectory fontsDirectory) {
        this.defaultFontFilename = defaultFontFilename;
        this.userFontSize = userFontSize;
        this.userFontColor = FontUtils.toColor(userFontColorString);
        this.fontsDirectory = fontsDirectory;
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
            checkNotNull(defaultFontFilename);
            checkState(fontSize>0);
            return loadFont(defaultFontFilename,fontSize);
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
     * @param fontFilename name of font file (must be within config/fonts dir)
     */
    @Override
    public SrtFonts loadFont(String fontFilename, double fontSize) throws SrtFontLoadingException {
        try {
            Font regularFont = findRegularFont(fontFilename, fontSize);
            checkNotNull(regularFont);
            Font italicFont = findItalicFont(fontFilename, fontSize);
            checkNotNull(italicFont);
            return new SrtFonts(regularFont,italicFont);
        }catch (Exception e){
            throw new SrtFontLoadingException(e);
        }
    }

    /**
     * finds italic font for given regular font file.
     * Give foo.otf and it will get foo.italic.otf
     */
    private Font findItalicFont(String fontPath, double fontSize) throws IOException, SrtFontLoadingException, FontsLocationNotFoundException {
        String fileType = FilenameUtils.getExtension(fontPath);
        String italicFontFilename = fontPath.substring(0,fontPath.length()-3)+ITALIC_FILE_SUFFIX+"."+fileType;
        log.trace("italic font filename = " + italicFontFilename);
        return resolveFont(italicFontFilename,fontSize);
    }

    /**
     * Find regular font with given name in fonts dir or as backup in classpath.
     */
    private Font findRegularFont(String fontFilename, double fontSize) throws IOException, FontsLocationNotFoundException, SrtFontLoadingException {
        return resolveFont(fontFilename,fontSize);
    }

    private Font resolveFont(String fontFilename, double fontSize) throws FontsLocationNotFoundException, FileNotFoundException, SrtFontLoadingException {
        String fileType = FilenameUtils.getExtension(fontFilename);
        log.debug("loading font with file name: " + fontFilename);
        checkArgument(fileType.equals("ttf") || fileType.equals("otf"));
        Path fontsDir = fontsDirectory.findOrCreate();
        log.debug("fonts directory: " + fontsDir.toString());
        Path fontPath = fontsDir.resolve(fontFilename).toAbsolutePath();
        log.debug("font path: " + fontPath.toString());
        File fontFile = fontPath.toFile();
        if(!fontFile.exists()){
            throw new SrtFontLoadingException("Font with name: " + fontFile + " not found");
        }
        Font font = Font.loadFont(new FileInputStream(fontFile), fontSize);
        log.debug("font loaded");
        return font;
    }

}
