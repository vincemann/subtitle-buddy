package io.github.vincemann.subtitlebuddy.srt.font;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectory;
import io.github.vincemann.subtitlebuddy.config.ConfigFileException;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFile;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileLocator;
import lombok.extern.log4j.Log4j;
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
@Log4j
@Singleton
public class FontsDirectoryManagerImpl implements FontsDirectoryManager {

    // relative to jar
    private static final String DEFAULT_FONT_PATH = "fonts";
    private static final String DEFAULT_FONT_FILES_PATTERN = "/fonts/*";

    private ConfigDirectory configDirectory;
    private ClassPathFileLocator classPathFileLocator;

    @Inject
    public FontsDirectoryManagerImpl(ConfigDirectory configDirectory, ClassPathFileLocator classPathFileLocator) {
        this.configDirectory = configDirectory;
        this.classPathFileLocator = classPathFileLocator;
    }

    @Override
    public Path findOrCreateFontDirectory(Path userFontPath) throws FontsLocationNotFoundException {
        try {
            Path absFontPath = userFontPath.toAbsolutePath();
            if (!absFontPath.toFile().exists()) {
                log.warn("user font directory" + absFontPath + " does not exist");
                log.debug("creating it for user");
                try {
                    Files.createDirectory(userFontPath);
                    log.debug("user font dir successfully created");
                    absFontPath=userFontPath;
                }catch (Exception e){
                    log.warn("could not create dir at user font dir path",e);
                    Path executablePath = configDirectory.findOrCreate();
                    Path defaultFontsDirPath = executablePath.resolve(DEFAULT_FONT_PATH).toAbsolutePath();
                    log.warn("using default font directory instead: " + defaultFontsDirPath.toString());
                    if (defaultFontsDirPath.toFile().exists() && defaultFontsDirPath.toFile().isDirectory()) {
                        log.debug("default font directory already exists");
                        absFontPath = defaultFontsDirPath;
                    } else {
                        log.debug("default font directory does not exist, creating now");
                        Files.createDirectory(defaultFontsDirPath);
                        absFontPath = defaultFontsDirPath;
                    }
                }

                log.debug( "adding default fonts now if necessary");
                populateWithDefaultFontsIfNeeded(absFontPath);
            }else {
                log.debug("user font dir exists!");
            }

            log.debug("selected fonts path: " + absFontPath.toString());
            return absFontPath;
        } catch (IOException | ConfigFileException e) {
            throw new FontsLocationNotFoundException(e);
        }
    }


    /**
     * Copies default fonts from jar to {@code path} if they are not already there.
     */
    private void populateWithDefaultFontsIfNeeded(Path targetPath) throws IOException {
        // get font files from jar
        List<ClassPathFile> fontFiles = classPathFileLocator.findAllInDir(DEFAULT_FONT_FILES_PATTERN);
        for (ClassPathFile tmpFontFile : fontFiles) {
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
