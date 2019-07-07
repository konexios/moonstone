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

import com.arrow.acs.AcsUtils;
import com.arrow.acs.KeyValuePair;

public enum TelemetryItemType {
	// @formatter:off
    System("_|", true, false, "strValue"),
    String("s|", false, false, "strValue"),
    Integer("i|", false, true, "intValue"),
    Float("f|", false, true, "floatValue"),
    Boolean("b|", false, false, "boolValue"),
    Date("d|", false, false, "dateValue"),
    DateTime("dt|", false, false, "dateTimeValue"),
    IntegerSquare("i2|", false, false, "intSqrValue"),
    IntegerCube("i3|", false, false, "intCubeValue"),
    FloatSquare("f2|", false, false, "floatSqrValue"),
    FloatCube("f3|", false, false, "floatCubeValue"),
    Binary("bi|", false, false, "binaryValue");
    // @formatter:on

	private final String prefix;
	private final boolean system;
	private final boolean chartable;
	private final String fieldName;

	private TelemetryItemType(String prefix, boolean system, boolean chartable, String fieldName) {
		this.prefix = prefix;
		this.system = system;
		this.chartable = chartable;
		this.fieldName = fieldName;
	}

	public String getPrefix() {
		return prefix;
	}

	public boolean isSystem() {
		return system;
	}

	public boolean isChartable() {
		return chartable;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String buildName(String name) {
		return java.lang.String.format("%s%s", prefix, name);
	}

	public static KeyValuePair<TelemetryItemType, String> parse(String name) {
		if (AcsUtils.isNotEmpty(name))
			for (TelemetryItemType type : TelemetryItemType.values())
				if (name.startsWith(type.getPrefix()))
					return new KeyValuePair<>(type, name.substring(type.getPrefix().length()));
		return null;
	}
}
