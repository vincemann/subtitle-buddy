package io.github.vincemann.subtitleBuddy.os.osDetector;

import io.github.vincemann.subtitleBuddy.os.OS;
import io.github.vincemann.subtitleBuddy.os.UnsupportedOperatingSystemException;

public interface OperatingSystemDetector {

    public OS detectOperatingSystem() throws UnsupportedOperatingSystemException;
}
