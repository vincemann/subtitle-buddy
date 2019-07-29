package io.github.vincemann.subtitleBuddy.runningExecutableFinder;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RunningJarFinder implements RunningExecutableFinder {
    @Override
    public Path findRunningExecutable() throws RunningExecutableNotFoundException {
        try {
            return Paths.get(RunningJarFinder.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        }catch (Exception e){
            throw new RunningExecutableNotFoundException(e);
        }
    }
}
