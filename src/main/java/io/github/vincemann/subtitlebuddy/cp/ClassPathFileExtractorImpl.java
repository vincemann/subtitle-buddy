package io.github.vincemann.subtitlebuddy.cp;

import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.util.FileUtils;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Finds files on the classpath and extracts into temp file.
 */
@Singleton
@Log4j2
public class ClassPathFileExtractorImpl implements ClassPathFileExtractor {

    public CopiedClassPathFile findOnClassPath(String relPath) throws IOException {
        log.debug("loading classpath resource: " + relPath);
        // not working with paths or urls here, because this causes issues with windows, resulting in wrong path syntax ect
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(relPath);
        return copyToTempFile(resourceStream, FileUtils.getFileNameOfPath(relPath));
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
