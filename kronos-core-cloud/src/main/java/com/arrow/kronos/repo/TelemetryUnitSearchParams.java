package com.arrow.kronos.repo;

import com.arrow.pegasus.repo.params.DefinitionDocumentParamsAbstract;

public class TelemetryUnitSearchParams extends DefinitionDocumentParamsAbstract {

	private static final long serialVersionUID = 4474563531096770531L;

	private String systemName;

	public TelemetryUnitSearchParams addSystemName(String systemName) {
		setSystemName(systemName);
		return this;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
}
