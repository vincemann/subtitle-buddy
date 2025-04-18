package io.github.vincemann.subtitlebuddy.gui.filechooser;

public class UserQuitException extends RuntimeException {

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
