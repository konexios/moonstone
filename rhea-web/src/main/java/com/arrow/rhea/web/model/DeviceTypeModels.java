package com.arrow.rhea.web.model;

import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.rhea.data.DeviceType;
import com.arrow.rhea.web.model.DeviceProductModels.DeviceProductOption;

public class DeviceTypeModels {

	public static class DeviceTypeOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -973372691536309810L;

		public DeviceTypeOption() {
			super(null, null, null);
		}

		public DeviceTypeOption(DeviceType deviceType) {
			super(deviceType.getId(), deviceType.getHid(), deviceType.getName());
		}
	}

	public static class DeviceTypeModel extends DeviceTypeOption {
		private static final long serialVersionUID = 9143820621905600716L;

		private String description;
		private boolean enabled;
		private boolean editable;
		private DeviceProductOption deviceProduct;

		public DeviceTypeModel() {
			super();
			this.enabled = true;
			this.editable = true;
		}

		public DeviceTypeModel(DeviceType deviceType) {
			super(deviceType);

			this.description = deviceType.getDescription();
			this.enabled = deviceType.isEnabled();
			this.editable = deviceType.isEditable();
			this.deviceProduct = new DeviceProductOption(deviceType.getRefDeviceProduct());
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

		public DeviceProductOption getDeviceProduct() {
			return deviceProduct;
		}

		public void setDeviceProduct(DeviceProductOption deviceProduct) {
			this.deviceProduct = deviceProduct;
		}
	}
}
