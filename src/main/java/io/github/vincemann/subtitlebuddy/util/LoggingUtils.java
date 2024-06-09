package io.github.vincemann.subtitlebuddy.util;

import java.util.logging.LogManager;

public class LoggingUtils {

    private LoggingUtils(){}

    public static void disableUtilLogger(){
        LogManager.getLogManager().reset();
    }
}
