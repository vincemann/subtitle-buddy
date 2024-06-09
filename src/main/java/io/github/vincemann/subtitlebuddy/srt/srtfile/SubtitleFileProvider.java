package io.github.vincemann.subtitlebuddy.srt.srtfile;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsKeys;
import io.github.vincemann.subtitlebuddy.srt.FileEncodingConverter;
import io.github.vincemann.subtitlebuddy.srt.FileEncodingConverterImpl;
import io.github.vincemann.subtitlebuddy.srt.parser.CorruptedSrtFileException;
import io.github.vincemann.subtitlebuddy.gui.filechooser.FileChooser;
import io.github.vincemann.subtitlebuddy.gui.filechooser.UserQuitException;
import io.github.vincemann.subtitlebuddy.gui.dialog.AlertDialog;
import io.github.vincemann.subtitlebuddy.gui.dialog.ContinueDialog;
import io.github.vincemann.subtitlebuddy.srt.SubtitleParagraph;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtFileParser;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;


@Log4j2
@Singleton
public class SubtitleFileProvider implements Provider<SubtitleFile> {

    private FileChooser fileChooser;
    private SrtFileParser srtFileParser;
    private ContinueDialog continueDialog;
    private AlertDialog alertDialog;
    private String corruptedFileMessage;
    private String fileNotFoundMessage;
    private String invalidFileFormatMessage;
    private String emptyFileMessage;



    private SubtitleFile chosenFile;

    @Inject
    public SubtitleFileProvider(FileChooser fileChooser, SrtFileParser srtFileParser, ContinueDialog continueDialog, AlertDialog alertDialog, @Named(UIStringsKeys.CORRUPTED_FILE_MESSAGE) String corruptedFileMessage, @Named(UIStringsKeys.FILE_NOT_FOUND_MESSAGE) String fileNotFoundMessage, @Named(UIStringsKeys.INVALID_FILE_FORMAT_MESSAGE) String invalidFileFormatMessage, @Named(UIStringsKeys.EMPTY_FILE_MESSAGE) String emptyFileMessage) {
        this.fileChooser = fileChooser;
        this.srtFileParser = srtFileParser;
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
                log.info("asking user to choose file");
                File file =  fileChooser.letUserChooseFile();
                List<SubtitleParagraph> subtitles = srtFileParser.parseFile(file);
                log.info("file was parsed and valid, amount subtitles: " + subtitles.size());
                this.chosenFile = new SubtitleFileImpl(subtitles);
                log.debug("last subtitle: " + subtitles.get(subtitles.size()-1));
                return chosenFile;
//                return new SubtitleFileImpl(subtitles);
            }catch (FileNotFoundException e){
                log.warn("file not found",e);
                alertDialog.tellUser(fileNotFoundMessage);
                return get();
            }catch (CorruptedSrtFileException e) {
                log.error("corrupted file",e);
                if (e.getLinesRead() > 0) {
                    boolean stillContinue = continueDialog.askUserToContinue(composeCorruptedFileMsg(e));
                    if (!stillContinue) {
                        log.debug("user does not want to continue with a broken file");
                        // open file chooser again
                        return get();
                    } else {
                        log.debug("user chose to continue anyways");
                        this.chosenFile = new SubtitleFileImpl(e.getReadSubtitles());
                        return this.chosenFile;
                    }
                } else {
                    log.debug("corrupted file, could not even read a single line, telling user to choose a different file");
                    alertDialog.tellUser(invalidFileFormatMessage+ " Details" + e.getMessage());
                    return get();
                }
            }catch (UserQuitException e){
                log.debug("user didnt want to choose a file and quit the program");
                // todo solve better
                System.exit(0);
                return null;
            }
            catch (IOException e){
                alertDialog.tellUser("Error while loading file: " + e.getMessage());
                return get();
            }
        }catch (EmptySubtitleListException e){
            alertDialog.tellUser(emptyFileMessage);
            return get();
        }
    }

    private String composeCorruptedFileMsg(CorruptedSrtFileException exception){
        List<SubtitleParagraph> readSubtitles = exception.getReadSubtitles();
        int linesRead = exception.getLinesRead();
        String msg = corruptedFileMessage + "\n";
        msg += "Lines read: " + linesRead + ". Subtitles read: " + readSubtitles.size() + ".\n";
        if (!readSubtitles.isEmpty()){
            SubtitleParagraph last = readSubtitles.get(readSubtitles.size() - 1);
            msg += "Read until: " + last.getEndTime().toString()+ ".\n";
        }
        msg += "Details: " + exception.getMessage();
        return msg;
    }
}
