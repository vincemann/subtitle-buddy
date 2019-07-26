package com.youneedsoftware.subtitleBuddy.util;

import java.util.logging.LogManager;

public class LoggingUtils {

    private LoggingUtils(){}

    public static void disableUtilLogger(){
        LogManager.getLogManager().reset();
    }
}
