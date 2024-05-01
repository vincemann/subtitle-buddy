package io.github.vincemann.subtitlebuddy.config;

import io.github.vincemann.subtitlebuddy.config.ConfigDirectory;
import io.github.vincemann.subtitlebuddy.config.ConfigFileManager;
import io.github.vincemann.subtitlebuddy.config.ConfigFileException;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractor;
import io.github.vincemann.subtitlebuddy.cp.CopiedClassPathFile;
import io.github.vincemann.subtitlebuddy.util.FileUtils;
import lombok.extern.slf4j.Slf4j;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Component for creating/finding config file.
 * Original config file is within jar (on classpath).
 * This class extracts it to {@link ConfigDirectory#findOrCreate()} location, so the user can easily edit it.
 * If config file is already in config target folder, it is just returned.
 */
@Singleton
@Slf4j
public class ConfigFileManagerImpl implements ConfigFileManager {

    private ConfigDirectory configDirectory;
    private ClassPathFileExtractor classPathFileExtractor;

    @Inject
    public ConfigFileManagerImpl(ConfigDirectory configDirectory, ClassPathFileExtractor classPathFileExtractor) {
        this.configDirectory = configDirectory;
        this.classPathFileExtractor = classPathFileExtractor;
    }

    @Override
    public File findOrCreateConfigFile(String fileName) throws ConfigFileException {
        try {
            // first check if config file is already extracted into application folder
            Path configFolder = configDirectory.findOrCreate();
            log.debug("config folder: " + configFolder.toString());
            File configFile = FileUtils.findInDir(configFolder, fileName);
            if (configFile != null){
                log.debug("found config file: " + configFile.toPath().toAbsolutePath());
                return configFile;
            }
            // file is not in folder, create it by extracting from within the jar into the subtitle buddy folder in user dir
            CopiedClassPathFile configFileInJar = classPathFileExtractor.findOnClassPath(fileName);
            Path targetPath = configFolder.resolve(fileName);
            Files.copy(Paths.get(configFileInJar.getFile().toURI()),targetPath, StandardCopyOption.COPY_ATTRIBUTES);
            return targetPath.toFile();
        }catch (IOException e) {
            throw new ConfigFileException("Could not find/create config file: " + fileName,e);
        }
    }

}
