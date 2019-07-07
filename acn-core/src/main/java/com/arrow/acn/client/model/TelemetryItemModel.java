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

import com.arrow.acs.AcsSystemException;
import com.arrow.acs.client.model.ModelAbstract;

public class TelemetryItemModel extends ModelAbstract<TelemetryItemModel> {
	private static final long serialVersionUID = -7460783591208521190L;

	private String deviceHid;
	private String name;
	private TelemetryItemType type;
	private long timestamp;
	private String strValue;
	private Long intValue;
	private Double floatValue;
	private Boolean boolValue;
	private String dateValue;
	private String datetimeValue;
	private String intSqrValue;
	private String floatSqrValue;
	private String intCubeValue;
	private String floatCubeValue;
	private String binaryValue;

	@Override
	protected TelemetryItemModel self() {
		return this;
	}

	public Object value() {
		switch (type) {
		case Boolean:
			return getBoolValue();
		case Date:
			return getDateValue();
		case DateTime:
			return getDatetimeValue();
		case Float:
			return getFloatValue();
		case FloatCube:
			return getFloatCubeValue();
		case FloatSquare:
			return getFloatSqrValue();
		case Integer:
			return getIntValue();
		case IntegerCube:
			return getIntCubeValue();
		case IntegerSquare:
			return getIntSqrValue();
		case String:
			return getStrValue();
		case System:
			return getStrValue();
		case Binary:
			return getBinaryValue();
		default:
			throw new AcsSystemException("UNSUPPORTED TYPE: " + type);
		}
	}

	public TelemetryItemModel withDeviceHid(String deviceHid) {
		setDeviceHid(deviceHid);
		return this;
	}

	public TelemetryItemModel withName(String name) {
		setName(name);
		return this;
	}

	public TelemetryItemModel withType(TelemetryItemType type) {
		setType(type);
		return this;
	}

	public TelemetryItemModel withTimestamp(long timestamp) {
		setTimestamp(timestamp);
		return this;
	}

	public TelemetryItemModel withStrValue(String strValue) {
		setStrValue(strValue);
		return this;
	}

	public TelemetryItemModel withIntValue(Long intValue) {
		setIntValue(intValue);
		return this;
	}

	public TelemetryItemModel withFloatValue(Double floatValue) {
		setFloatValue(floatValue);
		return this;
	}

	public TelemetryItemModel withBoolValue(Boolean boolValue) {
		setBoolValue(boolValue);
		return this;
	}

	public TelemetryItemModel withDateValue(String dateValue) {
		setDateValue(dateValue);
		return this;
	}

	public TelemetryItemModel withDatetimeValue(String datetimeValue) {
		setDatetimeValue(datetimeValue);
		return this;
	}

	public TelemetryItemModel withIntSqrValue(String intSqrValue) {
		setIntSqrValue(intSqrValue);
		return this;
	}

	public TelemetryItemModel withFloatSqrValue(String floatSqrValue) {
		setFloatSqrValue(floatSqrValue);
		return this;
	}

	public TelemetryItemModel withIntCubeValue(String intCubeValue) {
		setIntCubeValue(intCubeValue);
		return this;
	}

	public TelemetryItemModel withFloatCubeValue(String floatCubeValue) {
		setFloatCubeValue(floatCubeValue);
		return this;
	}

	public TelemetryItemModel withBinaryValue(String binaryValue) {
		setBinaryValue(binaryValue);
		return this;
	}

	public String getDeviceHid() {
		return deviceHid;
	}

	public void setDeviceHid(String deviceHid) {
		this.deviceHid = deviceHid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TelemetryItemType getType() {
		return type;
	}

	public void setType(TelemetryItemType type) {
		this.type = type;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public Long getIntValue() {
		return intValue;
	}

	public void setIntValue(Long intValue) {
		this.intValue = intValue;
	}

	public Double getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(Double floatValue) {
		this.floatValue = floatValue;
	}

	public Boolean getBoolValue() {
		return boolValue;
	}

	public void setBoolValue(Boolean boolValue) {
		this.boolValue = boolValue;
	}

	public String getDateValue() {
		return dateValue;
	}

	public void setDateValue(String dateValue) {
		this.dateValue = dateValue;
	}

	public String getDatetimeValue() {
		return datetimeValue;
	}

	public void setDatetimeValue(String datetimeValue) {
		this.datetimeValue = datetimeValue;
	}

	public String getIntSqrValue() {
		return intSqrValue;
	}

	public void setIntSqrValue(String intSqrValue) {
		this.intSqrValue = intSqrValue;
	}

	public String getFloatSqrValue() {
		return floatSqrValue;
	}

	public void setFloatSqrValue(String floatSqrValue) {
		this.floatSqrValue = floatSqrValue;
	}

	public String getIntCubeValue() {
		return intCubeValue;
	}

	public void setIntCubeValue(String intCubeValue) {
		this.intCubeValue = intCubeValue;
	}

	public String getFloatCubeValue() {
		return floatCubeValue;
	}

	public void setFloatCubeValue(String floatCubeValue) {
		this.floatCubeValue = floatCubeValue;
	}

	public String getBinaryValue() {
		return binaryValue;
	}

	public void setBinaryValue(String binaryValue) {
		this.binaryValue = binaryValue;
	}
}
