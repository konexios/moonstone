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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;

import com.arrow.acn.client.model.TelemetryItemType;

public class IotParameters extends HashMap<String, String> {
	private static final long serialVersionUID = 9127332851010386582L;

	private String deviceType;
	private String externalId;
	private boolean dirty = false;

	public IotParameters() {
		put(TelemetryItemType.System.buildName(Telemetry.TIMESTAMP), String.valueOf(Instant.now().toEpochMilli()));
	}

	public void setDeviceHid(String deviceHid) {
		put(TelemetryItemType.System.buildName(Telemetry.DEVICE_HID), deviceHid);
	}

	public void setString(String name, String value) {
		put(TelemetryItemType.String.buildName(name), value);
		dirty = true;
	}

	public void setInteger(String name, Integer value) {
		setLong(name, Long.valueOf(value));
		dirty = true;
	}

	public void setLong(String name, Long value) {
		put(TelemetryItemType.Integer.buildName(name), String.valueOf(value));
		dirty = true;
	}

	public void setFloat(String name, Float value, String format) {
		setDouble(name, new Double(value), format);
		dirty = true;
	}

	public void setDouble(String name, Double value, String format) {
		put(TelemetryItemType.Float.buildName(name), String.format(format, value));
		dirty = true;
	}

	public void setBoolean(String name, boolean value) {
		put(TelemetryItemType.Boolean.buildName(name), String.valueOf(value));
		dirty = true;
	}

	public void setDate(String name, LocalDate date) {
		put(TelemetryItemType.Date.buildName(name), date.toString());
		dirty = true;
	}

	public void setDateTime(String name, LocalDateTime datetime) {
		put(TelemetryItemType.Date.buildName(name), datetime.toString());
		dirty = true;
	}

	public void setIntegerSquare(String name, Integer x, Integer y) {
		setLongSquare(name, Long.valueOf(x), Long.valueOf(y));
		dirty = true;
	}

	public void setLongSquare(String name, Long x, Long y) {
		put(TelemetryItemType.IntegerSquare.buildName(name), String.format("%d|%d", x, y));
		dirty = true;
	}

	public void setIntegerCube(String name, Integer x, Integer y, Integer z) {
		setLongCube(name, Long.valueOf(x), Long.valueOf(y), Long.valueOf(z));
		dirty = true;
	}

	public void setLongCube(String name, Long x, Long y, Long z) {
		put(TelemetryItemType.IntegerCube.buildName(name), String.format("%d|%d|%d", x, y, z));
		dirty = true;
	}

	public void setFloatSquare(String name, Float x, Float y, String format) {
		setDoubleSquare(name, new Double(x), new Double(y), format);
		dirty = true;
	}

	public void setDoubleSquare(String name, Double x, Double y, String format) {
		put(TelemetryItemType.IntegerSquare.buildName(name),
		        String.format("%s|%s", String.format(format, x), String.format(format, y)));
		dirty = true;
	}

	public void setFloatCube(String name, Float x, Float y, Float z, String format) {
		setDoubleCube(name, new Double(x), new Double(y), new Double(z), format);
		dirty = true;
	}

	public void setDoubleCube(String name, Double x, Double y, Double z, String format) {
		put(TelemetryItemType.FloatCube.buildName(name), String.format("%s|%s|%s", String.format(format, x),
		        String.format(format, y), String.format(format, z)));
		dirty = true;
	}

	public void setBinary(String name, byte[] value) {
		if (value == null) {
			value = new byte[0];
		}
		put(TelemetryItemType.Binary.buildName(name), Base64.getEncoder().encodeToString(value));
		dirty = true;
	}

	public String getDeviceHid() {
		return get(TelemetryItemType.System.buildName(Telemetry.DEVICE_HID));
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
}
