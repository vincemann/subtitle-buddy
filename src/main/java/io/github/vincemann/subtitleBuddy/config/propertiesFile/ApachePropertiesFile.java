package io.github.vincemann.subtitleBuddy.config.propertiesFile;

import com.google.inject.Singleton;
import lombok.extern.log4j.Log4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;

@Log4j
@Singleton
public class ApachePropertiesFile extends PropertiesConfiguration implements PropertiesFile {


    public ApachePropertiesFile(File file) throws ConfigurationException {
        super(file);
        this.setThrowExceptionOnMissing(true);
    }

    public ApachePropertiesFile(String fileName) throws ConfigurationException {
        super(fileName);
        this.setThrowExceptionOnMissing(true);
    }

    @Override
    public void save() throws PropertyAccessException {
        try {
            super.save();
        } catch (ConfigurationException e) {
            throw new PropertyAccessException(e);
        }
    }

    @Override
    public void refresh() throws PropertyAccessException {
        try {
            super.refresh();
        } catch (ConfigurationException e) {
            throw new PropertyAccessException(e);
        }
    }


    @Override
    public void saveProperty(String key, Object value) throws PropertyAccessException {
        setProperty(key, value);
        save();
    }


}
