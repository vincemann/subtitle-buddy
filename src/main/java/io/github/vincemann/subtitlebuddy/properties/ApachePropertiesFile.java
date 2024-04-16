package io.github.vincemann.subtitlebuddy.properties;

import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

import java.io.File;
import java.util.NoSuchElementException;

@Slf4j
@Singleton
public class ApachePropertiesFile extends PropertiesConfiguration implements PropertiesFile {

    private File file;
    private FileHandler fileHandler;

    public ApachePropertiesFile(File file) throws ConfigurationException {
        this.file = file;
        this.setThrowExceptionOnMissing(true);
        this.setListDelimiterHandler(new DefaultListDelimiterHandler(','));

        // Initialize FileHandler to manage file loading and saving
        fileHandler = new FileHandler(this);
        fileHandler.setFile(file);
        try {
            // Load the properties file
            fileHandler.load();
        } catch (ConfigurationException e) {
            log.error("Failed to load properties file: " + file, e);
            throw e;
        }
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void refresh() {
        try {
            // Reload the properties from the file
            fileHandler.load();
            log.info("Configuration refreshed from file.");
        } catch (ConfigurationException e) {
            log.error("Failed to refresh properties file: " + file, e);
        }
    }

    @Override
    public void save() {
        try {
            // Save the properties to the file
            fileHandler.save();
            log.info("Configuration saved to file.");
        } catch (ConfigurationException e) {
            log.error("Failed to save properties file: " + file, e);
        }
    }

    @Override
    public void saveProperty(String key, Object value) throws PropertyAccessException {
        try {
            // Set property and save immediately
            setProperty(key, value);
            save();
        }catch (NoSuchElementException e){
            log.error("Failed to save property: " + key, e);
            throw new PropertyAccessException(e);
        }

    }

}
