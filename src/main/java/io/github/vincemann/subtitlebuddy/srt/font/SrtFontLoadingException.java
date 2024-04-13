package io.github.vincemann.subtitlebuddy.srt.font;

public class SrtFontLoadingException extends Exception {


    public SrtFontLoadingException() {
    }

    public SrtFontLoadingException(String message) {
        super(message);
    }

    public SrtFontLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public SrtFontLoadingException(Throwable cause) {
        super(cause);
    }

    public SrtFontLoadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
