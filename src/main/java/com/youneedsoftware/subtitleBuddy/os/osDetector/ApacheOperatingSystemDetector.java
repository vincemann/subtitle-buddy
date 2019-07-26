package com.youneedsoftware.subtitleBuddy.os.osDetector;

import com.google.inject.Singleton;
import com.youneedsoftware.subtitleBuddy.os.OS;
import com.youneedsoftware.subtitleBuddy.os.UnsupportedOperatingSystemException;
import org.apache.commons.lang3.SystemUtils;

@Singleton
public class ApacheOperatingSystemDetector implements OperatingSystemDetector{

    @Override
    public OS detectOperatingSystem() throws UnsupportedOperatingSystemException {
        if(SystemUtils.IS_OS_LINUX){
            return OS.LINUX;
        }else if(SystemUtils.IS_OS_WINDOWS){
            return OS.WINDOWS;
        }else if(SystemUtils.IS_OS_MAC){
            return OS.OSX;
        }
        throw new UnsupportedOperatingSystemException();
    }
}
