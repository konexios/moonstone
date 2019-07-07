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

public class PageSearchCriteria extends SearchCriteria {
	private static final String PAGE = "_page";
	private static final String SIZE = "_size";

	public PageSearchCriteria withPage(long page) {
		simpleCriteria.put(PAGE, Long.toString(page));
		return this;
	}

	public PageSearchCriteria withSize(long size) {
		simpleCriteria.put(SIZE, Long.toString(size));
		return this;
	}
}
