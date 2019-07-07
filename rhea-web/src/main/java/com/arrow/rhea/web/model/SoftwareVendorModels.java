package com.arrow.rhea.web.model;

import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.rhea.data.SoftwareVendor;

public class SoftwareVendorModels {
	public static class SoftwareVendorOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 4197613793709137646L;

		public SoftwareVendorOption() {
			super(null, null, null);
		}

		public SoftwareVendorOption(SoftwareVendor softwareVendor) {
			super(softwareVendor.getId(), softwareVendor.getHid(), softwareVendor.getName());
		}
	}

	public static class SoftwareVendorModel extends SoftwareVendorOption {
		private static final long serialVersionUID = -3044373232458561919L;

		private String description;
		private boolean enabled;
		private boolean editable;

		public SoftwareVendorModel() {
			super();
			this.enabled = true;
		}

		public SoftwareVendorModel(SoftwareVendor softwareVendor) {
			super(softwareVendor);

			this.description = softwareVendor.getDescription();
			this.enabled = softwareVendor.isEnabled();
			this.editable = softwareVendor.isEditable();
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
