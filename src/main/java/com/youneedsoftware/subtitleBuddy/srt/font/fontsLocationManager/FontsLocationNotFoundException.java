package com.youneedsoftware.subtitleBuddy.srt.font.fontsLocationManager;

public class FontsLocationNotFoundException extends Exception {

    public FontsLocationNotFoundException() {
    }

    public FontsLocationNotFoundException(String message) {
        super(message);
    }

    public FontsLocationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FontsLocationNotFoundException(Throwable cause) {
        super(cause);
    }

    public FontsLocationNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
