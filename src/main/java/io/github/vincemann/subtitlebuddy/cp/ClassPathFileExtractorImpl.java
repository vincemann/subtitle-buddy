package io.github.vincemann.subtitlebuddy.cp;

import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Finds files on the classpath.
 * Files inside the running jar are only accessible as a Stream, so they are copied to a temp file.
 */
@Singleton
@Slf4j
public class ClassPathFileExtractorImpl implements ClassPathFileExtractor {

    public CopiedClassPathFile findOnClassPath(String relPath) throws IOException {
        URL resourceUrl = getClass().getClassLoader().getResource(relPath);
        log.debug("classpath resource to load: " + resourceUrl);
        if (resourceUrl == null) {
            throw new IOException("Resource not found on classpath: " + relPath);
        }
        return copyToTempFile(resourceUrl);
    }


    private CopiedClassPathFile copyToTempFile(URL resourceUrl) throws IOException {
        // Open the resource stream
        try (InputStream resourceStream = resourceUrl.openStream()) {
            if (resourceStream == null) {
                throw new IOException("Could not open stream for resource at " + resourceUrl);
            }
            // Create a temporary file
            Path tempFile = Files.createTempFile("SubtitleBuddyTempFile", ".tmp");
            // Copy the stream to the temporary file
            Files.copy(resourceStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            return new CopiedClassPathFile(tempFile.toFile(), getFileName(resourceUrl));
        } // Using try-with-resources to ensure the stream is closed properly
    }

    private String getFileName(URL url) {
        String path = url.getPath();
        // Handle JAR URL specifically to extract the real file name
        if (path.contains("!")) {
            path = path.substring(path.indexOf('!') + 2); // Skip the "!/" part
        }
        return Paths.get(path).getFileName().toString();
    }

}
