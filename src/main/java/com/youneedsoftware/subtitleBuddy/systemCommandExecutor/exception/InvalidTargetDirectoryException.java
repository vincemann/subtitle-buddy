package com.youneedsoftware.subtitleBuddy.systemCommandExecutor.exception;

public class InvalidTargetDirectoryException extends RuntimeException {

    public InvalidTargetDirectoryException(String message) {
        super(message);
    }

    public InvalidTargetDirectoryException() {
    }
}
