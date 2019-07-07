package com.arrow.kronos.repo;

import java.util.EnumSet;

import com.arrow.acn.client.model.AcnDeviceCategory;

public class DeviceTypeSearchParams extends KronosDocumentSearchParams {
	private static final long serialVersionUID = -6493717210935347395L;

	private Boolean rheaDeviceTypeDefined;
	private EnumSet<AcnDeviceCategory> deviceCategories;

	public Boolean getRheaDeviceTypeDefined() {
		return rheaDeviceTypeDefined;
	}

	public DeviceTypeSearchParams setRheaDeviceTypeDefined(Boolean rheaDeviceTypeDefined) {
		this.rheaDeviceTypeDefined = rheaDeviceTypeDefined;

		return this;
	}

	public EnumSet<AcnDeviceCategory> getDeviceCategories() {
		return deviceCategories;
	}

	public void setDeviceCategories(EnumSet<AcnDeviceCategory> deviceCategories) {
		this.deviceCategories = deviceCategories;
	}
}
