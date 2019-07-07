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

import java.time.Instant;

public abstract class TsModelAbstract<T extends TsModelAbstract<T>> extends ModelAbstract<T> {
	private static final long serialVersionUID = -6573132535292516518L;

	private Instant createdDate;
	private String createdBy;

	public T withCreatedDate(Instant createdDate) {
		setCreatedDate(createdDate);
		return self();
	}

	public T withCreatedBy(String createdBy) {
		setCreatedBy(createdBy);
		return self();
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
