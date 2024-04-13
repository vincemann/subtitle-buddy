package io.github.vincemann.subtitlebuddy.util.vec;

public class VectorDecodeException extends RuntimeException{

    public VectorDecodeException(Throwable cause) {
        super(cause);
    }

    public VectorDecodeException() {
    }

    public VectorDecodeException(String message) {
        super(message);
    }
}
