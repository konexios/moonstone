package com.arrow.rhea.web.model;

import java.io.Serializable;
import java.util.List;

import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.web.model.SoftwareVendorModels.SoftwareVendorOption;

public class SoftwareProductModels {
	public static class SoftwareProductOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -5957691459080405879L;

		public SoftwareProductOption() {
			super(null, null, null);
		}

		public SoftwareProductOption(SoftwareProduct software) {
			super(software.getId(), software.getHid(), software.getName());
		}
	}

	public static class SoftwareProductModel extends SoftwareProductOption {
		private static final long serialVersionUID = -4407763630377299552L;

		private String description;
		private boolean enabled;
		private SoftwareVendorOption softwareVendor;
		private boolean editable;

		public SoftwareProductModel() {
			super();
		}

		public SoftwareProductModel(SoftwareProduct softwareProduct, SoftwareVendorOption softwareVendor) {
			super(softwareProduct);

			this.description = softwareProduct.getDescription();
			this.enabled = softwareProduct.isEnabled();
			this.softwareVendor = softwareVendor;
			this.editable = softwareProduct.isEditable();
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

		public SoftwareVendorOption getSoftwareVendor() {
			return softwareVendor;
		}

		public void setSoftwareVendor(SoftwareVendorOption softwareVendor) {
			this.softwareVendor = softwareVendor;
		}

		public boolean isEditable() {
			return editable;
		}

		public void setEditable(boolean editable) {
			this.editable = editable;
		}
	}

	public static class SoftwareProductUpsertModel implements Serializable {
		private static final long serialVersionUID = -724030868890229840L;

		private SoftwareProductModel softwareProduct;
		private List<SoftwareVendorModels.SoftwareVendorOption> softwareVendors;

		public SoftwareProductUpsertModel withSoftware(SoftwareProductModel softwareProduct) {
			setSoftwareProduct(softwareProduct);

			return this;
		}

		public SoftwareProductUpsertModel withSoftwareVendors(
		        List<SoftwareVendorModels.SoftwareVendorOption> softwareVendors) {
			setSoftwareVendors(softwareVendors);

			return this;
		}

		public SoftwareProductModel getSoftwareProduct() {
			return softwareProduct;
		}

		public void setSoftwareProduct(SoftwareProductModel softwareProduct) {
			this.softwareProduct = softwareProduct;
		}

		public List<SoftwareVendorModels.SoftwareVendorOption> getSoftwareVendors() {
			return softwareVendors;
		}

		public void setSoftwareVendors(List<SoftwareVendorModels.SoftwareVendorOption> softwareVendors) {
			this.softwareVendors = softwareVendors;
		}
	}
}
