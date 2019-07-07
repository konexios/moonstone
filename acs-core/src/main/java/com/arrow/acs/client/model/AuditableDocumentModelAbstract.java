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

public abstract class AuditableDocumentModelAbstract<T extends AuditableDocumentModelAbstract<T>>
        extends TsModelAbstract<T> {
	private static final long serialVersionUID = -1644864075961766068L;

	private Instant lastModifiedDate;
	private String lastModifiedBy;

	public T withLastModifiedDate(Instant lastModifiedDate) {
		setLastModifiedDate(lastModifiedDate);
		return self();
	}

	public T withLastModifiedBy(String lastModifiedBy) {
		setLastModifiedBy(lastModifiedBy);
		return self();
	}

	public Instant getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
}
