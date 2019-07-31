package io.github.vincemann.subtitleBuddy.srt.font.fontsLocationManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitleBuddy.classpathFileFinder.ReadOnlyClassPathFileFinder;
import io.github.vincemann.subtitleBuddy.classpathFileFinder.LoadedClassPathFile;
import io.github.vincemann.subtitleBuddy.runningExecutableFinder.RunningExecutableFinder;
import io.github.vincemann.subtitleBuddy.runningExecutableFinder.RunningExecutableNotFoundException;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Log4j
@Singleton
public class ExternalFolderFontsLocationManager implements FontsLocationManager {

    private static final String DEFAULT_FONT_PATH_RELATIVE_TO_RUNNING_EXECUTABLE = "fonts";
    private static final String DEFAULT_FONT_PATH_RELATIVE_TO_CLASS_PATH_PATTERN = "/fonts/*";

    private RunningExecutableFinder runningExecutableFinder;
    private ReadOnlyClassPathFileFinder readOnlyClassPathFileFinder;

    @Inject
    public ExternalFolderFontsLocationManager(RunningExecutableFinder runningExecutableFinder, ReadOnlyClassPathFileFinder readOnlyClassPathFileFinder) {
        this.runningExecutableFinder = runningExecutableFinder;
        this.readOnlyClassPathFileFinder = readOnlyClassPathFileFinder;
    }

    /**
     *
     * wenn das userdir nicht existiert, wird es angelegt und mit default fonts gefüllt
     * wenn das nicht gelingt, wird executablefilepath/fonts angelegt und mit den default fonts gefüllt
     *
     *
     * @param userFontPath                   das dir in dem der user angibt seine fonts zu haben
     * @return
     * @throws FontsLocationNotFoundException
     */
    @Override
    public Path findFontDirectory(Path userFontPath) throws FontsLocationNotFoundException {
        try {
            Path absFontPath = userFontPath.toAbsolutePath();
            if (!absFontPath.toFile().exists()) {
                log.warn("user font directory" + absFontPath.toString() + " does not exist");
                log.debug("creating it for user");
                try {
                    Files.createDirectory(userFontPath);
                    log.debug("user font dir successfully created");

                    absFontPath=userFontPath;
                }catch (Exception e){
                    log.warn("could not create dir at user font dir path",e);
                    Path runningExecutablePath = runningExecutableFinder.findRunningExecutable();
                    Path defaultAbsFontPath = runningExecutablePath.resolve(DEFAULT_FONT_PATH_RELATIVE_TO_RUNNING_EXECUTABLE).toAbsolutePath();
                    log.warn("using default font directory instead: " + defaultAbsFontPath.toString());
                    if (defaultAbsFontPath.toFile().exists() && defaultAbsFontPath.toFile().isDirectory()) {
                        log.debug("default font directory already exists");
                        absFontPath = defaultAbsFontPath;
                    } else {
                        log.debug("default font directory does not exist, creating now");
                        Files.createDirectory(defaultAbsFontPath);
                        absFontPath = defaultAbsFontPath;
                    }
                }

                log.debug( "adding default fonts now if neccessary");
                createFontsInDirIfNecessary(absFontPath);
            }else {
                log.debug("user font dir exists!");
            }

            log.debug("selected fonts path: " + absFontPath.toString());

            return absFontPath;
        } catch (RunningExecutableNotFoundException | IOException e) {
            throw new FontsLocationNotFoundException(e);
        }
    }


    private void createFontsInDirIfNecessary(Path absDirPath) throws IOException {
        //hole nun die font files aus der jar und kopier sie in absDirPath Verzeichnis
        List<LoadedClassPathFile> loadedClassPathFiles = readOnlyClassPathFileFinder.findFilesOnClassPathDir(DEFAULT_FONT_PATH_RELATIVE_TO_CLASS_PATH_PATTERN);
        for (LoadedClassPathFile loadedClassPathFile : loadedClassPathFiles) {
            //every file
            File dest = absDirPath.resolve(loadedClassPathFile.getOriginalFileName()).toFile();
            if (dest.exists()) {
                log.warn("file: " + dest.getAbsolutePath() + " already exists, skipping");
                continue;
            }
            FileUtils.copyFile(loadedClassPathFile.getFile(), dest);
        }
    }
}
