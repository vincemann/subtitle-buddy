package io.github.vincemann.subtitlebuddy.gui.filechooser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsKeys;
import io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath.LastPathRegistry;
import io.github.vincemann.subtitlebuddy.options.PropertyNotFoundException;
import io.github.vincemann.subtitlebuddy.util.FileUtils;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

/**
 * Opens a JavaFX file chooser dialog, where the user can choose a file.
 * Utilizes {@link LastPathRegistry} to remember the last path the user navigated to.
 */
@Log4j2
@Singleton
public class JavaFxFileChooser implements FileChooser {
    private String description;
    private String dialogTitle;
    private LastPathRegistry lastPathRegistry;

    @Inject
    public JavaFxFileChooser(@Named(UIStringsKeys.SELECT_FILE_DESC) String description,
                             @Named(UIStringsKeys.SELECT_FILE_WINDOW_TITLE) String dialogTitle,
                             LastPathRegistry lastPathRegistry) {
        this.description = description;
        this.dialogTitle = dialogTitle;
        this.lastPathRegistry = lastPathRegistry;
    }

    @Override
    public File letUserChooseFile() throws UserQuitException {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle(dialogTitle);
        fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter(description, "*.srt"));

        try {
            String startingDir = lastPathRegistry.getSavedPath();
            log.debug("last path: " + startingDir);
            if (Files.exists(Paths.get(startingDir))) {
                log.debug("starting from last path");
                fileChooser.setInitialDirectory(new File(startingDir));
            }
            else{
                log.debug("last path does not exist");
            }
        } catch (PropertyNotFoundException | InvalidPathException e) {
            log.warn("Starting at root directory due to error: " + e.getMessage());
        }

        log.debug("open file chooser dialog");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            log.debug("You chose to open this file: " + file.getName());
            try {
                lastPathRegistry.savePath(FileUtils.getParentDir(file));
            } catch (Exception e) {
                log.error("Last path could not be saved: ", e);
            }
            return file;
        } else {
            throw new UserQuitException();
        }
    }
}