package io.github.vincemann.subtitlebuddy.srt;

public class SrTException extends Exception {

	private static final long serialVersionUID = -5091778448749118104L;

	public SrTException() {
	}

	public SrTException(String message) {
		super(message);
	}

	public SrTException(String message, Throwable cause) {
		super(message, cause);
	}

	public SrTException(Throwable cause) {
		super(cause);
	}

	public SrTException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
