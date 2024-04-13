package io.github.vincemann.subtitlebuddy.config;

public class ConfigFileException extends Exception {

    public ConfigFileException() {
    }

    public ConfigFileException(String message) {
        super(message);
    }

    public ConfigFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigFileException(Throwable cause) {
        super(cause);
    }

    public ConfigFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
