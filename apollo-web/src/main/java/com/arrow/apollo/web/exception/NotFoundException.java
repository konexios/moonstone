package com.arrow.apollo.web.exception;

import com.arrow.acs.AcsLogicalException;

public class NotFoundException extends AcsLogicalException {

	private static final long serialVersionUID = -2642348648424853071L;

	public NotFoundException(String message) {
		super(message);
	}
	
	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	

}
