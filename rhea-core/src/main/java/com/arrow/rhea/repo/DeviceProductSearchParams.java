package com.arrow.rhea.repo;

import java.util.EnumSet;
import java.util.Set;

import moonstone.acn.client.model.AcnDeviceCategory;

public class DeviceProductSearchParams extends RheaSearchParamsAbstract {
	private static final long serialVersionUID = 5040111236979913570L;

	private Set<String> deviceManufacturerIds;
	// private Set<String> deviceCategoryIds;
	private EnumSet<AcnDeviceCategory> deviceCategories;

	public Set<String> getDeviceManufacturerIds() {
		return getValues(deviceManufacturerIds);
	}

	public DeviceProductSearchParams addDeviceManufacturerIds(String... deviceManufacturerIds) {
		this.deviceManufacturerIds = addValues(this.deviceManufacturerIds, deviceManufacturerIds);
		return this;
	}

	// public Set<String> getDeviceCategoryIds() {
	// return deviceCategoryIds;
	// }

	// public DeviceProductSearchParams addDeviceCategoryIds(String...
	// deviceCategoryIds) {
	// this.deviceCategoryIds = addValues(this.deviceCategoryIds,
	// deviceCategoryIds);
	// return this;
	// }

	public EnumSet<AcnDeviceCategory> getDeviceCategories() {
		return deviceCategories;
	}

	public DeviceProductSearchParams setDeviceCategories(EnumSet<AcnDeviceCategory> deviceCategories) {
		this.deviceCategories = deviceCategories;

		return this;
	}
}