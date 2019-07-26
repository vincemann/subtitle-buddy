package com.youneedsoftware.subtitleBuddy.runningExecutableFinder;

public class RunningExecutableNotFoundException extends Exception {
    public RunningExecutableNotFoundException() {
    }

    public RunningExecutableNotFoundException(String message) {
        super(message);
    }

    public RunningExecutableNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RunningExecutableNotFoundException(Throwable cause) {
        super(cause);
    }

    public RunningExecutableNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
