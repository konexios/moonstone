package com.arrow.pegasus;

import moonstone.acs.AcsLogicalException;

public class DuplicateLoginException extends AcsLogicalException {
	private static final long serialVersionUID = 7254127839385519488L;

	public DuplicateLoginException() {
		super("Login already exists!");
	}
}
