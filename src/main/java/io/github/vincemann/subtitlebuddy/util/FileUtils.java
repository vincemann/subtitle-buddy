package io.github.vincemann.subtitlebuddy.util;


import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class FileUtils {

    /**
     * Checks if a directory is empty.
     *
     * @param path the path to the directory to check
     * @return true if the directory is empty, false otherwise
     * @throws IOException if an I/O error occurs
     */
    public static boolean isDirectoryEmpty(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {
                return !dirStream.iterator().hasNext(); // True if directory is empty
            }
        } else {
            throw new IllegalArgumentException("Provided path is not a directory.");
        }
    }
    public static File getParentDir(File file){
        if (file.getParent() != null) {
            File parentFile = file.getParentFile();
            if (!parentFile.isDirectory()) {
                throw new IllegalArgumentException("parent was no directory");
            }
            return parentFile;
        } else {
            throw new IllegalArgumentException("ParentDir of file was null");
        }
    }

    public static File findInDir(Path dir, String filename) throws IOException {
        Optional<Path> foundFile = Files.walk(dir)
                .filter(path -> path.getFileName().toString().equals(filename))
                .findFirst();

        if (foundFile.isPresent()) {
            File file = foundFile.get().toFile();
            if (!file.exists())
                throw new IllegalArgumentException("File " + file + " does not exist");
            return file;
        } else {
            return null;
        }
    }
}
