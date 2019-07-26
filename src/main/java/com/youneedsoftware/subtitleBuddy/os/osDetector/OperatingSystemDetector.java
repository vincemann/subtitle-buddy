package com.youneedsoftware.subtitleBuddy.os.osDetector;

import com.youneedsoftware.subtitleBuddy.os.OS;
import com.youneedsoftware.subtitleBuddy.os.UnsupportedOperatingSystemException;

public interface OperatingSystemDetector {

    public OS detectOperatingSystem() throws UnsupportedOperatingSystemException;
}
