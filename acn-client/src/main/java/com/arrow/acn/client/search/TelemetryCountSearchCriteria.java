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
package com.arrow.acn.client.search;

public class TelemetryCountSearchCriteria extends TimestampSearchCriteria {
	private static final String TELEMETRY_NAME = "telemetryName";

	public TelemetryCountSearchCriteria withTelemetryName(String telemetryName) {
		simpleCriteria.put(TELEMETRY_NAME, telemetryName);
		return this;
	}
}
