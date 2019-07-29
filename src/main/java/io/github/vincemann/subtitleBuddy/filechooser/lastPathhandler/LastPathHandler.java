package io.github.vincemann.subtitleBuddy.filechooser.lastPathhandler;

import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyAccessException;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyNotFoundException;

import java.io.File;


public interface LastPathHandler {

    String getSavedPath() throws PropertyNotFoundException;

    void savePathOfParentDir(File file) throws InvalidParentDirectoryException, PropertyAccessException;
}
