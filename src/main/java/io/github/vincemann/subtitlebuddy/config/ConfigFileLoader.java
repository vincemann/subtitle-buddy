package io.github.vincemann.subtitlebuddy.config;

import java.io.File;
import java.io.IOException;

public interface ConfigFileLoader {

    File findOrCreateConfigFile(String fileName) throws IOException;
}
