package io.github.vincemann.subtitlebuddy.srt.font;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.options.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.util.fx.FontUtils;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.*;

/**
 * Component for loading srt fonts.
 * Loads font files from fonts dir (config/fonts).
 * Supports ttf and otf files.
 * For every normal font file it is expected to also find a italic font file with the same name but with the suffix "italic" before the file extension.
 */
@Log4j2
@Singleton
public class SrtFontManagerImpl implements SrtFontManager {

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
    public FontBundle loadDefaultFont() {
        return loadDefaultFont(userFontSize);
    }

    /**
     * Loads default font with size of {@link #userFontSize} from default font path specified by user via config file.
     */
    @Override
    public FontBundle loadDefaultFont(double fontSize) {
        try {
            checkNotNull(defaultFontFilename);
            checkState(fontSize>0);
            return loadFont(defaultFontFilename,fontSize);
        } catch (Exception e) {
            log.error("Could not load applications default font, using System default Font instead ", e);
            return new FontBundle(Font.getDefault(),Font.getDefault());
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




}
