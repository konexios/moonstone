package com.arrow.rhea.data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.rhea.RheaCoreConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

import moonstone.acn.client.model.AcnDeviceCategory;

@Document(collection = "device_product")
public class DeviceProduct extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = 7564272480523230445L;

	@NotBlank
	private String companyId;
	@NotBlank
	private String deviceManufacturerId;
	// @NotBlank
	// private String deviceCategoryId;
	@NotNull
	private AcnDeviceCategory deviceCategory;
	private boolean editable = CoreConstant.DEFAULT_EDITABLE;

	@Transient
	@JsonIgnore
	private Company refCompany;
	@Transient
	@JsonIgnore
	private DeviceManufacturer refDeviceManufacturer;
	@Transient
	@JsonIgnore
	private DeviceCategory refDeviceCategory;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getDeviceManufacturerId() {
		return deviceManufacturerId;
	}

	public void setDeviceManufacturerId(String deviceManufacturerId) {
		this.deviceManufacturerId = deviceManufacturerId;
	}

	public AcnDeviceCategory getDeviceCategory() {
		return deviceCategory;
	}

	public void setDeviceCategory(AcnDeviceCategory deviceCategory) {
		this.deviceCategory = deviceCategory;
	}

	// public String getDeviceCategoryId() {
	// return deviceCategoryId;
	// }
	//
	// public void setDeviceCategoryId(String deviceCategoryId) {
	// this.deviceCategoryId = deviceCategoryId;
	// }

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public Company getRefCompany() {
		return refCompany;
	}

	public void setRefCompany(Company refCompany) {
		this.refCompany = refCompany;
	}

	public DeviceManufacturer getRefDeviceManufacturer() {
		return refDeviceManufacturer;
	}

	public void setRefDeviceManufacturer(DeviceManufacturer refDeviceManufacturer) {
		this.refDeviceManufacturer = refDeviceManufacturer;
	}

	public DeviceCategory getRefDeviceCategory() {
		return refDeviceCategory;
	}

	public void setRefDeviceCategory(DeviceCategory refDeviceCategory) {
		this.refDeviceCategory = refDeviceCategory;
	}

	@Override
	protected String getProductPri() {
		return RheaCoreConstants.RHEA_PRI;
	}

	@Override
	protected String getTypePri() {
		return RheaCoreConstants.RheaPri.DEVICE_PRODUCT;
	}
}
