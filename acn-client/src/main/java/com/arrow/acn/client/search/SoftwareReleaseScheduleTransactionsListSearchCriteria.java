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

public class SoftwareReleaseScheduleTransactionsListSearchCriteria extends SearchCriteria {

	private static final String SORT_FIELD = "sortField";
	private static final String SORT_DIRECTION = "sortDirection";

	private PageSearchCriteria pageSearchCriteria = new PageSearchCriteria();

	public SoftwareReleaseScheduleTransactionsListSearchCriteria withSortField(String sortField) {
		simpleCriteria.put(SORT_FIELD, sortField);
		return this;
	}

	public SoftwareReleaseScheduleTransactionsListSearchCriteria withSortDirection(String sortDirection) {
		simpleCriteria.put(SORT_DIRECTION, sortDirection);
		return this;
	}

	public SoftwareReleaseScheduleTransactionsListSearchCriteria withPage(long page) {
		pageSearchCriteria.withPage(page);
		simpleCriteria.putAll(pageSearchCriteria.getSimpleCriteria());
		return this;
	}

	public SoftwareReleaseScheduleTransactionsListSearchCriteria withSize(long size) {
		pageSearchCriteria.withSize(size);
		simpleCriteria.putAll(pageSearchCriteria.getSimpleCriteria());
		return this;
	}
}
