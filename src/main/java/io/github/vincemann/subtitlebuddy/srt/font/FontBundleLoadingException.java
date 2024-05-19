package io.github.vincemann.subtitlebuddy.srt.font;

public class FontBundleLoadingException extends Exception {


    public FontBundleLoadingException() {
    }

    public FontBundleLoadingException(String message) {
        super(message);
    }

    public FontBundleLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public FontBundleLoadingException(Throwable cause) {
        super(cause);
    }

    public FontBundleLoadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
