package io.github.vincemann.subtitlebuddy.cp;

import io.github.vincemann.subtitlebuddy.config.ExecutableLocator;
import io.github.vincemann.subtitlebuddy.config.RunningExecutableNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JarLocator implements ExecutableLocator {
    @Override
    public Path findPath() throws RunningExecutableNotFoundException {
        try {
            return Paths.get(JarLocator.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        }catch (Exception e){
            throw new RunningExecutableNotFoundException(e);
        }
    }
}
