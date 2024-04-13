package io.github.vincemann.subtitlebuddy.srt.font;


import java.nio.file.Path;

public interface FontsDirectoryManager {


    /**
     * Finds or creates the font directory and populates it with the fonts from the {@code userFontPath}.
     * @param userFontPath dir specified by user where fonts are located
     * @return path to font directory
     */
    Path findOrCreateFontDirectory(Path userFontPath) throws FontsLocationNotFoundException;


}
