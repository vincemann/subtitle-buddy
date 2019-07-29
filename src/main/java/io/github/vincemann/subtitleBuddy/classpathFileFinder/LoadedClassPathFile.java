package io.github.vincemann.subtitleBuddy.classpathFileFinder;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@AllArgsConstructor
@Getter
public class LoadedClassPathFile {
    private File file;
    private String originalFileName;
}
