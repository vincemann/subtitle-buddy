package com.youneedsoftware.subtitleBuddy.filechooser.lastPathhandler;

import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertyAccessException;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.exception.InvalidParentDirectoryException;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertyNotFoundException;

import java.io.File;


public interface LastPathHandler {

    String getSavedPath() throws PropertyNotFoundException;

    void savePathOfParentDir(File file) throws InvalidParentDirectoryException, PropertyAccessException;
}
