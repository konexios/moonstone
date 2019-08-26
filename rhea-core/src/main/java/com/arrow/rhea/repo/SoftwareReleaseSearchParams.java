package com.arrow.rhea.repo;

import java.util.EnumSet;
import java.util.Set;

import moonstone.acn.client.model.RightToUseType;

public class SoftwareReleaseSearchParams extends RheaSearchParamsAbstract {
	private static final long serialVersionUID = 3759875221507701923L;

	private Set<String> softwareVendorIds;
	private Set<String> deviceTypeIds;
	private Boolean noLongerSupported;
	private Set<String> softwareProductIds;
	private Set<String> upgradeableFromIds;
	private EnumSet<RightToUseType> rightToUseTypes;

	public Set<String> getSoftwareVendorIds() {
		return super.getValues(softwareVendorIds);
	}

	public SoftwareReleaseSearchParams addSoftwareVendorIds(String... softwareVendorIds) {
		this.softwareVendorIds = super.addValues(this.softwareVendorIds, softwareVendorIds);

		return this;
	}

	public Set<String> getDeviceTypeIds() {
		return super.getValues(deviceTypeIds);
	}

	public SoftwareReleaseSearchParams addDeviceTypeIds(String... deviceTypeIds) {
		this.deviceTypeIds = super.addValues(this.deviceTypeIds, deviceTypeIds);

		return this;
	}

	public Boolean getNoLongerSupported() {
		return noLongerSupported;
	}

	public void setNoLongerSupported(Boolean noLongerSupported) {
		this.noLongerSupported = noLongerSupported;
	}

	public Set<String> getSoftwareProductIds() {
		return super.getValues(softwareProductIds);
	}

	public SoftwareReleaseSearchParams addSoftwareProductIds(String... softwareProductIds) {
		this.softwareProductIds = super.addValues(this.softwareProductIds, softwareProductIds);
		return this;
	}

	public Set<String> getUpgradeableFromIds() {
		return super.getValues(upgradeableFromIds);
	}

	public SoftwareReleaseSearchParams addUpgradeableFromIds(String... upgradeableFromIds) {
		this.upgradeableFromIds = super.addValues(this.upgradeableFromIds, upgradeableFromIds);
		return this;
	}

	public void setRightToUseTypes(EnumSet<RightToUseType> rtuTypes) {
		this.rightToUseTypes = rtuTypes;
	}

	public EnumSet<RightToUseType> getRightToUseTypes() {
		return rightToUseTypes;
	}

	public SoftwareReleaseSearchParams withRightToUseTypes(EnumSet<RightToUseType> rightToUseTypes) {
		setRightToUseTypes(rightToUseTypes);
		return this;
	}
}