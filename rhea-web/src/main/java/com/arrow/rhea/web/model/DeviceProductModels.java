package com.arrow.rhea.web.model;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;

import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.rhea.data.DeviceProduct;
import com.arrow.rhea.web.model.DeviceManufacturerModels.DeviceManufacturerOption;

import moonstone.acn.client.model.AcnDeviceCategory;

public class DeviceProductModels {
	public static class DeviceProductOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -4299678383186231378L;

		public DeviceProductOption() {
			super(null, null, null);
		}

		public DeviceProductOption(DeviceProduct deviceProduct) {
			super(deviceProduct.getId(), deviceProduct.getHid(), deviceProduct.getName());
		}
	}

	public static class DeviceProductModel extends DeviceProductOption {
		private static final long serialVersionUID = 8478013431074069139L;

		private String description;
		private boolean enabled;
		private boolean editable;
		private DeviceManufacturerOption deviceManufacturer;
		// private DeviceCategoryOption deviceCategory;
		private String deviceCategory;

		public DeviceProductModel() {
			super();
			this.enabled = true;
			this.editable = true;
		}

		public DeviceProductModel(DeviceProduct deviceProduct) {
			super(deviceProduct);

			this.description = deviceProduct.getDescription();
			this.enabled = deviceProduct.isEnabled();
			this.editable = deviceProduct.isEditable();
			this.deviceManufacturer = new DeviceManufacturerOption(deviceProduct.getRefDeviceManufacturer());
			// this.deviceCategory = new
			// DeviceCategoryOption(deviceProduct.getRefDeviceCategory());
			if (deviceProduct.getDeviceCategory() != null)
				this.deviceCategory = deviceProduct.getDeviceCategory().name();
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public boolean isEditable() {
			return editable;
		}

		public void setEditable(boolean editable) {
			this.editable = editable;
		}

		public DeviceManufacturerOption getDeviceManufacturer() {
			return deviceManufacturer;
		}

		public void setDeviceManufacturer(DeviceManufacturerOption deviceManufacturer) {
			this.deviceManufacturer = deviceManufacturer;
		}

		public String getDeviceCategory() {
			return deviceCategory;
		}

		public void setDeviceCategory(String deviceCategory) {
			this.deviceCategory = deviceCategory;
		}

		// public DeviceCategoryOption getDeviceCategory() {
		// return deviceCategory;
		// }
		//
		// public void setDeviceCategory(DeviceCategoryOption deviceCategory) {
		// this.deviceCategory = deviceCategory;
		// }

	}

	public static class DeviceProductOptions implements Serializable {
		private static final long serialVersionUID = 8478013431074069139L;

		private List<DeviceManufacturerOption> manufacturerOptions;
		// private List<DeviceCategoryOption> categoryOptions;
		private EnumSet<AcnDeviceCategory> deviceCategoryOptions;

		public DeviceProductOptions() {
		}

		public DeviceProductOptions(List<DeviceManufacturerOption> manufacturerOptions,
		        EnumSet<AcnDeviceCategory> deviceCategoryOptions) {
			this.manufacturerOptions = manufacturerOptions;
			// this.categoryOptions = categoryOptions;
			this.deviceCategoryOptions = deviceCategoryOptions;
		}

		public List<DeviceManufacturerOption> getManufacturerOptions() {
			return manufacturerOptions;
		}

		// public List<DeviceCategoryOption> getCategoryOptions() {
		// return categoryOptions;
		// }

		public EnumSet<AcnDeviceCategory> getDeviceCategoryOptions() {
			return deviceCategoryOptions;
		}
	}
}
