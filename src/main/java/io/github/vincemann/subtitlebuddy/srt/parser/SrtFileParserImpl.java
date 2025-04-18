package io.github.vincemann.subtitlebuddy.srt.parser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.gui.dialog.AlertDialog;
import io.github.vincemann.subtitlebuddy.srt.SrtOptions;
import io.github.vincemann.subtitlebuddy.srt.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class SrtFileParserImpl implements SrtFileParser {

    private static final int END_ID = 9999;

    private Charset encoding = StandardCharsets.UTF_8;

    private int currentId;

    private int linesRead = 0;

    private String currentIdLine = null;

    private List<SubtitleParagraph> subtitles = new ArrayList<>();

    private SubtitleTextParser subtitleTextParser;

    private FileEncodingConverter fileEncodingConverter = new FileEncodingConverterImpl();

    private boolean eof = false;

    @Inject
    public SrtFileParserImpl(SrtOptions options, AlertDialog alertDialog, SubtitleTextParser subtitleTextParser) {
        this.subtitleTextParser = subtitleTextParser;
        initUserDefinedEncoding(options.getEncoding(), alertDialog);
    }

    public SrtFileParserImpl(SubtitleTextParser subtitleTextParser) {
        this.subtitleTextParser = subtitleTextParser;
    }

    private void initUserDefinedEncoding(String encodingFromConfigFile, AlertDialog alertDialog) {
        try {
            if (encodingFromConfigFile != null) {
                this.encoding = Charset.forName(encodingFromConfigFile);
            }
        } catch (UnsupportedCharsetException e) {
            alertDialog.tellUser("Invalid encoding: " + encodingFromConfigFile +
                    "\n Please change in applications.properties file - which is located in %APPDATA%\\SubtitleBuddy or /home/user/.subtitlebuddy." +
                    "\n example: encoding=UTF-8 \n Using default encoding."
            );
        }
    }

    @Override
    public List<SubtitleParagraph> parseFile(File srtFile) throws CorruptedSrtFileException, IOException {

        FileInputStream in = fileEncodingConverter.loadWithEncoding(encoding,srtFile);
        Scanner scanner = new Scanner(in, this.encoding);
        SubtitleTimestamps timestamps = null;
        SubtitleText text = null;

        if (!scanner.hasNextLine()) {
            throw new CorruptedSrtFileException("Empty file", 0, subtitles);
        }
        try {
            while (scanner.hasNextLine()) {
                if (eof)
                    break;
                // read id
                this.currentId = readIdLine(scanner);
//                System.err.println("read id: " + this.currentId);

                // read timestamps
                timestamps = readTimestampsLine(scanner);
//                System.err.println("read timestamps: " + timestamps);

                // read subtitle -> read all lines until next id
                text = readSubtitleText(scanner);
//                System.err.println("read text: " + text);

                // add finished paragraph
                SubtitleParagraph paragraph = new SubtitleParagraph(currentId, timestamps, text);
                subtitles.add(paragraph);
//                System.err.println("added new subtitle paragraph: " + paragraph);
            }

        } catch (NoSuchElementException | InvalidTimestampFormatException | InvalidDelimiterException |
                 InvalidIdException e) {
            throw new CorruptedSrtFileException(linesRead, subtitles, e);
        } catch (EOFException e) {
            if (timestamps != null && text != null) {
                // subtitle is good enough add it and close parser
                subtitles.add(new SubtitleParagraph(currentId, timestamps, text));
            }
            // if subtitle is too broken, dont add
        } finally {
            scanner.close();
        }
        return subtitles;
    }

    private SubtitleText readSubtitleText(Scanner scanner) throws InvalidDelimiterException, EOFException {
        String subtitleString = readAndFormatSubtitleString(scanner);
        return subtitleTextParser.parse(subtitleString);
    }

    /**
     * Read until next id is encountered.
     * Also formats by replacing newlines with NEW_LINE_DELIMITER expected by subtitleTextParser.
     * Makes sure no trailing NEW_LINE_DELIMITER are present.
     */
    private String readAndFormatSubtitleString(Scanner scanner) throws EOFException {
        // dont add empty subtitle lines to avoid cluttering
        // usually the last line is empty and not needed
        // some kinda malformed srt files also have intermediate empty lines - skip those
        // also by reading until the next id, I make sure these kinda malformed files are read correctly
                /*
                example of proper

                id
                text

                id+1

                 * example of kinda malformed:
                 * id
                 *
                 * text
                 *
                 * id+1
                 */
        StringBuilder subtitleString = new StringBuilder();
        try {
            String line = readLine(scanner);
            boolean firstLine = true;
            boolean lastLineEmpty = false;
            while (!isNextId(line,lastLineEmpty)) {
                if (!line.isEmpty()) {
                    if (!firstLine){
                        subtitleString.append(SubtitleTextParser.NEW_LINE_DELIMITER);
                    }
                    subtitleString.append(removeUnsupportedTags(line));
                    firstLine = false;
                    lastLineEmpty = false;
                }else {
                    lastLineEmpty = true;
                }
                line = readLine(scanner);
                if (isEndId(line,lastLineEmpty)){
                    // end found
                    this.eof = true;
                    break;
                }
            }
            // we consumed the id line already, so store it for next iteration, for readIdLine to pick it up
            currentIdLine = line;
            return subtitleString.toString();
        }catch (EOFException e){
            // save last subtitle
            return subtitleString.toString();
        }
    }

    /**
     * example:
     * line = "<font color="#808080">JAMES :</font> We're building a house on the cliff!"
     *
     * Not supported - remove meta information from line
     * Only <i>...</i> and <n> is supported for now.
     *
     */
    public String removeUnsupportedTags(String line){
        // This regex matches tags that are not <i>...</i> or <n>
        String regex = "<(?!/i>|i>|n>)[^>]+>";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        // Remove all tags that are not <i>...</i> or <n>
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(result, "");
        }
        matcher.appendTail(result);
        return result.toString();
    }


    // sometimes we have encoding information in first line, so we need to do this a more robust way
    private String readUntilFirstId(Scanner scanner) throws EOFException {
        while (true) {
            String line = readLine(scanner);
            if (line.contains("0")) {
                return "0";
            } else if (line.contains("1")) {
                return "1";
            }
        }
    }

    private String readLine(Scanner scanner) throws EOFException {
        if (!scanner.hasNext()){
            eof = true;
            throw new EOFException();
        }
        String line = scanner.nextLine();
//        System.err.println("reading line: " + linesRead);
//        System.err.println(line);
        linesRead++;
        return line;
    }

    private boolean isEndId(String line, boolean lastLineEmpty) {
        // avoid false positive id detection by requiring empty line before id
        if (!lastLineEmpty)
            return false;
        try {
            int id = parseId(line);
            return id == END_ID || (id == 0 && currentId > 10);
        } catch (InvalidIdException e) {
            return false;
        }
    }

    private boolean isNextId(String line, boolean lastLineEmpty) {
        // avoid false positive id detection by requiring empty line before id
        if (!lastLineEmpty)
            return false;
        try {
            int id = parseId(line);
            return id == currentId + 1;
        } catch (InvalidIdException e) {
            return false;
        }
    }

    private SubtitleTimestamps readTimestampsLine(Scanner scanner) throws InvalidTimestampFormatException, EOFException {
        String line = readLine(scanner);
        if (!line.contains("-->")){
            // missing timestamp
            throw new InvalidTimestampFormatException("Missing timestamp or malformed: " + line);
        }
        String[] timestamps = line.split(" --> ");
        if (timestamps.length != 2) throw new InvalidTimestampFormatException("line: " + line + " was invalid timestamp");

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
//        System.err.println("parsing id line: " + line);
//        System.err.println("hex: " + toHexString(line));
        return parseId(line);
    }

    private int parseId(String line) throws InvalidIdException {
        Pattern pattern = Pattern.compile("^\\d+");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(0));
            } catch (NumberFormatException e) {
                throw new InvalidIdException("invalid id format at line: " + linesRead + " : " + line, e);
            }
        } else {
            throw new InvalidIdException("invalid id at line: " + linesRead + " : " + line);
        }
    }


//    public static String toHexString(String str) {
//        char[] chars = str.toCharArray();
//        StringBuilder hex = new StringBuilder();
//        for (char ch : chars) {
//            hex.append(Integer.toHexString((int) ch)).append(" ");
//        }
//        return hex.toString().trim();
//    }


}
