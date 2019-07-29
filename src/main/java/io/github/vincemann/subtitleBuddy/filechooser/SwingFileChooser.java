package io.github.vincemann.subtitleBuddy.filechooser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitleBuddy.config.uiStringsFile.UIStringsFileKeys;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyAccessException;
import io.github.vincemann.subtitleBuddy.filechooser.lastPathhandler.InvalidParentDirectoryException;
import io.github.vincemann.subtitleBuddy.filechooser.lastPathhandler.LastPathHandler;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyNotFoundException;
import lombok.extern.log4j.Log4j;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.validation.constraints.NotNull;
import java.io.File;

@Log4j
@Singleton
public class SwingFileChooser implements FileChooser {
    private String[] fileTypes;
    private String description;
    private String dialogTitle;
    private LastPathHandler lastPathHandler;


    @Inject
    private SwingFileChooser(@NotNull @Named(UIStringsFileKeys.SRT_FILE_TYPE_KEY) String[] fileTypes, @Named(UIStringsFileKeys.BASIC_SELECT_FILE_DESC_KEY) String description, @NotNull @Named(UIStringsFileKeys.SELECT_FILE_WINDOW_TITLE_KEY) String dialogTitle, @NotNull LastPathHandler lastPathHandler) {
        this.fileTypes = fileTypes;
        this.description = description;
        this.dialogTitle = dialogTitle;
        this.lastPathHandler = lastPathHandler;
    }

    @Override
    public File letUserChooseFile() throws UserQuitException {
        log.debug( "Filechooser swing check ");
        JFileChooser chooser = null;


        try {
            String startingDir = lastPathHandler.getSavedPath();
            log.debug( "Saved Path: " + startingDir + " was found, starting from there");
            chooser = new JFileChooser(startingDir);
        } catch (PropertyNotFoundException e) {
            log.warn( "No saved Path was found, starting at root directory");
            chooser = new JFileChooser();
        }

        chooser.setDialogTitle(dialogTitle);


        //todo checken ob hier immernoch eine current modification exception fliegt
        FileNameExtensionFilter filter = new FileNameExtensionFilter(description, fileTypes);
        chooser.setFileFilter(filter);

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            log.debug( "you chose to open this file: " + chooser.getSelectedFile().getName());
            try {
                lastPathHandler.savePathOfParentDir(chooser.getSelectedFile());
            } catch (InvalidParentDirectoryException | PropertyAccessException e) {
                log.error( "last path could not be saved casue: " + e.getMessage());
            }
            return chooser.getSelectedFile();
        } else {
            throw new UserQuitException();
        }
    }

}
