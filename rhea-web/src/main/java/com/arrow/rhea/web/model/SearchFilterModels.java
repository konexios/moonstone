package com.arrow.rhea.web.model;

import com.arrow.pegasus.webapi.data.CoreSearchFilterModel;

public class SearchFilterModels {

	public static abstract class RheaSearchFilterModel extends CoreSearchFilterModel {
		private static final long serialVersionUID = 8285571777531367991L;

		private Boolean enabled;

		public Boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}
	}

	public static class DeviceManufacturerSearchFilterModel extends RheaSearchFilterModel {
		private static final long serialVersionUID = 1142839551569504638L;

		// TODO
	}

	public static class DeviceProductSearchFilterModel extends RheaSearchFilterModel {
		private static final long serialVersionUID = -6989315904932206236L;

		// private String[] deviceCategoryIds;
		private String[] deviceCategories;
		private String[] deviceManufacturerIds;

		// public String[] getDeviceCategoryIds() {
		// return deviceCategoryIds;
		// }
		//
		// public void setDeviceCategoryIds(String[] deviceCategoryIds) {
		// this.deviceCategoryIds = deviceCategoryIds;
		// }

		public String[] getDeviceCategories() {
			return deviceCategories;
		}

		public void setDeviceCategories(String[] deviceCategories) {
			this.deviceCategories = deviceCategories;
		}

		public String[] getDeviceManufacturerIds() {
			return deviceManufacturerIds;
		}

		public void setDeviceManufacturerIds(String[] deviceManufacturerIds) {
			this.deviceManufacturerIds = deviceManufacturerIds;
		}
	}

	public static class DeviceTypeSearchFilterModel extends RheaSearchFilterModel {
		private static final long serialVersionUID = -7001014343452706287L;

		private String[] deviceProductIds;

		public String[] getDeviceProductIds() {
			return deviceProductIds;
		}

		public void setDeviceProductIds(String[] deviceProductIds) {
			this.deviceProductIds = deviceProductIds;
		}
	}

	public static class SoftwareVendorSearchFilterModel extends RheaSearchFilterModel {
		private static final long serialVersionUID = 4336999756025278430L;

		private Boolean editable;

		public Boolean isEditable() {
			return editable;
		}

		public void setEditable(Boolean editable) {
			this.editable = editable;
		}
	}

	public static class SoftwareProductSearchFilterModel extends RheaSearchFilterModel {
		private static final long serialVersionUID = 1502280165045527616L;

		private Boolean editable;

		private String[] softwareVendorIds;

		public Boolean isEditable() {
			return editable;
		}

		public void setEditable(Boolean editable) {
			this.editable = editable;
		}

		public String[] getsoftwareVendorIds() {
			return softwareVendorIds;
		}

		public void setsoftwareVendorIds(String[] softwareVendorIds) {
			this.softwareVendorIds = softwareVendorIds;
		}

	}

	public static class SoftwareReleaseSearchFilterModel extends RheaSearchFilterModel {
		private static final long serialVersionUID = -6424915412114862151L;

		private String[] softwareProductIds;
		private String[] deviceTypeIds;
		private String[] upgradeableFromIds;

		public String[] getSoftwareProductIds() {
			return softwareProductIds;
		}

		public void setSoftwareProductIds(String[] softwareProductIds) {
			this.softwareProductIds = softwareProductIds;
		}

		public String[] getDeviceTypeIds() {
			return deviceTypeIds;
		}

		public void setDeviceTypeIds(String[] deviceTypeIds) {
			this.deviceTypeIds = deviceTypeIds;
		}

		public String[] getUpgradeableFromIds() {
			return upgradeableFromIds;
		}

		public void setUpgradeableFromIds(String[] upgradeableFromIds) {
			this.upgradeableFromIds = upgradeableFromIds;
		}

	}
	
	public static class RTURequestSearchFilterModel extends RheaSearchFilterModel {
		private static final long serialVersionUID = -4502636796772423452L;

		private String[] companyIds;
		private String[] statuses;
		
		public String[] getStatuses() {
			return statuses;
		}
		
		public void setStatuses(String[] statuses) {
			this.statuses = statuses;
		}

		public String[] getCompanyIds() {
			return companyIds;
		}

		public void setCompanyIds(String[] companyIds) {
			this.companyIds = companyIds;
		}

	}
}
