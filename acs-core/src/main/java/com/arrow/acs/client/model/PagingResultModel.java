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
package com.arrow.acs.client.model;

public class PagingResultModel<T> extends ListResultModel<T> {
	private static final long serialVersionUID = -7461075045946232595L;

	private long page;
	private long totalSize;
	private long totalPages;

	public PagingResultModel<T> withPage(long page) {
		setPage(page);
		return this;
	}

	public PagingResultModel<T> withTotalSize(long totalSize) {
		setTotalSize(totalSize);
		return this;
	}

	public PagingResultModel<T> withTotalPages(long totalPages) {
		setTotalPages(totalPages);
		return this;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}
}
