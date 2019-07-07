package com.arrow.rhea.client.search;

import com.arrow.acs.client.search.SearchCriteria;

public class SoftwareReleaseSearchCriteria extends SearchCriteria {

	private static final String ENABLED = "enabled";
	private static final String COMPANY_ID = "companyId";
	private static final String SOFTWARE_PRODUCT_ID = "softwareProductId";
	private static final String DEVICE_TYPE_ID = "deviceTypeId";
	private static final String NO_LONGER_SUPPORTED = "noLongerSupported";
	private static final String RIGHT_TO_USE_TYPES = "rightToUseTypes";
	private static final String PAGE = "page";
	private static final String SIZE = "size";
	private static final String SORT_DIRECTION = "sortDirection";
	private static final String SORT_PROPERTY = "sortProperty";
	private static final String UPGRADEABLE_FROM_ID = "upgradeableFromId";

	public SoftwareReleaseSearchCriteria withEnabled(Boolean enabled) {
		if (enabled != null) {
			simpleCriteria.put(ENABLED, enabled.toString());
		}
		return this;
	}

	public SoftwareReleaseSearchCriteria withCompanyId(String companyId) {
		if (companyId != null) {
			simpleCriteria.put(COMPANY_ID, companyId);
		}
		return this;
	}

	public SoftwareReleaseSearchCriteria withSoftwareProductId(String softwareProductId) {
		if (softwareProductId != null) {
			simpleCriteria.put(SOFTWARE_PRODUCT_ID, softwareProductId);
		}
		return this;
	}

	public SoftwareReleaseSearchCriteria withDeviceTypeIds(String... deviceTypeIds) {
		if (deviceTypeIds != null) {
			arrayCriteria.put(DEVICE_TYPE_ID, deviceTypeIds);
		}
		return this;
	}

	public SoftwareReleaseSearchCriteria withRightToUseTypes(String... rightToUseTypes) {
		if (rightToUseTypes != null) {
			arrayCriteria.put(RIGHT_TO_USE_TYPES, rightToUseTypes);
		}
		return this;
	}

	public SoftwareReleaseSearchCriteria withNoLongerSupported(Boolean noLongerSupported) {
		if (noLongerSupported != null) {
			simpleCriteria.put(NO_LONGER_SUPPORTED, noLongerSupported.toString());
		}
		return this;
	}

	public SoftwareReleaseSearchCriteria withPage(long page) {
		simpleCriteria.put(PAGE, Long.toString(page));
		return this;
	}

	public SoftwareReleaseSearchCriteria withSize(long size) {
		simpleCriteria.put(SIZE, Long.toString(size));
		return this;
	}

	public SoftwareReleaseSearchCriteria withSortDirection(String sortDirection) {
		if (sortDirection != null) {
			simpleCriteria.put(SORT_DIRECTION, sortDirection);
		}
		return this;
	}

	public SoftwareReleaseSearchCriteria withSortProperty(String sortProperty) {
		if (sortProperty != null) {
			simpleCriteria.put(SORT_PROPERTY, sortProperty);
		}
		return this;
	}
	
	public SoftwareReleaseSearchCriteria withUpgradeableFromIds(String... upgradeableFromIds) {
		if (upgradeableFromIds != null) {
			arrayCriteria.put(UPGRADEABLE_FROM_ID, upgradeableFromIds);
		}
		return this;
	}
}
