package io.github.vincemann.subtitleBuddy.config.propertiesFile;

import org.apache.commons.configuration.ConfigurationException;

public class PropertyAccessException extends ConfigurationException {

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
