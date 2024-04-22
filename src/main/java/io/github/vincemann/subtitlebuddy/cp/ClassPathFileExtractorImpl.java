package io.github.vincemann.subtitlebuddy.cp;

import com.google.inject.Singleton;

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
public class ClassPathFileExtractorImpl implements ClassPathFileExtractor {

    public CopiedClassPathFile findOnClassPath(String relPath) throws IOException {
        URL resourceUrl = getClass().getClassLoader().getResource(relPath);
        if (resourceUrl == null) {
            throw new IOException("Resource not found on classpath: " + relPath);
        }
        return copyToTempFile(resourceUrl);
    }

    public List<CopiedClassPathFile> findAllInDir(String relDirPath) throws IOException {
        Enumeration<URL> resourceUrls = getClass().getClassLoader().getResources(relDirPath);
        List<CopiedClassPathFile> files = new ArrayList<>();
        while (resourceUrls.hasMoreElements()) {
            URL url = resourceUrls.nextElement();
            files.add(copyToTempFile(url));
        }
        return files;
    }


    private CopiedClassPathFile copyToTempFile(URL resourceUrl) throws IOException {
        InputStream resourceStream = resourceUrl.openStream();
        if (resourceStream == null) {
            throw new IOException("Could not open stream for resource at " + resourceUrl);
        }
        Path tempFile = Files.createTempFile("SubtitleBuddyTempFile", ".tmp");
        Files.copy(resourceStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        return new CopiedClassPathFile(tempFile.toFile(),getFileName(resourceUrl));
    }

    private String getFileName(URL url) {
        // Extracts the path part of the URL
        String path = url.getPath();
        // Uses Paths.get to handle the path correctly and get the file name
        return Paths.get(path).getFileName().toString();
    }
}
