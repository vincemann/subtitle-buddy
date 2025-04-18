package io.github.vincemann.subtitlebuddy.config;

import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractor;
import io.github.vincemann.subtitlebuddy.cp.CopiedClassPathFile;
import io.github.vincemann.subtitlebuddy.util.FileUtils;
import lombok.extern.log4j.Log4j2;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Component for loading config file.
 * Original config file is within jar (on classpath).
 * This class extracts it to {@link ConfigDirectory#find()} location, so the user can easily edit it.
 * If config file is already in config target folder, it is just loaded from there.
 */
@Singleton
@Log4j2
public class ConfigFileLoaderImpl implements ConfigFileLoader {

    private ConfigDirectory configDirectory;
    private ClassPathFileExtractor classPathFileExtractor;

    @Inject
    public ConfigFileLoaderImpl(ConfigDirectory configDirectory, ClassPathFileExtractor classPathFileExtractor) {
        this.configDirectory = configDirectory;
        this.classPathFileExtractor = classPathFileExtractor;
    }

    @Override
    public File findOrCreateConfigFile(String fileName) throws IOException {
        // first check if config file is already extracted into application folder
        Path configFolder = configDirectory.find();
        log.debug("config folder: " + configFolder.toString());
        File configFile = FileUtils.findInDir(configFolder, fileName);
        if (configFile != null) {
            log.debug("found config file: " + configFile.toPath().toAbsolutePath());
            return configFile;
        }
        // file is not in folder, create it by extracting from within the jar into the subtitle buddy folder in user dir
        CopiedClassPathFile configFileInJar = classPathFileExtractor.findOnClassPath(fileName);
        Path targetPath = configFolder.resolve(fileName);
        Files.copy(Paths.get(configFileInJar.getFile().toURI()), targetPath, StandardCopyOption.COPY_ATTRIBUTES);
        return targetPath.toFile();

    }

}
