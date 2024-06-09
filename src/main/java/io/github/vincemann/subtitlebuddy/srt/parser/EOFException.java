package io.github.vincemann.subtitlebuddy.srt.parser;

public class EOFException extends Exception{

    public EOFException() {
    }

    public EOFException(String message) {
        super(message);
    }

    public EOFException(String message, Throwable cause) {
        super(message, cause);
    }

    public EOFException(Throwable cause) {
        super(cause);
    }

    public EOFException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
