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

public abstract class DefinitionModelAbstract<T extends DefinitionModelAbstract<T>>
        extends AuditableDocumentModelAbstract<T> {
	private static final long serialVersionUID = 7485858149744674534L;

	private String name;
	private String description;
	private boolean enabled;

	public T withDescription(String description) {
		setDescription(description);
		return self();
	}

	public T withName(String name) {
		setName(name);
		return self();
	}

	public T withEnabled(boolean enabled) {
		setEnabled(enabled);
		return self();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
