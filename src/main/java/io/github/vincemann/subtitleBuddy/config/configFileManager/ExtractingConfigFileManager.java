package io.github.vincemann.subtitleBuddy.config.configFileManager;

import io.github.vincemann.subtitleBuddy.classpathFileFinder.LoadedClassPathFile;
import io.github.vincemann.subtitleBuddy.classpathFileFinder.ReadOnlyClassPathFileFinder;
import io.github.vincemann.subtitleBuddy.runningExecutableFinder.RunningExecutableFinder;
import io.github.vincemann.subtitleBuddy.runningExecutableFinder.RunningExecutableNotFoundException;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Finds a configFile, with a specified name, inside the jar and extracts it to the folder, in which the jar is located.
 * If the configFile, with the specified name, is already in the folder, than this file is taken.
 *
 * This process ensures, that the file is editable. (And also easily editable by the user)
 */
@Singleton
@Log4j
public class ExtractingConfigFileManager implements ConfigFileManager {

    private RunningExecutableFinder runningExecutableFinder;
    private ReadOnlyClassPathFileFinder readOnlyClassPathFileFinder;

    @Inject
    public ExtractingConfigFileManager(RunningExecutableFinder runningExecutableFinder, ReadOnlyClassPathFileFinder readOnlyClassPathFileFinder) {
        this.runningExecutableFinder = runningExecutableFinder;
        this.readOnlyClassPathFileFinder = readOnlyClassPathFileFinder;
    }

    @Override
    public File findConfigFile(String fileName) throws ConfigFileManagerException{
        try {
            //is file in folder of jar?
            Path runningExecutable = runningExecutableFinder.findRunningExecutable();
            Path jarFolder = runningExecutable.getParent();
            AtomicReference<Path> foundConfigFileInJarFolder = new AtomicReference<>();
            Files.walk(jarFolder, 1).forEach(path -> {
                if (path.getFileName().equals(Paths.get(fileName))) {
                    //we found the configFile
                    foundConfigFileInJarFolder.set(path);
                }
            });
            if (foundConfigFileInJarFolder.get() != null) {
                //we found the configFile
                return foundConfigFileInJarFolder.get().toFile();
            }
            //the config file is not in the folder of the jar
            //lets continue searching in the jar
            LoadedClassPathFile classPathFile = readOnlyClassPathFileFinder.findFileOnClassPath(fileName);
            //copy file from classPath to folder of jar
            Path targetPath = jarFolder.resolve(fileName);
            Files.copy(Paths.get(classPathFile.getFile().toURI()),targetPath, StandardCopyOption.COPY_ATTRIBUTES);
            //assert that copy operation worked
            File targetConfigFile = targetPath.toFile();
            if(!targetConfigFile.exists()){
                throw new FileNotFoundException("Copy Operation failed. Config File not found in jar Directory");
            }else {
             return targetConfigFile;
            }
        }catch (RunningExecutableNotFoundException|IOException e) {
            throw new ConfigFileManagerException(e);
        }
    }
}
