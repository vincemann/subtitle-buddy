package io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath;

import io.github.vincemann.subtitlebuddy.options.PropertyAccessException;
import io.github.vincemann.subtitlebuddy.options.PropertyNotFoundException;

import java.io.File;


public interface LastPathRegistry {

    String getSavedPath() throws PropertyNotFoundException;

    void savePath(File file) throws PropertyAccessException;
}
