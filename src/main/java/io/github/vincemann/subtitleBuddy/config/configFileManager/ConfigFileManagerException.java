package io.github.vincemann.subtitleBuddy.config.configFileManager;

public class ConfigFileManagerException extends Exception {

    public ConfigFileManagerException() {
    }

    public ConfigFileManagerException(String message) {
        super(message);
    }

    public ConfigFileManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigFileManagerException(Throwable cause) {
        super(cause);
    }

    public ConfigFileManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
