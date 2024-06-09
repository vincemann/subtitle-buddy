package io.github.vincemann.subtitlebuddy.config.strings;

import com.google.inject.Singleton;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@Singleton
public class ApacheMessageSource implements MessageSource {

    private PropertiesConfiguration propertiesConfiguration;

    public ApacheMessageSource(String resourceName) throws ConfigurationException {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);
            if (inputStream == null) {
                throw new ConfigurationException("Resource not found: " + resourceName);
            }
            Reader reader = new InputStreamReader(inputStream);
            // Initialize PropertiesConfiguration and load from Reader
            this.propertiesConfiguration = new PropertiesConfiguration();
            this.propertiesConfiguration.read(reader);
        } catch (Exception e) {
            throw new ConfigurationException("Unexpected error while loading configuration resource: " + resourceName, e);
        }
    }


    /**
     * Returns the string associated with the key, default value = ""
     *
     * @param key Key whose associated value is to be returned
     * @return the value associated with the key, or "" if the key is not found
     */
    public String getString(String key) {
        return this.propertiesConfiguration.getString(key, "");
    }
}

