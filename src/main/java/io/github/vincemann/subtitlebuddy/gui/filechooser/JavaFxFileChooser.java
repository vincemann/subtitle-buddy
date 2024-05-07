package io.github.vincemann.subtitlebuddy.gui.filechooser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsKeys;
import io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath.LastPathRegistry;
import io.github.vincemann.subtitlebuddy.properties.PropertyNotFoundException;
import io.github.vincemann.subtitlebuddy.util.FileUtils;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

/**
 * Opens a JavaFX file chooser dialog, where the user can choose a file.
 * Utilizes {@link LastPathRegistry} to remember the last path the user navigated to.
 */
@Log4j2
@Singleton
public class JavaFxFileChooser implements FileChooser {
    private String[] fileTypes;
    private String description;
    private String dialogTitle;
    private LastPathRegistry lastPathRegistry;

    @Inject
    public JavaFxFileChooser(@Named(UIStringsKeys.SRT_FILE_TYPES) String[] fileTypes,
                             @Named(UIStringsKeys.SELECT_FILE_DESC) String description,
                             @Named(UIStringsKeys.SELECT_FILE_WINDOW_TITLE) String dialogTitle,
                             LastPathRegistry lastPathRegistry) {
        this.fileTypes = fileTypes;
        this.description = description;
        this.dialogTitle = dialogTitle;
        this.lastPathRegistry = lastPathRegistry;
    }

    @Override
    public File letUserChooseFile() throws UserQuitException {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle(dialogTitle);
        fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter(description, fileTypes));

        try {
            String startingDir = lastPathRegistry.getSavedPath();
            if (Files.exists(Paths.get(startingDir))) {
                fileChooser.setInitialDirectory(new File(startingDir));
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