package io.github.vincemann.subtitlebuddy.cp;

import java.io.IOException;
import java.util.List;

/**
 * Finds and copies files on the classpath into temp file.
 * Files inside the running jar are only accessible as a Stream, so they are copied to a temp file.
 */
public interface ClassPathFileExtractor {

    CopiedClassPathFile findOnClassPath(String relPath) throws IOException;

}
