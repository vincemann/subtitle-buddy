package io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.config.properties.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.config.properties.PropertiesFile;
import io.github.vincemann.subtitlebuddy.config.properties.PropertyAccessException;
import io.github.vincemann.subtitlebuddy.config.properties.PropertyNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.NoSuchElementException;

/**
 * Component for saving and retrieving the last path the user navigated to,
 * so the user does not have to navigate to the same path again.
 *
 * This impl saves to {@link PropertiesFile}.
 */
@Slf4j
@Singleton
public class PropertiesFileLastPathRegistry implements LastPathRegistry {

    private PropertiesFile configuration;

    @Inject
    public PropertiesFileLastPathRegistry(PropertiesFile configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getSavedPath() {
        try {
            return configuration.getString(PropertyFileKeys.LAST_PATH);
        }catch (NoSuchElementException e){
            throw new PropertyNotFoundException(e);
        }
    }

    @Override
    public void savePath(File file) throws PropertyAccessException {
        log.trace("saving path: " + file);
        String path = file.getPath();
        configuration.saveProperty(PropertyFileKeys.LAST_PATH, path);
        log.debug("Successfully saved path: " + path);

    }
}
