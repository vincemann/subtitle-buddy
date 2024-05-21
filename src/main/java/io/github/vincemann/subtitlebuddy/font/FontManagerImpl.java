package io.github.vincemann.subtitlebuddy.font;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import javafx.scene.text.Font;
import lombok.extern.log4j.Log4j2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Loads all font bundles in given {@link FontsDirectory} into {@link this#loadedFonts} and stores default and system font objects.
 * With respect to {@link FontOptions}.
 */
@Log4j2
@Singleton
public class FontManagerImpl implements FontManager {


    private FontOptions fontOptions;
    private FontsDirectory fontsDirectory;

    private FontBundleLoader fontBundleLoader;

    private List<FontBundle> loadedFonts = new ArrayList<>();

    private FontBundle currentFont;

    @Inject
    public FontManagerImpl(FontOptions fontOptions, FontsDirectory fontsDirectory, FontBundleLoader fontBundleLoader) {
        this.fontOptions = fontOptions;
        this.fontsDirectory = fontsDirectory;
        this.fontBundleLoader = fontBundleLoader;
    }

    @Override
    public void loadFonts() {
        // load all fonts from within font dir and stores in fontsLoaded list
        try {
            Path fontsDir = fontsDirectory.find();
            log.debug("loading fonts from dir: " + fontsDir.toString());
            try (Stream<Path> paths = Files.walk(fontsDir)) {
                paths
                        .filter(Files::isRegularFile)
                        .forEach(fontPath -> {
                            String fileName = fontPath.getFileName().toString();
                            //dont load italic fonts, font manger already loads regular and italic fonts, if u give him the regular font path
                            if (!fileName.contains("italic")) {
                                try {
                                    log.debug("loading font: " + fileName);
                                    FontBundle fontBundle = fontBundleLoader.loadFontBundle(fontsDir, fileName);
                                    this.loadedFonts.add(fontBundle);
                                } catch (FontBundleLoadingException e) {
                                    log.error("could not load font bundle at path: " + fontPath.toString() + ", caused by: ", e);
                                }
                            }
                        });
            }
        } catch (Exception e) {
            log.warn("could not load fonts from fonts dir", e);
        }
        loadCurrentFont();
    }

    private void loadCurrentFont() {
        log.debug("loading current font");
        List<FontBundle> matches = loadedFonts.stream()
                .filter(font -> font.getRegularFileName().equals(fontOptions.getCurrentFont()))
                .toList();
        if (matches.isEmpty()) {
            log.error("could not find current font with filename: " + fontOptions.getCurrentFont());
            log.error("using default");
            this.currentFont = new FontBundle(Font.getDefault(), Font.getDefault(), null, null);
        } else if (matches.size() == 1) {
            log.debug("found current font: ");
            this.currentFont = matches.get(0);
        } else {
            log.error("found multiple current fonts, using first");
            this.currentFont = matches.get(0);
        }

        Preconditions.checkNotNull(currentFont);
        log.debug("found current font: " + currentFont.getRegularFont().getName());
    }

    @Override
    public void reloadCurrentFont() {
        loadCurrentFont();
    }


    @Override
    public FontBundle getSystemFont() {
        return new FontBundle(Font.getDefault(), Font.getDefault(), null, null);
    }

    @Override
    public List<FontBundle> getLoadedFonts() {
        return this.loadedFonts;
    }

    @Override
    public FontBundle getCurrentFont() {
        return this.currentFont;
    }


}
