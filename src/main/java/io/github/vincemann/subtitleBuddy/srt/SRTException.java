package io.github.vincemann.subtitleBuddy.srt;

public class SRTException extends Exception {

	private static final long serialVersionUID = -5091778448749118104L;

	public SRTException() {
	}

	public SRTException(String message) {
		super(message);
	}

	public SRTException(String message, Throwable cause) {
		super(message, cause);
	}

	public SRTException(Throwable cause) {
		super(cause);
	}

	public SRTException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
