package com.arrow.rhea.data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.acn.client.model.RightToUseStatus;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.rhea.RheaCoreConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "rtu_request")
public class RTURequest extends AuditableDocumentAbstract {
	private static final long serialVersionUID = -1323823390385734863L;

	@NotEmpty
	private String firmwareVersion;
	@NotNull
	private RightToUseStatus status;
	@NotEmpty
	private String companyId;
	@NotEmpty
	private String softwareReleaseId;

	@Transient
	@JsonIgnore
	private Company refCompany;
	@Transient
	@JsonIgnore
	private SoftwareRelease refSoftwareRelease;

	@Override
	protected String getProductPri() {
		return RheaCoreConstants.RHEA_PRI;
	}

	@Override
	protected String getTypePri() {
		return RheaCoreConstants.RheaPri.RTU_REQUEST;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public RightToUseStatus getStatus() {
		return status;
	}

	public void setStatus(RightToUseStatus status) {
		this.status = status;
	}

	public Company getRefCompany() {
		return refCompany;
	}

	public void setRefCompany(Company refCompany) {
		this.refCompany = refCompany;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public SoftwareRelease getRefSoftwareRelease() {
		return refSoftwareRelease;
	}

	public void setRefSoftwareRelease(SoftwareRelease softwareRelease) {
		this.refSoftwareRelease = softwareRelease;
	}

	public String getSoftwareReleaseId() {
		return softwareReleaseId;
	}

	public void setSoftwareReleaseId(String softwareReleaseId) {
		this.softwareReleaseId = softwareReleaseId;
	}
}
