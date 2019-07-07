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

public class TelemetryUnitSearchCriteria extends SearchCriteria {

	private static final String SYSTEM_NAME = "systemName";
	private static final String NAME = "name";
	private static final String ENABLED = "enabled";
	private static final String SORT_FIELD = "sortField";
	private static final String SORT_DIRECTION = "sortDirection";

	public TelemetryUnitSearchCriteria withSystemName(String systemName) {
		simpleCriteria.put(SYSTEM_NAME, systemName);
		return this;
	}

	public TelemetryUnitSearchCriteria withName(String name) {
		simpleCriteria.put(NAME, name);
		return this;
	}

	public TelemetryUnitSearchCriteria withEnabled(boolean enabled) {
		simpleCriteria.put(ENABLED, String.valueOf(enabled));
		return this;
	}

	public TelemetryUnitSearchCriteria withSortField(String sortField) {
		simpleCriteria.put(SORT_FIELD, sortField);
		return this;
	}

	public TelemetryUnitSearchCriteria withSortDirection(String sortDirection) {
		simpleCriteria.put(SORT_DIRECTION, sortDirection);
		return this;
	}
}
