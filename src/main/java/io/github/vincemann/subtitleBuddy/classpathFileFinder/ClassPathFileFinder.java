package io.github.vincemann.subtitleBuddy.classpathFileFinder;

import java.io.IOException;
import java.util.List;

/**
 *  creates a tempfile and copies content from classpath Resource stream into it
 *
 */
public interface ClassPathFileFinder {

    /**
     *
     * @param relPath   resource file must exist on the given path
     * @throws IOException
     */
    public LoadedClassPathFile findFileOnClassPath(String relPath) throws IOException;

    /**
     * give me the rel path of a dir in the classpath and i'll return you all files in that directory
     * @param relDirPath
     * @throws IOException
     */
    public List<LoadedClassPathFile> findFilesOnClassPathDir(String relDirPath) throws IOException;



}
