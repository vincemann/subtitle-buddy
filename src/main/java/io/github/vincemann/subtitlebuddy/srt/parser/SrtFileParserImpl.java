package io.github.vincemann.subtitlebuddy.srt.parser;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.gui.dialog.AlertDialog;
import io.github.vincemann.subtitlebuddy.srt.SrtOptions;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Core component for parsing srt files.
 */
@Singleton
public class SrtFileParserImpl implements SrtFileParser {

    private static final String ITALIC_START_DELIMITER = "<i>";
    private static final String ITALIC_END_DELIMITER = "</i>";
    private static final String NEW_LINE_DELIMITER = "<n>";

    private Charset encoding = StandardCharsets.UTF_8;

    private int currentId;

    private int linesRead = 0;

    private String currentIdLine = null;

    private List<SubtitleParagraph> subtitles = new ArrayList<>();

    @Inject
    public SrtFileParserImpl(SrtOptions options, AlertDialog alertDialog) {
        initUserDefinedEncoding(options.getEncoding(), alertDialog);
    }

    public SrtFileParserImpl() {
    }

    private void initUserDefinedEncoding(String encodingFromConfigFile, AlertDialog alertDialog) {
        try {
            if (encodingFromConfigFile != null) {
                this.encoding = Charset.forName(encodingFromConfigFile);
            }
        } catch (UnsupportedCharsetException e) {
            alertDialog.tellUser("Invalid encoding: " + encodingFromConfigFile +
                    "\n Please change in applications.properties file - which is located in the folder of the executable (jar)." +
                    "\n example: encoding=UTF-8 \n Using default encoding."
            );
        }
    }

    @Override
    public List<SubtitleParagraph> parseSrtFile(File srtFile) throws CorruptedSrtFileException, FileNotFoundException {

        FileInputStream in = new FileInputStream(srtFile);
        Scanner scanner = new Scanner(in, this.encoding);
        SubtitleTimestamps timestamps = null;
        SubtitleText text = null;

        if (!scanner.hasNextLine()) {
            throw new CorruptedSrtFileException("Empty file", 0, subtitles);
        }
        try {
            while (scanner.hasNextLine()) {
                // read id
                this.currentId = readIdLine(scanner);
                System.err.println("read id: " + this.currentId);

                // read timestamps
                timestamps = readTimestampsLine(scanner);
                System.err.println("read timestamps: " + timestamps);

                // read subtitle -> read all lines until next id
                text = readSubtitleText(scanner);
                System.err.println("read text: " + text);

                // add finished paragraph
                SubtitleParagraph paragraph = new SubtitleParagraph(timestamps, text);
                subtitles.add(paragraph);
                System.err.println("added new subtitle paragraph: " + paragraph);
            }

        } catch (NoSuchElementException | InvalidTimestampFormatException | InvalidDelimiterException |
                 InvalidIdException e) {
            throw new CorruptedSrtFileException(linesRead, subtitles, e);
        } catch (EOFException e) {
            if (timestamps != null && text != null){
                // subtitle is good enough add it and close parser
                subtitles.add(new SubtitleParagraph(timestamps, text));
            }
            // if subtitle is too broken, dont add
        } finally {
            scanner.close();
        }
        return subtitles;
    }

    private SubtitleText readSubtitleText(Scanner scanner) throws InvalidDelimiterException, EOFException {
        StringBuilder subtitleString = new StringBuilder();
        String line = readLine(scanner);
        while (!isNextId(line)) {
            if (!line.isEmpty()){
                // dont add empty subtitle lines to avoid cluttering
                // usually the last line is empty and not needed
                // some kinda malformed srt files also have intermediate empty lines - skip those
                // also by reading until the next id, I make sure these kinda malformed files are read correctly
                /*
                example of proper

                id
                text

                nextid

                 * example of kinda malformed:
                 * id
                 *
                 * text
                 *
                 * nextid
                 */
                subtitleString.append(line);
                subtitleString.append(NEW_LINE_DELIMITER);
            }
            line = readLine(scanner);
        }
        // we consumed the id line already, so store it for next iteration, for readIdLine to pick it up
        currentIdLine = line;


        List<List<SubtitleSegment>> subtitleSegments = createSubtitleSegments(subtitleString.toString());
        return new SubtitleText(subtitleSegments);
    }

    // sometimes we have encoding information in first line, so we need to do this a more robust way
    private String readUntilFirstId(Scanner scanner) throws EOFException {
        while (true){
            String line = readLine(scanner);
            if (line.contains("0")){
                return "0";
            }
            else if (line.contains("1")){
                return "1";
            }
        }
    }

    private String readLine(Scanner scanner) throws EOFException {
        if (!scanner.hasNext())
            throw new EOFException();
        String line = scanner.nextLine();
        linesRead++;
        return line;
    }
    private boolean isNextId(String line) {
        try {
            int id = parseId(line);
            return id == currentId + 1;
        } catch (InvalidIdException e) {
            return false;
        }
    }

    private SubtitleTimestamps readTimestampsLine(Scanner scanner) throws InvalidTimestampFormatException, EOFException {
        String line = readLine(scanner);
        String[] timestamps = line.split(" --> ");
        linesRead++;
        if (timestamps.length != 2) throw new InvalidTimestampFormatException("line: " + linesRead + " was invalid");

        Timestamp startTime = new Timestamp(timestamps[0]);
        Timestamp endTime = new Timestamp(timestamps[1]);
        return new SubtitleTimestamps(startTime, endTime);
    }


    /**
     * If no id is read yet, read until find id.
     * Otherwise the last subtitle segment read process read the id line already, so just take that and parse.
     * I cant peek with scanner, thats why this is solved like that.
     */
    private int readIdLine(Scanner scanner) throws InvalidIdException, EOFException {
        String line;
        if (currentIdLine == null) {
            line = readUntilFirstId(scanner);
        } else {
            line = currentIdLine;
        }
        System.err.println("parsing id line: " + line);
        System.err.println("hex: " + toHexString(line));
        return parseId(line);
    }

    private int parseId(String line) throws InvalidIdException {
        Pattern pattern = Pattern.compile("^\\d+");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(0));
            }catch (NumberFormatException e){
                throw new InvalidIdException("invalid id format at line: " + linesRead +" : " + line,e);
            }
        } else {
            throw new InvalidIdException("invalid id at line: " + linesRead + " : " + line);
        }
    }


    public static String toHexString(String str) {
        char[] chars = str.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch)).append(" ");
        }
        return hex.toString().trim();
    }


    public static List<List<SubtitleSegment>> createSubtitleSegments(@NonNull String subtitleString) throws InvalidDelimiterException {
        List<List<SubtitleSegment>> result = new ArrayList<>();
        List<SubtitleSegment> currentLine = new ArrayList<>();
        StringBuilder currentText = new StringBuilder();
        SubtitleType currentSubtitleType = SubtitleType.NORMAL;

        int amountCharsToSkip;
        for (int i = 0; i < subtitleString.length(); i++) {
            char currentChar = subtitleString.charAt(i);
            if (currentChar == '<') {
                SrtDelimiter srtDelimiter = findDelimiterType(subtitleString.substring(i));
                switch (srtDelimiter) {
                    case ITALIC_START_DELIMITER:
                        amountCharsToSkip = ITALIC_START_DELIMITER.length();
                        currentSubtitleType = SubtitleType.ITALIC;
                        break;
                    case NEW_LINE_DELIMITER:
                        amountCharsToSkip = NEW_LINE_DELIMITER.length();
                        //clone list
                        if (currentText.length() != 0) {
                            currentLine.add(new SubtitleSegment(currentSubtitleType, currentText.toString()));
                            currentText.setLength(0);
                        }
                        result.add(new ArrayList<>(currentLine));
                        currentLine.clear();
                        break;
                    case ITALIC_END_DELIMITER:
                        amountCharsToSkip = ITALIC_END_DELIMITER.length();
                        SubtitleSegment subtitleSegment = new SubtitleSegment(SubtitleType.ITALIC, currentText.toString());
                        currentLine.add(subtitleSegment);
                        //clear string builder
                        currentText.setLength(0);
                        currentSubtitleType = SubtitleType.NORMAL;
                        break;
                    default:
                        throw new InvalidDelimiterException(subtitleString.substring(i) + " is an invalid delimiter");
                }
                //-1 weil die schleife ja noch i um 1 inkrementiert
                i += amountCharsToSkip - 1;
            } else {
                currentText.append(currentChar);
            }
        }
        return result;
    }

    public static SrtDelimiter findDelimiterType(String subText) throws InvalidDelimiterException {
        try {
            Preconditions.checkState(subText != null);
            Preconditions.checkState(!subText.isEmpty());
            Preconditions.checkState(subText.charAt(0) == '<');
            char currentChar = ' ';
            StringBuilder delimiter = new StringBuilder();
            int count = 0;
            while (currentChar != '>') {
                Preconditions.checkState(subText.length() > count);
                currentChar = subText.charAt(count);
                delimiter.append(currentChar);
                count++;
            }

            Preconditions.checkState(delimiter.length() >= 3 && delimiter.length() <= 4);
            switch (delimiter.toString()) {
                case ITALIC_START_DELIMITER:
                    return SrtDelimiter.ITALIC_START_DELIMITER;
                case ITALIC_END_DELIMITER:
                    return SrtDelimiter.ITALIC_END_DELIMITER;
                case NEW_LINE_DELIMITER:
                    return SrtDelimiter.NEW_LINE_DELIMITER;
            }
            //nichts davon eingetreten
            throw new InvalidDelimiterException(delimiter + " is an invalid delimiter");
        } catch (IllegalStateException e) {
            throw new InvalidDelimiterException(e);
        }
    }

}
