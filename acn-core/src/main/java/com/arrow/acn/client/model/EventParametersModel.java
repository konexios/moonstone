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

public class EventParametersModel implements Serializable{
	private static final long serialVersionUID = -5489221070333276834L;
	
	private String type;
	private String dataType;
	private String name;
	private String value;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public EventParametersModel withName(String name) {
		this.setName(name);
		return this;
	}
	
	public EventParametersModel withType(String type) {
		this.setType(type);
		return this;
	}

	public EventParametersModel withDataType(String dataType) {
		this.setDataType(dataType);
		return this;
	}

	public EventParametersModel withValue(String value) {
		this.setValue(value);
		return this;
	}
}
