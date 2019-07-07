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
package com.arrow.acn.client.model;

import java.io.Serializable;

public class GlobalActionPropertyModel implements Serializable {
	private static final long serialVersionUID = -1881669763883357159L;

	private String parameterName;
	private String parameterType;
	private String parameterValue;

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
	
	public GlobalActionPropertyModel withParameterName(String parameterName) {
		setParameterName(parameterName);
		return this;
	}
	
	public GlobalActionPropertyModel withParameterValue(String parameterValue) {
		setParameterValue(parameterValue);
		return this;
	}
	
	public GlobalActionPropertyModel withParameterType(String parameterType) {
		setParameterType(parameterType);
		return this;
	}
}