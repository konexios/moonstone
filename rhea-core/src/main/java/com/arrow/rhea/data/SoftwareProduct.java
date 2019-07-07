package com.arrow.rhea.data;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.rhea.RheaCoreConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "software_product")
public class SoftwareProduct extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = 7928546310350821468L;

	@NotBlank
	private String companyId;
	@NotBlank
	private String softwareVendorId;
	private boolean editable = CoreConstant.DEFAULT_EDITABLE;

	@Transient
	@JsonIgnore
	private Company refCompany;
	@Transient
	@JsonIgnore
	private SoftwareVendor refSoftwareVendor;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getSoftwareVendorId() {
		return softwareVendorId;
	}

	public void setSoftwareVendorId(String softwareVendorId) {
		this.softwareVendorId = softwareVendorId;
	}

	public Company getRefCompany() {
		return refCompany;
	}

	public void setRefCompany(Company refCompany) {
		this.refCompany = refCompany;
	}

	public SoftwareVendor getRefSoftwareVendor() {
		return refSoftwareVendor;
	}

	public void setRefSoftwareVendor(SoftwareVendor refSoftwareVendor) {
		this.refSoftwareVendor = refSoftwareVendor;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	protected String getProductPri() {
		return RheaCoreConstants.RHEA_PRI;
	}

	@Override
	protected String getTypePri() {
		return RheaCoreConstants.RheaPri.SOFTWARE_PRODUCT;
	}
}