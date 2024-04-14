package io.github.vincemann.subtitlebuddy.cp;

import io.github.vincemann.subtitlebuddy.config.ConfigDirectory;
import io.github.vincemann.subtitlebuddy.config.ConfigFileManager;
import io.github.vincemann.subtitlebuddy.config.ConfigFileException;
import io.github.vincemann.subtitlebuddy.util.FileUtils;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Component for creating/finding config file.
 * Original config file is within jar. This class copies it to folder of jar, so the user can easily edit it.
 * If config file is already in folder of jar, it is returned.
 */
@Singleton
@Log4j
public class JarConfigFileManager implements ConfigFileManager {

    private ConfigDirectory configDirectory;
    private ClassPathFileLocator classPathFileLocator;

    @Inject
    public JarConfigFileManager(ConfigDirectory configDirectory, ClassPathFileLocator classPathFileLocator) {
        this.configDirectory = configDirectory;
        this.classPathFileLocator = classPathFileLocator;
    }

    @Override
    public File findOrCreateConfigFile(String fileName) throws ConfigFileException {
        try {
            // first check if config file is already extracted into application folder
            Path configFolder = configDirectory.findOrCreate();
            File configFile = FileUtils.findInDir(configFolder, fileName);
            if (configFile != null){
                return configFile;
            }
            // file is not in folder, create it by extracting from within the jar into the jars folder
            ClassPathFile configFileInJar = classPathFileLocator.findOnClassPath(fileName);
            Path targetPath = configFolder.resolve(fileName);
            Files.copy(Paths.get(configFileInJar.getFile().toURI()),targetPath, StandardCopyOption.COPY_ATTRIBUTES);
            return targetPath.toFile();

        }catch (IOException e) {
            throw new ConfigFileException("Could not find/create config file: " + fileName,e);
        }
    }

}
