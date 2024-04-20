package io.github.vincemann.subtitlebuddy.gui.filechooser;

import java.io.File;

/**
 * Interface for classes that let the user choose a file.
 */
public interface FileChooser {

    File letUserChooseFile() throws UserQuitException;

}
