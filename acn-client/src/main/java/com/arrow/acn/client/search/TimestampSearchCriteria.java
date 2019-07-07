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

public class TimestampSearchCriteria extends SearchCriteria {
	private static final String FROM_TIMESTAMP = "fromTimestamp";
	private static final String TO_TIMESTAMP = "toTimestamp";

	public TimestampSearchCriteria withFromTimestamp(String fromTimestamp) {
		simpleCriteria.put(FROM_TIMESTAMP, fromTimestamp);
		return this;
	}

	public TimestampSearchCriteria withToTimestamp(String toTimestamp) {
		simpleCriteria.put(TO_TIMESTAMP, toTimestamp);
		return this;
	}
}
