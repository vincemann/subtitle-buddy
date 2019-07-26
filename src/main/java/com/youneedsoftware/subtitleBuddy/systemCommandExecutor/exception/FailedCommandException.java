package com.youneedsoftware.subtitleBuddy.systemCommandExecutor.exception;

public class FailedCommandException extends Exception {

    public FailedCommandException() {
    }

    public FailedCommandException(String message) {
        super(message);
    }

    public FailedCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedCommandException(Throwable cause) {
        super(cause);
    }

    public FailedCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
