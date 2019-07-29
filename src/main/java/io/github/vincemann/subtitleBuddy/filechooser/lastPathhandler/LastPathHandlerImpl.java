package io.github.vincemann.subtitleBuddy.filechooser.lastPathhandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyFileKeys;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertiesFile;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyAccessException;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyNotFoundException;
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
