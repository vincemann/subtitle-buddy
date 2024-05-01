package io.github.vincemann.subtitlebuddy.cp;

import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.util.FileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Finds files on the classpath.
 * Files inside the running jar are only accessible as a Stream, so they are copied to a temp file.
 */
@Singleton
@Slf4j
public class ClassPathFileExtractorImpl implements ClassPathFileExtractor {

    public CopiedClassPathFile findOnClassPath(String relPath) throws IOException {
        log.debug("Relative path of classpath resource to load: " + relPath);
        // not working with paths or urls here, because this causes issues with windows, resulting in wrong path syntax ect
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(relPath);
        return copyToTempFile(resourceStream, FileUtils.extractFileName(relPath));
    }

    private CopiedClassPathFile copyToTempFile(InputStream resourceStream, String fileName) throws IOException {
        // Open the resource stream directly from the URL
        if (resourceStream == null) {
            throw new IOException("Could not open resource stream ");
        }
        // Create a temporary file
        Path tempFile = Files.createTempFile("SubtitleBuddyTempFile", ".tmp");
        // Copy the stream to the temporary file
        Files.copy(resourceStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

        // Extract file name from the URL or resource path
        return new CopiedClassPathFile(tempFile.toFile(), fileName);

    }


}
