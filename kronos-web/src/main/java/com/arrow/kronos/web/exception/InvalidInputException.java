package com.arrow.kronos.web.exception;

import com.arrow.acs.AcsLogicalException;

public class InvalidInputException extends AcsLogicalException {

	private static final long serialVersionUID = 566682791404869094L;

	public InvalidInputException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidInputException(String message) {
		super(message);
	}
}
