package com.arrow.pegasus;

import com.arrow.acs.AcsLogicalException;

public class RequiredChangePasswordException extends AcsLogicalException {
	private static final long serialVersionUID = -4391554817324406071L;

	public RequiredChangePasswordException() {
		super("Password must be changed!");
	}
}
