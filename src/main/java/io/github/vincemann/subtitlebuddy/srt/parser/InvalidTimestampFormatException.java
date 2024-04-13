package io.github.vincemann.subtitlebuddy.srt.parser;

import io.github.vincemann.subtitlebuddy.srt.SrTException;

public class InvalidTimestampFormatException extends SrTException {

	private static final long serialVersionUID = 1856680234321642324L;

	public InvalidTimestampFormatException(String message) {
		super(message);
	}

	public InvalidTimestampFormatException() {
	}
}
