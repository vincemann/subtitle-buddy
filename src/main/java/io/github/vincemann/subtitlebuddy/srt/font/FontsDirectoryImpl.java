package io.github.vincemann.subtitlebuddy.srt.font;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.config.ConfigDirectory;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Component used to find or create folder in which fonts reside relative to given path.
 * If user dir does not exist, create it.
 */
@Log4j2
@Singleton
public class FontsDirectoryImpl implements FontsDirectory {

    // relative to config dir
    @Setter
    private String fontPath = "fonts";
    private ConfigDirectory configDirectory;

    @Inject
    public FontsDirectoryImpl(ConfigDirectory configDirectory) {
        this.configDirectory = configDirectory;
    }

    @Override
    public Path findOrCreate() throws IOException {
        Path configDir = configDirectory.findOrCreate();
        Path fontsDir = configDir.resolve(fontPath).toAbsolutePath();
        if (!fontsDir.toFile().exists()) {
            log.warn("font directory" + fontsDir + " does not exist");
            log.debug("creating it for user");
            Files.createDirectory(fontsDir);
            log.debug("font dir successfully created");
        } else {
            log.debug("font dir already existing");
        }
        log.debug("selected fonts path: " + fontsDir.toString());
        return fontsDir;
    }


}
