package io.github.vincemann.subtitlebuddy.srt.font;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectory;
import io.github.vincemann.subtitlebuddy.config.ConfigFileException;
import io.github.vincemann.subtitlebuddy.cp.CopiedClassPathFile;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Component used to find or create folder in which fonts reside.
 * User can specify the path to his font folder.
 * If user dir does not exist, create it and fill it with default fonts.
 * If that fails, create /path/to/jar/fonts and fill it with default fonts.
 * Default fonts are loaded from file inside jar.
 */
@Slf4j
@Singleton
public class FontsDirectoryImpl implements FontsDirectory {

    // relative to config dir
    private static final String DEFAULT_FONT_PATH = "fonts";
    private static final String DEFAULT_FONT_FILES_PATTERN = "/fonts/*";

    private ConfigDirectory configDirectory;
    private ClassPathFileExtractor classPathFileExtractor;

    @Inject
    public FontsDirectoryImpl(ConfigDirectory configDirectory, ClassPathFileExtractor classPathFileExtractor) {
        this.configDirectory = configDirectory;
        this.classPathFileExtractor = classPathFileExtractor;
    }

    @Override
    public Path findOrCreate() throws FontsLocationNotFoundException {
        try {
            Path configDir = configDirectory.findOrCreate();
            Path fontsDir = configDir.resolve(DEFAULT_FONT_PATH).toAbsolutePath();
            if (!fontsDir.toFile().exists()) {
                log.warn("font directory" + fontsDir + " does not exist");
                log.debug("creating it for user");
                Files.createDirectory(fontsDir);
                log.debug("font dir successfully created");
                log.debug( "adding default fonts now if necessary");
                populateWithDefaultFontsIfNeeded(fontsDir);
            }else {
                log.debug("font dir already existing");
            }

            log.debug("selected fonts path: " + fontsDir.toString());
            return fontsDir;
        } catch (IOException | ConfigFileException e) {
            throw new FontsLocationNotFoundException(e);
        }
    }


    /**
     * Copies default fonts from jar to {@code path} if they are not already there.
     */
    private void populateWithDefaultFontsIfNeeded(Path targetPath) throws IOException {
        // get font files from jar
        List<CopiedClassPathFile> fontFiles = classPathFileExtractor.findAllInDir(DEFAULT_FONT_FILES_PATTERN);
        for (CopiedClassPathFile tmpFontFile : fontFiles) {
            //every file
            File targetFontFile = targetPath.resolve(tmpFontFile.getFileName()).toFile();
            if (targetFontFile.exists()) {
                log.warn("file: " + targetFontFile.getAbsolutePath() + " already exists, skipping");
                continue;
            }
            FileUtils.copyFile(tmpFontFile.getFile(), targetFontFile);
        }
    }
}
