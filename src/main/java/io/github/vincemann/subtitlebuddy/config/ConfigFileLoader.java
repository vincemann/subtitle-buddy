package io.github.vincemann.subtitlebuddy.config;

import java.io.File;

public interface ConfigFileLoader {

    File findOrCreateConfigFile(String fileName) throws ConfigFileException;
}
