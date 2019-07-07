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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListResultModel<T> implements Serializable {
	private static final long serialVersionUID = -6654116888756092498L;

	private long size;
	private List<T> data = new ArrayList<>();

	public ListResultModel<T> withSize(long size) {
		setSize(size);
		return this;
	}

	public ListResultModel<T> withData(List<T> data) {
		setData(data);
		return this;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
}
