package io.github.vincemann.subtitlebuddy.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigDirectoryImpl implements ConfigDirectory {
    @Override
    public Path findOrCreate() throws ConfigFileException {
        Path appDirectoryPath = getAppDataDirectory();
        try {
            // The Files.createDirectories method creates the directory only if it does not exist,
            // and does nothing if the directory already exists, which removes the need for the existence check.
            Files.createDirectories(appDirectoryPath);
        } catch (IOException e) {
            throw new ConfigFileException("Failed to create config directory: " + appDirectoryPath, e);
        }
        return appDirectoryPath;
    }

    private Path getAppDataDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");
        if (os.contains("win")) {
            return Paths.get(System.getenv("APPDATA"), "subtitle-buddy");
        } else {
            return Paths.get(userHome, ".subtitle-buddy");
        }
    }
}