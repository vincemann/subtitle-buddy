package com.youneedsoftware.subtitleBuddy.filechooser.lastPathhandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertyFileKeys;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertiesFile;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertyAccessException;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.exception.InvalidParentDirectoryException;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertyNotFoundException;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.util.NoSuchElementException;

@Log4j
@Singleton
public class LastPathHandlerImpl implements LastPathHandler {

    private PropertiesFile configuration;

    @Inject
    public LastPathHandlerImpl(PropertiesFile configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getSavedPath() {
        try {
            return configuration.getString(PropertyFileKeys.LAST_PATH_KEY);
        }catch (NoSuchElementException e){
            throw new PropertyNotFoundException(e);
        }
    }

    @Override
    public void savePathOfParentDir(File file) throws InvalidParentDirectoryException, PropertyAccessException {
        String parentDir;
        if (file.getParent() != null) {
            parentDir = file.getParent();
            File parentFile = file.getParentFile();
            if (!parentFile.isDirectory()) {
                throw new InvalidParentDirectoryException("parentDir was no directory");
            }
        } else {
            throw new InvalidParentDirectoryException("ParentDir of file was null");
        }
        log.trace("saving path: " + parentDir);

        configuration.saveProperty(PropertyFileKeys.LAST_PATH_KEY, parentDir);
        log.debug("Successfully saved path: " + parentDir);

    }
}
