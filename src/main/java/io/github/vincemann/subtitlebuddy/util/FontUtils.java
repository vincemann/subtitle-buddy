package io.github.vincemann.subtitlebuddy.util;

import javafx.scene.text.Font;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

@Log4j2
public class FontUtils {

    public static Font load(Path fontPath, double fontSize) throws IOException {
        log.debug("loading font: " + fontPath);
        File fontFile = fontPath.toFile();
        if(!fontFile.exists()){
            throw new FileNotFoundException("Font with name: " + fontFile + " not found");
        }
        Font font = Font.loadFont(new FileInputStream(fontFile), fontSize);
        log.debug("font loaded");
        return font;
    }

}
