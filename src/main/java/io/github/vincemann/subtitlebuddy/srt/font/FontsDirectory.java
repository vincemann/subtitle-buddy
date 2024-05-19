package io.github.vincemann.subtitlebuddy.srt.font;


import java.io.IOException;
import java.nio.file.Path;

/**
 * Finds or creates the font directory and populates it with the fonts from the {@code userFontPath}.
 */
public interface FontsDirectory {


    Path findOrCreate() throws IOException;
}
