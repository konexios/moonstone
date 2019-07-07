package com.arrow.kronos.web.exception;

import com.arrow.acs.AcsLogicalException;

public class AccountExistsException extends AcsLogicalException {

	private static final long serialVersionUID = -7498392878392409063L;

	public AccountExistsException(String message) {
		super(message);
	}

	public AccountExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
