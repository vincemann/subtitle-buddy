package io.github.vincemann.subtitlebuddy.util;

public class OSUtils {

    public static boolean isMacOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("mac");
    }

}
