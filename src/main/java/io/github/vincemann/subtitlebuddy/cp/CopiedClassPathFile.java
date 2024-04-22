package io.github.vincemann.subtitlebuddy.cp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

/**
 * Represents a file extracted from classpath into temp file.
 * Contains the copied temp file and its original name.
 */
@AllArgsConstructor
@Getter
public class CopiedClassPathFile {
    private File file;
    private String fileName;
}
