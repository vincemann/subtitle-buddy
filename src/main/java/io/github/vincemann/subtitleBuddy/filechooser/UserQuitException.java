package io.github.vincemann.subtitleBuddy.filechooser;

public class UserQuitException extends Exception {

    public UserQuitException() {
    }

    public UserQuitException(String message) {
        super(message);
    }

    public UserQuitException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserQuitException(Throwable cause) {
        super(cause);
    }

    public UserQuitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
