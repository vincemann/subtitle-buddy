package io.github.vincemann.subtitlebuddy.cp;

import java.io.IOException;
import java.util.List;

/**
 * Finds files on the classpath.
 */
public interface ClassPathFileLocator {



    ClassPathFile findOnClassPath(String relPath) throws IOException;

    /**
     * give me the rel path of a dir in the classpath and i'll return you all files in that directory
     * @param relDirPath
     * @throws IOException
     */
    public List<ClassPathFile> findAllInDir(String relDirPath) throws IOException;



}
