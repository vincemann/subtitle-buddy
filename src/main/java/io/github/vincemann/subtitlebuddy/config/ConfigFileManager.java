package io.github.vincemann.subtitlebuddy.config;

import java.io.File;

public interface ConfigFileManager {

    File findOrCreateConfigFile(String fileName) throws ConfigFileException;
}
