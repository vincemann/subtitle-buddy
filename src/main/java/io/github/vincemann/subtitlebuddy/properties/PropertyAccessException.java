package io.github.vincemann.subtitlebuddy.properties;


import org.apache.commons.configuration2.ex.ConfigurationException;

public class    PropertyAccessException extends ConfigurationException {

    public PropertyAccessException() {
    }

    public PropertyAccessException(String message) {
        super(message);
    }

    public PropertyAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyAccessException(Throwable cause) {
        super(cause);
    }
}
