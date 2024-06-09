package io.github.vincemann.subtitlebuddy.font;


import java.io.IOException;
import java.nio.file.Path;

/**
 * Finds or creates the font directory and populates it with the fonts from the {@code userFontPath}.
 */
public interface FontsDirectory {


    Path create(Path target) throws IOException;

    Path find() throws IOException;
}
