package io.github.vincemann.subtitlebuddy.config;

import com.google.inject.Singleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Singleton
public class ConfigDirectoryImpl implements ConfigDirectory {

    private Path configDir;

    @Override
    public Path create() throws IOException {
        configDir = Files.createDirectories(getAppDataDirectory());
        return configDir;
    }

    @Override
    public Path find() throws IOException {
        if (configDir == null)
            configDir = create();
        return configDir;
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
