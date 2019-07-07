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
package com.arrow.acs;

import java.io.Serializable;

public class AcsErrorResponse implements Serializable {
	private static final long serialVersionUID = 3740195068188665232L;

	private int status;
	private String message;
	private String exceptionClassName;

	public AcsErrorResponse withStatus(int status) {
		setStatus(status);
		return this;
	}

	public AcsErrorResponse withMessage(String message) {
		setMessage(message);
		return this;
	}

	public AcsErrorResponse withExceptionClassName(String exceptionClassName) {
		setExceptionClassName(exceptionClassName);
		return this;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExceptionClassName() {
		return exceptionClassName;
	}

	public void setExceptionClassName(String exceptionClassName) {
		this.exceptionClassName = exceptionClassName;
	}
}
