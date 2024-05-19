package io.github.vincemann.subtitlebuddy.srt.font;

import com.google.inject.Inject;
import io.github.vincemann.subtitlebuddy.options.FontOptions;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Loads all font bundles in given {@link FontsDirectory} into {@link this#loadedFonts} and stores default and system font objects.
 * With respect to {@link FontOptions}.
 */
@Log4j2
public class FontManagerImpl implements FontManager {


    private FontOptions fontOptions;
    private FontsDirectory fontsDirectory;

    private FontBundleLoader fontBundleLoader;

    private List<FontBundle> loadedFonts = new ArrayList<>();

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
            Path fontsDirPath = fontsDirectory.findOrCreate();
            log.debug("using font path: " + fontsDirPath.toString());
            try (Stream<Path> paths = Files.walk(fontsDirPath)) {
                paths
                        .filter(Files::isRegularFile)
                        .forEach(path -> {
                            // path is relative to fonts dir (config/fonts)
                            String fontFileRelPath = path.toAbsolutePath().toString();
                            //dont load italic fonts, font manger already loads regular and italic fonts, if u give him the regular font path
                            if (!Paths.get(fontFileRelPath).getFileName().toString().contains("italic")) {
                                try {
                                    Path fontsDir = fontsDirectory.findOrCreate();
                                    FontBundle fontBundle = fontBundleLoader.loadFontBundle(fontsDir, fontFileRelPath);
                                    this.loadedFonts.add(fontBundle);
                                } catch (FontBundleLoadingException | IOException e) {
                                    log.error("could not load font (or respective italic font) with fontpath: " + fontFileRelPath + ", caused by: ", e);
                                }
                            }
                        });
            }
        } catch (Exception e) {
            log.warn("could not load fonts from fonts dir", e);
        }
    }


    @Override
    public FontBundle getSystemFont() {
        return null;
    }

    @Override
    public List<FontBundle> getLoadedFonts() {
        return null;
    }

    @Override
    public FontBundle getCurrentFont() {
        return null;
    }


}
