package com.arrow.pegasus;

import moonstone.acs.AcsLogicalException;

public class InvalidLoginException extends AcsLogicalException {
	private static final long serialVersionUID = -4391554817324406071L;

	public InvalidLoginException() {
		super("Invalid login!");
	}
}
