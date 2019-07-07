package com.arrow.kronos.repo;

import java.util.Set;

public class TestProcedureSearchParams extends KronosDocumentSearchParams {
	private static final long serialVersionUID = -1758732599818631791L;
	
	private Set<String> deviceTypeIds;

	public Set<String> getDeviceTypeIds() {
		return super.getValues(deviceTypeIds);
	}

	public TestProcedureSearchParams addDeviceTypeIds(String... deviceTypeIds) {
		this.deviceTypeIds = super.addValues(this.deviceTypeIds, deviceTypeIds);
		return this;
	}
}
