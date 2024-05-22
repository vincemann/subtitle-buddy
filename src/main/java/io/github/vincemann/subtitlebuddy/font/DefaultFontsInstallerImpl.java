package io.github.vincemann.subtitlebuddy.font;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractor;
import io.github.vincemann.subtitlebuddy.cp.CopiedClassPathFile;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Extracts default fronts from within jar into target dir.
 */
@Log4j2
@Singleton
public class DefaultFontsInstallerImpl implements DefaultFontsInstaller {


    private static final List<String> DEFAULT_FONT_FILES = Arrays.asList(
            "fonts/Aileron-Black.italic.otf",
            "fonts/Aileron-Black.otf",
            "fonts/Amaranth.ttf",
            "fonts/Amaranth.italic.ttf",
            "fonts/Montserrat-Medium.otf",
            "fonts/Montserrat-Medium.italic.otf",
            "fonts/Quicksand.otf",
            "fonts/Quicksand.italic.otf",
            "fonts/Roboto-Black.ttf",
            "fonts/Roboto-Black.italic.ttf"
    );

    private ClassPathFileExtractor classPathFileExtractor;

    @Inject
    public DefaultFontsInstallerImpl(ClassPathFileExtractor classPathFileExtractor) {
        this.classPathFileExtractor = classPathFileExtractor;
    }

    /**
     * Copies default fonts from jar to {@code path} if they are not already there.
     */
    @Override
    public void installIfNeeded(Path fontDir) throws IOException {
        // get font files from jar
        if (io.github.vincemann.subtitlebuddy.util.FileUtils.isDirectoryEmpty(fontDir)) {
            log.debug("font dir is empty");
            log.debug("installing default fonts...");
            List<CopiedClassPathFile> fontFiles = extractDefaultFonts();
            for (CopiedClassPathFile tmpFontFile : fontFiles) {
                //every file
                File targetFontFile = fontDir.resolve(tmpFontFile.getFileName()).toFile();
                if (targetFontFile.exists()) {
                    log.warn("file: " + targetFontFile.getAbsolutePath() + " already exists, skipping");
                    continue;
                }
                FileUtils.copyFile(tmpFontFile.getFile(), targetFontFile);
            }
        } else {
            log.debug("font dir is not empty");
        }
        log.debug("default fonts installed");
    }

    // todo just read the default fonts as stream and write to font dir without using file objects
    private List<CopiedClassPathFile> extractDefaultFonts() throws IOException {
        List<CopiedClassPathFile> defaultFonts = Lists.newArrayList();
        for (String defaultFontFile : DEFAULT_FONT_FILES) {
            defaultFonts.add(classPathFileExtractor.findOnClassPath(defaultFontFile));
        }
        return defaultFonts;
    }


}
