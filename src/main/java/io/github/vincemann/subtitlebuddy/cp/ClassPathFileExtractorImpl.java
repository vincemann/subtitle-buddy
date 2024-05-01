package io.github.vincemann.subtitlebuddy.cp;

import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
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
        log.debug("Relative path of classpath resource to load: " + relPath);
        URL resourceUrl = getClass().getClassLoader().getResource(relPath);
        log.debug("Classpath resource to load: " + resourceUrl);
        if (resourceUrl == null) {
            throw new IOException("Resource not found on classpath: " + relPath);
        }
        return copyToTempFile(resourceUrl);
    }

    private CopiedClassPathFile copyToTempFile(URL resourceUrl) throws IOException {
        // Open the resource stream directly from the URL
        InputStream resourceStream = resourceUrl.openStream();
        if (resourceStream == null) {
            throw new IOException("Could not open stream for resource at " + resourceUrl);
        }
        // Create a temporary file
        Path tempFile = Files.createTempFile("SubtitleBuddyTempFile", ".tmp");
        // Copy the stream to the temporary file
        Files.copy(resourceStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

        // Extract file name from the URL or resource path
        String fileName = Paths.get(resourceUrl.getPath()).getFileName().toString(); // This may need to be adjusted based on the actual URL string format
        return new CopiedClassPathFile(tempFile.toFile(), fileName);

    }


}
