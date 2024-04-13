package io.github.vincemann.subtitlebuddy.config.properties;

public class PropertyNotFoundException extends RuntimeException {

    public PropertyNotFoundException() {
    }

    public PropertyNotFoundException(String message) {
        super(message);
    }

    public PropertyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyNotFoundException(Throwable cause) {
        super(cause);
    }

    public PropertyNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
