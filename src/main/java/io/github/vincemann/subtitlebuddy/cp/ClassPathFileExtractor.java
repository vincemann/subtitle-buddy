package io.github.vincemann.subtitlebuddy.cp;

import java.io.IOException;
import java.util.List;

/**
 * Finds and copies files on the classpath into temp file.
 * Files inside the running jar are only accessible as a Stream, so they are copied to a temp file.
 */
public interface ClassPathFileExtractor {



    CopiedClassPathFile findOnClassPath(String relPath) throws IOException;

    /**
     * give me the rel path of a dir in the classpath and i'll return you all files in that directory
     * @param relDirPath
     * @throws IOException
     */
    List<CopiedClassPathFile> findAllInDir(String relDirPath) throws IOException;



}
