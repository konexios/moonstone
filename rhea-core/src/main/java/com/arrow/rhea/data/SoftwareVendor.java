package com.arrow.rhea.data;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.DefinitionCollectionAbstract;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.rhea.RheaCoreConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "software_vendor")
public class SoftwareVendor extends DefinitionCollectionAbstract {
	private static final long serialVersionUID = -371358884310151462L;

	@NotBlank
	private String companyId;
	private boolean editable = CoreConstant.DEFAULT_EDITABLE;

	@Transient
	@JsonIgnore
	private Company refCompany;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Company getRefCompany() {
		return refCompany;
	}

	public void setRefCompany(Company refCompany) {
		this.refCompany = refCompany;
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
		return RheaCoreConstants.RheaPri.SOFTWARE_VENDOR;
	}
}
