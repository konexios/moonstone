package com.arrow.pegasus;

import com.arrow.acs.AcsLogicalException;

public class LockedAccountException extends AcsLogicalException {
	private static final long serialVersionUID = -4391554817324406071L;

	public LockedAccountException() {
		super("Account is locked!");
	}
}
