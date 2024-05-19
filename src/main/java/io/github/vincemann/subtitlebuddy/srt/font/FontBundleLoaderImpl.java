package io.github.vincemann.subtitlebuddy.srt.font;

import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import javafx.scene.text.Font;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

@Log4j2
public class FontBundleLoaderImpl implements FontBundleLoader {

    private static final String ITALIC_FILE_SUFFIX = "italic";
    @Setter
    private static Integer DEFAULT_FONT_SIZE = 12;

    /**
     * Find fonts with given path and size.
     *
     * @param fontFilename name of font file (must be within config/fonts dir)
     */
    @Override
    public FontBundle loadFontBundle(Path dir, String fontFilename) throws FontBundleLoadingException {
        try {
            LoadedFont regularFont = loadRegularFont(dir,fontFilename, DEFAULT_FONT_SIZE);
            checkNotNull(regularFont);
            LoadedFont italicFont = loadItalicFont(dir,fontFilename, DEFAULT_FONT_SIZE);
            checkNotNull(italicFont);
            return new FontBundle(
                    regularFont.getFont(),
                    italicFont.getFont(),
                    regularFont.getFilename(),
                    italicFont.getFilename()
            );
        } catch (Exception e) {
            throw new FontBundleLoadingException(e);
        }
    }

    /**
     * finds italic font for given regular font file.
     * Give foo.otf and it will get foo.italic.otf
     */
    private LoadedFont loadItalicFont(Path dir, String fileName, double fontSize) throws IOException, FontBundleLoadingException {
        String fileType = FilenameUtils.getExtension(fileName);
        String italicFontFilename = fileName.substring(0, fileName.length() - 3) + ITALIC_FILE_SUFFIX + "." + fileType;
        log.trace("italic font filename = " + italicFontFilename);
        return _loadFont(dir,italicFontFilename, fontSize);
    }

    /**
     * Find regular font with given name in fonts dir or as backup in classpath.
     */
    private LoadedFont loadRegularFont(Path dir, String fontFilename, double fontSize) throws IOException, FontBundleLoadingException {
        return _loadFont(dir,fontFilename, fontSize);
    }


    @AllArgsConstructor
    @Getter
    private static class LoadedFont {
        private Font font;
        private String filename;
    }

    private LoadedFont _loadFont(Path fontsDir, String fontFilename, double fontSize) throws IOException, FontBundleLoadingException {
        Path fontPath = fontsDir.resolve(fontFilename).toAbsolutePath();
        File fontFile = fontPath.toFile();
        if (!fontFile.exists()) {
            throw new FontBundleLoadingException("Font with name: " + fontFile + " not found");
        }
        Font font = Font.loadFont(new FileInputStream(fontFile), fontSize);
        log.debug("font loaded");
        return new LoadedFont(font, fontFilename);
    }
}
