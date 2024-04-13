package io.github.vincemann.subtitlebuddy.cp;

import io.github.vincemann.subtitlebuddy.config.ExecutableLocator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JarLocator implements ExecutableLocator {
    @Override
    public Path findPath() {
        try {
            return Paths.get(JarLocator.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        }catch (Exception e){
            throw new IllegalStateException("cant find running executable",e);
        }
    }
}
