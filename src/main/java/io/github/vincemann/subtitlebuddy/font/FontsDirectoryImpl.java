package io.github.vincemann.subtitlebuddy.font;

import com.google.inject.Singleton;
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
    private Path dir;


    @Override
    public Path create(Path target) throws IOException {
        Path fontsDir = target.resolve(fontPath).toAbsolutePath();
        if (!fontsDir.toFile().exists()) {
            log.warn("font directory" + fontsDir + " does not exist");
            log.debug("creating it for user");
            dir = Files.createDirectory(fontsDir);
            log.debug("font dir successfully created");
        } else {
            log.debug("font dir already existing");
            dir = fontsDir;
        }
        log.debug("selected fonts path: " + fontsDir.toString());
        return dir;
    }

    @Override
    public Path find() throws IOException {
       if (dir == null)
           throw new IllegalStateException("Need to call create at least once before finding");

       return dir;
    }



}
