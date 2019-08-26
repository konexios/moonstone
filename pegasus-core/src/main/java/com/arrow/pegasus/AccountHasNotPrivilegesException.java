package com.arrow.pegasus;

import moonstone.acs.AcsLogicalException;

public class AccountHasNotPrivilegesException extends AcsLogicalException {
	private static final long serialVersionUID = -926485465627039725L;

	public AccountHasNotPrivilegesException() {
		super("Account has no privileges for the application!");
	}
}
