package io.github.vincemann.subtitleBuddy.config.configFileManager;

import java.io.File;

public interface ConfigFileManager {

    /**
     * Finds the ConfigFile with name {@param fileName}.
     * ConfigFile must be writable.
     * @return  FileObject representing ConfigFile
     */
    public File findConfigFile(String fileName) throws ConfigFileManagerException;
}
