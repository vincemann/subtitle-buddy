package io.github.vincemann.subtitlebuddy.config.strings;

import com.google.inject.Singleton;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

@Singleton
public class ApacheUIStringsFile implements UIStringsFile {

    private PropertiesConfiguration propertiesConfiguration;

    public ApacheUIStringsFile(String fileName) throws ConfigurationException {
        this(new File(fileName));
    }

    public ApacheUIStringsFile(File file) throws ConfigurationException {
        Configurations configs = new Configurations();
        try {
            // Set up the builder for PropertiesConfiguration and initialize it with the file
            FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                    configs.propertiesBuilder(file);

            // Obtain the configuration and assign it
            this.propertiesConfiguration = builder.getConfiguration();
        } catch (ConfigurationException cex) {
            throw new ConfigurationException("Failed to load configuration file: " + file, cex);
        }
    }

    /**
     * Returns the string associated with the key, default value = ""
     * @param key Key whose associated value is to be returned
     * @return the value associated with the key, or "" if the key is not found
     */
    public String getString(String key){
        return this.propertiesConfiguration.getString(key, "");
    }
}

