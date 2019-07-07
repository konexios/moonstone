package com.arrow.rhea.data;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.rhea.RheaCoreConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "device_type")
public class DeviceType extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = 6505321791387166583L;

	@NotBlank
	private String companyId;
	private String deviceProductId;
	private boolean editable = CoreConstant.DEFAULT_EDITABLE;

	@Transient
	@JsonIgnore
	private Company refCompany;
	@Transient
	@JsonIgnore
	private DeviceProduct refDeviceProduct;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getDeviceProductId() {
		return deviceProductId;
	}

	public void setDeviceProductId(String deviceProductId) {
		this.deviceProductId = deviceProductId;
	}

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

	public DeviceProduct getRefDeviceProduct() {
		return refDeviceProduct;
	}

	public void setRefDeviceProduct(DeviceProduct refDeviceProduct) {
		this.refDeviceProduct = refDeviceProduct;
	}

	@Override
	protected String getProductPri() {
		return RheaCoreConstants.RHEA_PRI;
	}

	@Override
	protected String getTypePri() {
		return RheaCoreConstants.RheaPri.DEVICE_TYPE;
	}
}
