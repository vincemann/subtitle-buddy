package io.github.vincemann.subtitlebuddy.config;

import java.nio.file.Path;

/**
 * Finds the directory, where the config files should be stored.
 * Differs for different os.
 */
public interface ConfigDirectory {

    Path findOrCreate() throws ConfigFileException;
}
