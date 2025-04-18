package io.github.vincemann.subtitlebuddy.config;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Finds and creates the directory, where the config files should be stored.
 * Differs for different os.
 */
public interface ConfigDirectory {


    Path create() throws IOException;
    Path find() throws IOException;
}
