package io.github.vincemann.subtitleBuddy.os;

public class UnsupportedOperatingSystemException extends Exception {
    public UnsupportedOperatingSystemException() {
    }

    public UnsupportedOperatingSystemException(String message) {
        super(message);
    }

    public UnsupportedOperatingSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedOperatingSystemException(Throwable cause) {
        super(cause);
    }

    public UnsupportedOperatingSystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
