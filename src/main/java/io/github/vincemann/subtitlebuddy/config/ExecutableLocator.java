package io.github.vincemann.subtitlebuddy.config;

import java.nio.file.Path;

/**
 * Finds the running executables path.
 * This is useful for finding the jar file, in which the program is running.
 */
public interface ExecutableLocator {
    /**
     * @return the path of the running executable or null if it could not be found.
     */
    Path findPath();
}
