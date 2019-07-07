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

import com.arrow.acs.client.search.SearchCriteria;

public class TelemetrySearchCriteria extends SearchCriteria {
	private static final String TELEMETRY_NAMES = "telemetryNames";
	private PageSearchCriteria pageSearchCriteria = new PageSearchCriteria();
	private TimestampSearchCriteria timestampSearchCriteria = new TimestampSearchCriteria();

	public TelemetrySearchCriteria withTelemetryNames(String telemetryNames) {
		simpleCriteria.put(TELEMETRY_NAMES, telemetryNames);
		return this;
	}

	public TelemetrySearchCriteria withPage(int page) {
		pageSearchCriteria.withPage(page);
		simpleCriteria.putAll(pageSearchCriteria.getSimpleCriteria());
		return this;
	}

	public TelemetrySearchCriteria withSize(int size) {
		pageSearchCriteria.withSize(size);
		simpleCriteria.putAll(pageSearchCriteria.getSimpleCriteria());
		return this;
	}

	public TelemetrySearchCriteria withFromTimestamp(String fromTimestamp) {
		timestampSearchCriteria.withFromTimestamp(fromTimestamp);
		simpleCriteria.putAll(timestampSearchCriteria.getSimpleCriteria());
		return this;
	}

	public TelemetrySearchCriteria withToTimestamp(String toTimestamp) {
		timestampSearchCriteria.withToTimestamp(toTimestamp);
		simpleCriteria.putAll(timestampSearchCriteria.getSimpleCriteria());
		return this;
	}
}
