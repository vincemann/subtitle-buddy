package com.youneedsoftware.subtitleBuddy.filechooser;

import java.io.File;

public interface FileChooser {
    /**
     * stopjob
     * lets the user choose a file via gui
     * may return null if something went wrong
     * @return
     */
    public File letUserChooseFile() throws UserQuitException;

}
