/*******************************************************************************
 * Copyright (c) 2018 Arrow Electronics, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * http://apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Arrow Electronics, Inc.
 *******************************************************************************/
package com.arrow.acn.client;

import com.arrow.acs.AcsRuntimeException;

public class AcnClientException extends AcsRuntimeException {
	private static final long serialVersionUID = 2769384072955482071L;

	public AcnClientException(String message) {
		super(message);
	}

	public AcnClientException(String message, Throwable cause) {
		super(message, cause);
	}
}