package com.arrow.pegasus.repo.params;

import java.io.Serializable;

public class DefinitionDocumentParamsAbstract extends AuditableDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = -566123887490825190L;

	protected String name;
	protected Boolean enabled;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
