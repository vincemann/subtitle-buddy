package com.youneedsoftware.subtitleBuddy.runningExecutableFinder;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.youneedsoftware.subtitleBuddy.os.OS;
import com.youneedsoftware.subtitleBuddy.os.UnsupportedOperatingSystemException;
import com.youneedsoftware.subtitleBuddy.os.osDetector.OperatingSystemDetector;

public class RunningExecutableFinderProvider implements Provider<RunningExecutableFinder> {

    @Inject
    private OperatingSystemDetector operatingSystemDetector;

    @Override
    public RunningExecutableFinder get() {
        try {
            OS os = operatingSystemDetector.detectOperatingSystem();
            switch (os){
                case WINDOWS:
                    return new RunningJarFinder();
                case OSX:
                    return new RunningJarFinder();
                case LINUX:
                    return new RunningJarFinder();
            }
            throw new RuntimeException(new UnsupportedOperatingSystemException());
        }catch (UnsupportedOperatingSystemException e){
            throw new RuntimeException(e);
        }
    }
}
