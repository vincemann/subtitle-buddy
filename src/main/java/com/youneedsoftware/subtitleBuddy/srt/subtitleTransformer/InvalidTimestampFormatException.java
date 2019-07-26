package com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer;

import com.youneedsoftware.subtitleBuddy.srt.SRTException;

public class InvalidTimestampFormatException extends SRTException {

	private static final long serialVersionUID = 1856680234321642324L;

	public InvalidTimestampFormatException(String message) {
		super(message);
	}

	public InvalidTimestampFormatException() {
	}
}
