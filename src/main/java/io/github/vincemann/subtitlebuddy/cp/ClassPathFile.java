package io.github.vincemann.subtitlebuddy.cp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

/**
 * Represents a file found on the classpath.
 * Contains the file and its name.
 * Files inside the running jar are only accessible as a Stream, so they are copied to a temp file.
 */
@AllArgsConstructor
@Getter
public class ClassPathFile {
    private File file;
    private String fileName;
}
