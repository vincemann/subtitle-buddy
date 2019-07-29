package io.github.vincemann.subtitleBuddy.config.uiStringsFile;

import com.google.inject.Singleton;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;

@Singleton
public class ApacheUIStringsFile implements UIStringsFile {

    private PropertiesConfiguration propertiesConfiguration;

    public ApacheUIStringsFile(String fileName) throws ConfigurationException {
        this.propertiesConfiguration = new PropertiesConfiguration(fileName);
    }

    public ApacheUIStringsFile(File file) throws ConfigurationException {
        this.propertiesConfiguration = new PropertiesConfiguration(file);
    }

    /**
     * returns string of key, default value = ""
     * @param key
     * @return
     */
    public String getString(String key){
        return this.propertiesConfiguration.getString(key,"");
    }
}
