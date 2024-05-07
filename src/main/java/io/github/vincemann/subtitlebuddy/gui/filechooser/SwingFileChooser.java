package io.github.vincemann.subtitlebuddy.gui.filechooser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsKeys;
import io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath.LastPathRegistry;
import io.github.vincemann.subtitlebuddy.properties.PropertyNotFoundException;
import io.github.vincemann.subtitlebuddy.util.FileUtils;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Opens a swing file chooser dialog, where the user can choose a file.
 * Utilizes {@link LastPathRegistry} to remember the last path the user navigated to.
 */
@Log4j2
@Singleton
public class SwingFileChooser implements FileChooser {
    private String[] fileTypes;
    private String description;
    private String dialogTitle;
    private LastPathRegistry lastPathRegistry;


    @Inject
    private SwingFileChooser(@Named(UIStringsKeys.SRT_FILE_TYPES) String[] fileTypes,
                             @Named(UIStringsKeys.SELECT_FILE_DESC) String description,
                             @Named(UIStringsKeys.SELECT_FILE_WINDOW_TITLE) String dialogTitle,
                             LastPathRegistry lastPathRegistry
    ) {
        this.fileTypes = fileTypes;
        this.description = description;
        this.dialogTitle = dialogTitle;
        this.lastPathRegistry = lastPathRegistry;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            log.error("Failed to set Look and Feel", e);
        }
    }

    @Override
    public File letUserChooseFile() throws UserQuitException {
        log.debug("File chooser swing check ");
        AtomicReference<File> selected = new AtomicReference<>();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    JFileChooser chooser = null;
                    try {
                        String startingDir = lastPathRegistry.getSavedPath();
                        log.debug("Saved Path: " + startingDir + " was found, starting from there");
                        if (Files.exists(Paths.get(startingDir))) {
                            chooser = new JFileChooser(startingDir);
                        } else {
                            log.debug("Saved Path was invalid, starting from root directory");
                            chooser = new JFileChooser();
                        }
                    } catch (PropertyNotFoundException e) {
                        log.warn("No saved Path was found, starting at root directory");
                        chooser = new JFileChooser();
                    } catch (InvalidPathException e) {
                        log.debug("Saved Path was invalid, starting from root directory");
                        chooser = new JFileChooser();
                    }

                    chooser.setDialogTitle(dialogTitle);


                    FileNameExtensionFilter filter = new FileNameExtensionFilter(description, fileTypes);
                    chooser.setFileFilter(filter);

                    int returnVal = chooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        log.debug("you chose to open this file: " + chooser.getSelectedFile().getName());
                        try {
                            lastPathRegistry.savePath(FileUtils.getParentDir(chooser.getSelectedFile()));
                        } catch (Exception e) {
                            log.error("last path could not be saved: ", e);
                        }
                        selected.set(chooser.getSelectedFile());
                    } else {
                        throw new UserQuitException();
                    }
                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        if (selected.get() == null)
            throw new RuntimeException("selected file was null");
        return selected.get();
    }

}
