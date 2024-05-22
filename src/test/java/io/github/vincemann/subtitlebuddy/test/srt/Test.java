package io.github.vincemann.subtitlebuddy.test.srt;

import io.github.vincemann.subtitlebuddy.srt.parser.InvalidIdException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    @org.junit.Test
    public void test0() throws InvalidIdException {
        String line = "0";
        int read = readId(line);
        System.err.println(read);
    }

    private int readId(String line) throws InvalidIdException {
        String trimmedLine = line.trim();
        Pattern pattern = Pattern.compile("^\\d+");
        Matcher matcher = pattern.matcher(trimmedLine);

        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(0));
            return id;
        } else {
            throw new InvalidIdException("invalid id at line: " + line);
        }
    }

}
