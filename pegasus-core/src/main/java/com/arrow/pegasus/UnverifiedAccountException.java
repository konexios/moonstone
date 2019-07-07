package com.arrow.pegasus;

import com.arrow.acs.AcsLogicalException;

public class UnverifiedAccountException extends AcsLogicalException {
	private static final long serialVersionUID = -4391554817324406071L;

	public UnverifiedAccountException() {
		super("Account is unverified!");
	}
}
