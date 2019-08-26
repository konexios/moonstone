package com.arrow.rhea.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;

import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.rhea.data.SoftwareRelease;
import com.arrow.rhea.web.model.DeviceManufacturerModels.DeviceManufacturerOption;
import com.arrow.rhea.web.model.DeviceProductModels.DeviceProductOption;
import com.arrow.rhea.web.model.DeviceTypeModels.DeviceTypeOption;
import com.arrow.rhea.web.model.SoftwareProductModels.SoftwareProductOption;
import com.arrow.rhea.web.model.SoftwareVendorModels.SoftwareVendorOption;

import moonstone.acn.client.model.AcnDeviceCategory;
import moonstone.acn.client.model.RightToUseType;

public class SoftwareReleaseModels {
	public static class SoftwareReleaseOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 5862728025244835406L;
		private static final String SOFTWARE_RELEASE_NAME = "%s %d.%d.%d";

		public SoftwareReleaseOption() {
			super(null, null, null);
		}

		public SoftwareReleaseOption(SoftwareRelease softwareRelease, String softwareProduct) {
			super(softwareRelease.getId(), softwareRelease.getHid(), String.format(SOFTWARE_RELEASE_NAME,
			        softwareProduct, softwareRelease.getMajor(), softwareRelease.getMinor(), softwareRelease.getBuild()));
		}
	}

	public static class SoftwareReleaseModel extends SoftwareReleaseOption {
		private static final long serialVersionUID = 3636257632472073165L;

		private boolean enabled;
		private SoftwareProductOption softwareProduct;
		private boolean noLongerSupported;
		private String url;
		private Integer major;
		private Integer minor;
		private Integer build;
		private List<String> deviceTypeIds;
		private List<String> upgradeableFromIds;
		private SoftwareVendorOption softwareVendor;
		private RightToUseType rtuType;
		private String ownerName;
		private String ownerEmail;

		// ref file store
		private String fileName;
		private String fileChecksum;
		private Instant fileUploadDate;

		public SoftwareReleaseModel() {
			super();
			this.enabled = true;
		}

		public SoftwareReleaseModel(SoftwareRelease softwareRelease, String softwareProductName,
		        SoftwareVendorOption softwareVendor) {
			super(softwareRelease, softwareProductName);

			this.enabled = softwareRelease.isEnabled();
			this.softwareProduct = new SoftwareProductOption(softwareRelease.getRefSoftwareProduct());
			this.noLongerSupported = softwareRelease.isNoLongerSupported();
			this.url = softwareRelease.getUrl();
			this.major = softwareRelease.getMajor();
			this.minor = softwareRelease.getMinor();
			this.upgradeableFromIds = softwareRelease.getUpgradeableFromIds();
			this.deviceTypeIds = softwareRelease.getDeviceTypeIds();
			this.build = softwareRelease.getBuild();
			this.softwareVendor = softwareVendor;
			this.rtuType = softwareRelease.getRtuType();
			this.ownerEmail = softwareRelease.getOwnerEmail();
			this.ownerName = softwareRelease.getOwnerName();

			if (softwareRelease.getRefFileStore() != null) {
				this.fileName = softwareRelease.getRefFileStore().getName();
				this.fileChecksum = softwareRelease.getRefFileStore().getMd5();
				this.fileUploadDate = softwareRelease.getRefFileStore().getUploadDate();
			}
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public SoftwareProductOption getSoftwareProduct() {
			return softwareProduct;
		}

		public void setSoftwareProduct(SoftwareProductOption softwareProduct) {
			this.softwareProduct = softwareProduct;
		}

		public boolean isNoLongerSupported() {
			return noLongerSupported;
		}

		public void setNoLongerSupported(boolean noLongerSupported) {
			this.noLongerSupported = noLongerSupported;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public Integer getMajor() {
			return major;
		}

		public void setMajor(Integer major) {
			this.major = major;
		}

		public Integer getMinor() {
			return minor;
		}

		public void setMinor(Integer minor) {
			this.minor = minor;
		}

		public List<String> getDeviceTypeIds() {
			return deviceTypeIds;
		}

		public List<String> getUpgradeableFromIds() {
			return upgradeableFromIds;
		}

		public Integer getBuild() {
			return build;
		}

		public void setBuild(Integer build) {
			this.build = build;
		}

		public SoftwareVendorOption getSoftwareVendor() {
			return softwareVendor;
		}

		public void setSoftwareVendor(SoftwareVendorOption softwareVendor) {
			this.softwareVendor = softwareVendor;
		}

		public RightToUseType getRtuType() {
			return rtuType;
		}

		public void setRtuType(RightToUseType rtuType) {
			this.rtuType = rtuType;
		}

		public String getFileName() {
			return fileName;
		}

		public String getFileChecksum() {
			return fileChecksum;
		}

		public Instant getFileUploadDate() {
			return fileUploadDate;
		}

		public String getOwnerName() {
			return ownerName;
		}

		public void setOwnerName(String ownerName) {
			this.ownerName = ownerName;
		}

		public String getOwnerEmail() {
			return ownerEmail;
		}

		public void setOwnerEmail(String ownerEmail) {
			this.ownerEmail = ownerEmail;
		}
	}

	public static class SoftwareReleaseSelectionModel implements Serializable {
		private static final long serialVersionUID = 5982542698377740431L;

		private String manufacturerId;
		private AcnDeviceCategory deviceCategory;
		private String productId;
		private String softwareProductId;
		private String softwareReleaseId;
		private String softwareVendorId;
		private RightToUseType rtuType;
		private String ownerName;
		private String ownerEmail;

		public String getManufacturerId() {
			return manufacturerId;
		}

		public void setManufacturerId(String manufacturerId) {
			this.manufacturerId = manufacturerId;
		}

		public AcnDeviceCategory getDeviceCategory() {
			return deviceCategory;
		}

		public void setDeviceCategory(AcnDeviceCategory deviceCategory) {
			this.deviceCategory = deviceCategory;
		}

		public String getProductId() {
			return productId;
		}

		public void setProductId(String productId) {
			this.productId = productId;
		}

		public String getSoftwareProductId() {
			return softwareProductId;
		}

		public void setSoftwareProductId(String softwareProductId) {
			this.softwareProductId = softwareProductId;
		}

		public String getSoftwareReleaseId() {
			return softwareReleaseId;
		}

		public void setSoftwareReleaseId(String softwareReleaseId) {
			this.softwareReleaseId = softwareReleaseId;
		}

		public String getSoftwareVendorId() {
			return softwareVendorId;
		}

		public void setSoftwareVendorId(String softwareVendorId) {
			this.softwareVendorId = softwareVendorId;
		}

		public RightToUseType getRtuType() {
			return rtuType;
		}

		public void setRtuType(RightToUseType rtuType) {
			this.rtuType = rtuType;
		}

		public String getOwnerName() {
			return ownerName;
		}

		public void setOwnerName(String ownerName) {
			this.ownerName = ownerName;
		}

		public String getOwnerEmail() {
			return ownerEmail;
		}

		public void setOwnerEmail(String ownerEmail) {
			this.ownerEmail = ownerEmail;
		}
	}

	public static class SoftwareReleaseOptionsModel implements Serializable {
		private static final long serialVersionUID = 4196505753378289001L;

		private EnumSet<AcnDeviceCategory> deviceCategories;
		private List<DeviceManufacturerOption> manufacturers;
		private List<DeviceProductOption> products;
		private List<DeviceTypeOption> deviceTypes;
		private List<SoftwareProductOption> softwareProducts;
		private List<SoftwareReleaseOption> softwareReleases;
		private SoftwareReleaseSelectionModel selection;
		private List<SoftwareVendorOption> softwareVendors;
		private EnumSet<RightToUseType> rightToUseTypes;
		private String ownerName;
		private String ownerEmail;

		public EnumSet<AcnDeviceCategory> getDeviceCategories() {
			return deviceCategories;
		}

		public void setDeviceCategories(EnumSet<AcnDeviceCategory> deviceCategories) {
			this.deviceCategories = deviceCategories;
		}

		public List<DeviceManufacturerOption> getManufacturers() {
			return manufacturers;
		}

		public void setManufacturers(List<DeviceManufacturerOption> manufacturers) {
			this.manufacturers = manufacturers;
		}

		public List<DeviceProductOption> getProducts() {
			return products;
		}

		public void setProducts(List<DeviceProductOption> products) {
			this.products = products;
		}

		public List<DeviceTypeOption> getDeviceTypes() {
			return deviceTypes;
		}

		public void setDeviceTypes(List<DeviceTypeOption> deviceTypes) {
			this.deviceTypes = deviceTypes;
		}

		public SoftwareReleaseSelectionModel getSelection() {
			return selection;
		}

		public void setSelection(SoftwareReleaseSelectionModel selection) {
			this.selection = selection;
		}

		public void setSoftwareProducts(List<SoftwareProductOption> softwareProducts) {
			this.softwareProducts = softwareProducts;
		}

		public List<SoftwareProductOption> getSoftwareProducts() {
			return softwareProducts;
		}

		public void setSoftwareReleases(List<SoftwareReleaseOption> softwareReleases) {
			this.softwareReleases = softwareReleases;
		}

		public List<SoftwareReleaseOption> getSoftwareReleases() {
			return softwareReleases;
		}

		public void setSoftwareVendors(List<SoftwareVendorOption> softwareVendors) {
			this.softwareVendors = softwareVendors;
		}

		public List<SoftwareVendorOption> getSoftwareVendors() {
			return softwareVendors;
		}

		public EnumSet<RightToUseType> getRightToUseTypes() {
			return rightToUseTypes;
		}

		public void setRightToUseTypes(EnumSet<RightToUseType> rightToUseTypes) {
			this.rightToUseTypes = rightToUseTypes;
		}

		public String getOwnerName() {
			return ownerName;
		}

		public void setOwnerName(String ownerName) {
			this.ownerName = ownerName;
		}

		public String getOwnerEmail() {
			return ownerEmail;
		}

		public void setOwnerEmail(String ownerEmail) {
			this.ownerEmail = ownerEmail;
		}
	}

	public static class SoftwareReleaseFilterOptionsModel implements Serializable {
		private static final long serialVersionUID = 7006810969191069867L;

		private List<DeviceTypeOption> deviceTypes;
		private List<SoftwareProductOption> softwareProducts;
		private List<SoftwareReleaseOption> softwareReleases;

		public SoftwareReleaseFilterOptionsModel(List<SoftwareProductOption> softwareProducts,
		        List<DeviceTypeOption> deviceTypes, List<SoftwareReleaseOption> softwareReleases) {
			this.softwareProducts = softwareProducts;
			this.deviceTypes = deviceTypes;
			this.softwareReleases = softwareReleases;
		}

		public List<DeviceTypeOption> getDeviceTypes() {
			return deviceTypes;
		}

		public void setDeviceTypes(List<DeviceTypeOption> deviceTypes) {
			this.deviceTypes = deviceTypes;
		}

		public void setSoftwareProducts(List<SoftwareProductOption> softwareProducts) {
			this.softwareProducts = softwareProducts;
		}

		public List<SoftwareProductOption> getSoftwareProducts() {
			return softwareProducts;
		}

		public void setSoftwareReleases(List<SoftwareReleaseOption> softwareReleases) {
			this.softwareReleases = softwareReleases;
		}

		public List<SoftwareReleaseOption> getSoftwareReleases() {
			return softwareReleases;
		}

	}
}
