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
package com.arrow.acs.client.model;

public class HidModel extends ModelAbstract<HidModel> {
	private static final long serialVersionUID = 2017351620901710763L;

	private String message;

	@Override
	protected HidModel self() {
		return this;
	}

	public HidModel withMessage(String message) {
		setMessage(message);
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
