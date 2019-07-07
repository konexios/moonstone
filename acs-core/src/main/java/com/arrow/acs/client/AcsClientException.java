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
package com.arrow.acs.client;

import com.arrow.acs.AcsErrorResponse;
import com.arrow.acs.AcsRuntimeException;

public class AcsClientException extends AcsRuntimeException {
	private static final long serialVersionUID = -2538807905893177034L;

	private final AcsErrorResponse error;

	public AcsClientException(String message, AcsErrorResponse error) {
		super(message);
		this.error = error;
	}

	public AcsErrorResponse getError() {
		return error;
	}
}
