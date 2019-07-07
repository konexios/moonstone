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

public class EventsSearchCriteria extends SortedSearchCriteria {
	private static final String STATUSES = "statuses";
	private static final String SYSTEM_NAMES = "systemNames";

	public EventsSearchCriteria withStatuses(String[] statuses) {
		arrayCriteria.put(STATUSES, statuses);
		return this;
	}

	public EventsSearchCriteria withSystemNames(String[] systemNames) {
		arrayCriteria.put(SYSTEM_NAMES, systemNames);
		return this;
	}
}
