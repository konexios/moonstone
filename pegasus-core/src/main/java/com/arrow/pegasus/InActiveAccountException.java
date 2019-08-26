package com.arrow.pegasus;

import moonstone.acs.AcsLogicalException;

public class InActiveAccountException extends AcsLogicalException {
	private static final long serialVersionUID = -4391554817324406071L;

	public InActiveAccountException() {
		super("Account is inactive!");
	}
}
