package io.github.vincemann.subtitlebuddy.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigDirectoryImpl implements ConfigDirectory {

    @Override
    public void create() throws IOException {
        Files.createDirectories(getAppDataDirectory());
    }

    @Override
    public Path find() throws IOException {
        Path appDirectoryPath = getAppDataDirectory();
        // The Files.createDirectories method creates the directory only if it does not exist,
        // and does nothing if the directory already exists, which removes the need for the existence check.
        Files.createDirectories(appDirectoryPath);
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
