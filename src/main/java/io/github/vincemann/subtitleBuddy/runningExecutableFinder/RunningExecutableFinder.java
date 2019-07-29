package io.github.vincemann.subtitleBuddy.runningExecutableFinder;
import java.nio.file.Path;

/**
 * this is created for mocking/testing purposes
 * -> i want to test the softwareinstaller properly
 */
public interface RunningExecutableFinder {
    Path findRunningExecutable() throws RunningExecutableNotFoundException;
}
