package com.arrow.rhea.web.model;

import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.rhea.data.DeviceManufacturer;

public class DeviceManufacturerModels {
	public static class DeviceManufacturerOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -8686662920147738432L;

		public DeviceManufacturerOption() {
			super(null, null, null);
		}

		public DeviceManufacturerOption(DeviceManufacturer deviceManufacturer) {
			super(deviceManufacturer.getId(), deviceManufacturer.getHid(), deviceManufacturer.getName());
		}
	}

	public static class DeviceManufacturerModel extends DeviceManufacturerOption {
		private static final long serialVersionUID = -614564053393779761L;

		private String description;
		private boolean enabled;
		private boolean editable;

		public DeviceManufacturerModel() {
			super();
			this.enabled = true;
		}

		public DeviceManufacturerModel(DeviceManufacturer manufacturer) {
			super(manufacturer);

			this.description = manufacturer.getDescription();
			this.enabled = manufacturer.isEnabled();
			this.editable = manufacturer.isEditable();
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
	}
}
