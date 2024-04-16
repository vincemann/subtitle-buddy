package io.github.vincemann.subtitlebuddy.srt.parser;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.properties.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.gui.dialog.AlertDialog;
import io.github.vincemann.subtitlebuddy.srt.*;
import lombok.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Core component for parsing srt files.
 */
@Singleton
public class SrtFileParserImpl implements SrtFileParser {

    private static final String ITALIC_START_DELIMITER = "<i>";
    private static final String ITALIC_END_DELIMITER = "</i>";
    private static final String NEW_LINE_DELIMITER = "<n>";

    private Charset encoding = StandardCharsets.UTF_8;


    @Inject
    public SrtFileParserImpl(@Named(PropertyFileKeys.ENCODING) String encoding, AlertDialog alertDialog) {
        initUserDefinedEncoding(encoding,alertDialog);
    }

    public SrtFileParserImpl() {
    }

    private void initUserDefinedEncoding(String encodingFromConfigFile, AlertDialog alertDialog){
        try {
            if (encodingFromConfigFile != null){
                this.encoding = Charset.forName(encodingFromConfigFile);
            }
        }catch (UnsupportedCharsetException e){
            alertDialog.tellUser("Invalid encoding: " + encodingFromConfigFile +
                    "\n Please change in applications.properties file - which is located in the folder of the executable (jar)."+
                    "\n example: encoding=UTF-8 \n Using default encoding."
                    );
        }
    }

    @Override
        public List<SubtitleParagraph> transformFileToSubtitles(File srtFile) throws CorruptedSrtFileException, FileNotFoundException {
            List<SubtitleParagraph> subtitles = new ArrayList<>();

            FileInputStream in = new FileInputStream(srtFile);
            //todo aktuell verlasse ich mich noch auf default encoding
            Scanner scanner = new Scanner(in, this.encoding.name());
            int linesRead= 0;

            if(!scanner.hasNextLine()){
                throw new CorruptedSrtFileException("Empty file",0,subtitles);
            }
            try {
                while (scanner.hasNextLine()) {
                    StringBuilder subtitleString=new StringBuilder();
                    /* We assign our own ID's, ignore the ID given in the file. */
                    scanner.nextLine();
                    linesRead++;


                    //todo failt aktuell noch wenn ich nach dem timestamp eine zeile frei hab und dann den text und dann als ende wieder normal eine zeile frei -> fix
                    /* Read the Timestamps from the file. */
                    String[] timestamps = scanner.nextLine().split(" --> ");
                    linesRead++;
                    if (timestamps.length != 2) throw new InvalidTimestampFormatException("line: " + linesRead + " was invalid");

                    Timestamp startTime = new Timestamp(timestamps[0]);
                    Timestamp endTime = new Timestamp(timestamps[1]);


                    String line = scanner.nextLine();
                    linesRead++;
                    while (!line.equals("")) {
                        subtitleString.append(line);
                        subtitleString.append(NEW_LINE_DELIMITER);
                        line = scanner.nextLine();
                        linesRead++;
                    }

                    List<List<SubtitleSegment>> subtitleSegments = createSubtitleSegments(subtitleString.toString());
                    subtitles.add(new SubtitleParagraph(startTime,endTime,new SubtitleText(subtitleSegments)));
                }

            }catch (NoSuchElementException| InvalidTimestampFormatException| InvalidDelimiterException e) {
                throw new CorruptedSrtFileException(linesRead,subtitles,e);
            }
            finally {
                scanner.close();
            }
            return subtitles;
        }


        public static List<List<SubtitleSegment>>   createSubtitleSegments(@NonNull String subtitleString) throws InvalidDelimiterException{
        List<List<SubtitleSegment>> result = new ArrayList<>();
        List<SubtitleSegment> currentLine = new ArrayList<>();
        StringBuilder currentText = new StringBuilder();
        SubtitleType currentSubtitleType = SubtitleType.NORMAL;

        int amountCharsToSkip;
        for(int i = 0;i<subtitleString.length();i++){
            char currentChar = subtitleString.charAt(i);
            if(currentChar=='<'){
                SrtDelimiter srtDelimiter = findDelimiterType(subtitleString.substring(i));
                switch (srtDelimiter){
                    case ITALIC_START_DELIMITER:
                        amountCharsToSkip = ITALIC_START_DELIMITER.length();
                        currentSubtitleType=SubtitleType.ITALIC;
                        break;
                    case NEW_LINE_DELIMITER:
                        amountCharsToSkip= NEW_LINE_DELIMITER.length();
                        //clone list
                        if(currentText.length()!=0) {
                            currentLine.add(new SubtitleSegment(currentSubtitleType,currentText.toString()));
                            currentText.setLength(0);
                        }
                        result.add(new ArrayList<>(currentLine));
                        currentLine.clear();
                        break;
                    case ITALIC_END_DELIMITER:
                        amountCharsToSkip = ITALIC_END_DELIMITER.length();
                        SubtitleSegment subtitleSegment = new SubtitleSegment(SubtitleType.ITALIC,currentText.toString());
                        currentLine.add(subtitleSegment);
                        //clear string builder
                        currentText.setLength(0);
                        currentSubtitleType=SubtitleType.NORMAL;
                        break;
                    default:
                        throw new InvalidDelimiterException(subtitleString.substring(i) + " is an invalid delimiter");
                }
                //-1 weil die schleife ja noch i um 1 inkrementiert
                i+=amountCharsToSkip-1;
            }else {
                currentText.append(currentChar);
            }
        }
        return result;
    }

    public static SrtDelimiter findDelimiterType(String subText) throws InvalidDelimiterException{
        try {
            Preconditions.checkState(subText!=null);
            Preconditions.checkState(!subText.isEmpty());
            Preconditions.checkState(subText.charAt(0)=='<');
            char currentChar = ' ';
            StringBuilder delimiter=new StringBuilder();
            int count = 0;
            while (currentChar!='>'){
                Preconditions.checkState(subText.length()>count);
                currentChar = subText.charAt(count);
                delimiter.append(currentChar);
                count++;
            }

            Preconditions.checkState(delimiter.length()>=3 && delimiter.length()<=4);
            switch (delimiter.toString()){
                case ITALIC_START_DELIMITER:
                    return SrtDelimiter.ITALIC_START_DELIMITER;
                case ITALIC_END_DELIMITER:
                    return SrtDelimiter.ITALIC_END_DELIMITER;
                case NEW_LINE_DELIMITER:
                    return SrtDelimiter.NEW_LINE_DELIMITER;
            }
            //nichts davon eingetreten
            throw new InvalidDelimiterException(delimiter + " is an invalid delimiter");
        }catch (IllegalStateException e){
            throw new InvalidDelimiterException(e);
        }
    }

}
