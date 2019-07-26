package com.youneedsoftware.subtitleBuddy.srt.subtitleFile;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.youneedsoftware.subtitleBuddy.config.uiStringsFile.UIStringsFileKeys;
import com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer.CorruptedSrtFileException;
import com.youneedsoftware.subtitleBuddy.filechooser.FileChooser;
import com.youneedsoftware.subtitleBuddy.filechooser.UserQuitException;
import com.youneedsoftware.subtitleBuddy.gui.guiDialogs.alertDialog.AlertDialog;
import com.youneedsoftware.subtitleBuddy.gui.guiDialogs.continueDialog.ContinueDialog;
import com.youneedsoftware.subtitleBuddy.srt.Subtitle;
import com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformer;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;


@Log4j
@Singleton
public class SubtitleFileProvider implements Provider<SubtitleFile> {
    //todo in stringConfig auslagern

    private FileChooser fileChooser;
    private SrtFileTransformer srtFileTransformer;
    private ContinueDialog continueDialog;
    private AlertDialog alertDialog;
    private String corruptedFileMessage;
    private String fileNotFoundMessage;
    private String invalidFileFormatMessage;
    private String emptyFileMessage;


    private SubtitleFile chosenFile;

    @Inject
    public SubtitleFileProvider(FileChooser fileChooser, SrtFileTransformer srtFileTransformer, ContinueDialog continueDialog, AlertDialog alertDialog, @Named(UIStringsFileKeys.CORRUPTED_FILE_MESSAGE_KEY) String corruptedFileMessage, @Named(UIStringsFileKeys.FILE_NOT_FOUND_MESSAGE_KEY) String fileNotFoundMessage, @Named(UIStringsFileKeys.INVALID_FILE_FORMAT_MESSAGE_KEY) String invalidFileFormatMessage, @Named(UIStringsFileKeys.EMPTY_FILE_MESSAGE_KEY) String emptyFileMessage) {
        this.fileChooser = fileChooser;
        this.srtFileTransformer = srtFileTransformer;
        this.continueDialog = continueDialog;
        this.alertDialog = alertDialog;
        this.corruptedFileMessage = corruptedFileMessage;
        this.fileNotFoundMessage = fileNotFoundMessage;
        this.invalidFileFormatMessage = invalidFileFormatMessage;
        this.emptyFileMessage = emptyFileMessage;
    }

    @Inject
    @Override
    public SubtitleFile get() {
        if(chosenFile !=null){
            return chosenFile;
        }
        try {
            try {
                File file =  fileChooser.letUserChooseFile();
                List<Subtitle> subtitles = srtFileTransformer.transformFileToSubtitles(file);
                this.chosenFile = new SubtitleFileImpl(subtitles);
                return new SubtitleFileImpl(subtitles);
            }catch (FileNotFoundException e){
                log.warn("file not found",e);
                alertDialog.tellUser(fileNotFoundMessage);
                return get();
            }catch (CorruptedSrtFileException e) {
                log.error("corrupted file");
                if (e.getLinesRead() > 0) {
                    boolean goOn = continueDialog.askUserToContinue(corruptedFileMessage);
                    if (!goOn) {
                        log.debug("user does not want to conitune with a broken file");
                        return get();
                    } else {
                        log.debug("user chose to continue anyways");
                        this.chosenFile = new SubtitleFileImpl(e.getReadSubtitles());
                        return this.chosenFile;
                    }
                } else {
                    log.debug("corrupted file, couldnt even read a single line, telling user to choose a different file");
                    alertDialog.tellUser(invalidFileFormatMessage+ " Details" + e.getMessage());
                    return get();
                }
            }catch (UserQuitException e){
                log.debug("user didnt want to choose a file and quit the program");
                //todo nicht so geil hier gelöst
                System.exit(0);
                return null;
            }
        }catch (EmptySubtitleListException e){
            alertDialog.tellUser(emptyFileMessage);
            return get();
        }
    }
}
