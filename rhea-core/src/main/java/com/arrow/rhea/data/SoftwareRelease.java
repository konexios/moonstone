package com.arrow.rhea.data;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.acn.client.model.RightToUseType;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.FileStore;
import com.arrow.pegasus.data.profile.Company;
import com.arrow.rhea.RheaCoreConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "software_release")
public class SoftwareRelease extends AuditableDocumentAbstract {
	private static final long serialVersionUID = -4207708236105382209L;

	private static final boolean DEFAULT_NO_LONGER_SUPPORTED = false;

	@NotBlank
	private String companyId;
	@NotBlank
	private String softwareProductId;
	@NotNull
	private Integer major;
	@NotNull
	private Integer minor;
	private Integer build;
	private String url;
	private boolean noLongerSupported = DEFAULT_NO_LONGER_SUPPORTED;
	private List<String> deviceTypeIds = new ArrayList<>();
	private List<String> upgradeableFromIds = new ArrayList<>();
	private boolean enabled = CoreConstant.DEFAULT_ENABLED;
	private String fileStoreId;
	@NotNull
	private RightToUseType rtuType;
	@Size(max = 50)
	private String ownerName;
	private String ownerEmail;

	@Transient
	@JsonIgnore
	private Company refCompany;
	@Transient
	@JsonIgnore
	private SoftwareProduct refSoftwareProduct;
	@Transient
	@JsonIgnore
	private FileStore refFileStore;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getSoftwareProductId() {
		return softwareProductId;
	}

	public void setSoftwareProductId(String softwareId) {
		this.softwareProductId = softwareId;
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

	public Integer getBuild() {
		return build;
	}

	public void setBuild(Integer build) {
		this.build = build;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isNoLongerSupported() {
		return noLongerSupported;
	}

	public void setNoLongerSupported(boolean noLongerSupported) {
		this.noLongerSupported = noLongerSupported;
	}

	public List<String> getDeviceTypeIds() {
		return deviceTypeIds;
	}

	public void setDeviceTypeIds(List<String> deviceTypeIds) {
		this.deviceTypeIds = deviceTypeIds;
	}

	public List<String> getUpgradeableFromIds() {
		return upgradeableFromIds;
	}

	public void setUpgradeableFromIds(List<String> upgradeableFromIds) {
		this.upgradeableFromIds = upgradeableFromIds;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getFileStoreId() {
		return fileStoreId;
	}

	public void setFileStoreId(String fileStoreId) {
		this.fileStoreId = fileStoreId;
	}

	public RightToUseType getRtuType() {
		return rtuType;
	}

	public void setRtuType(RightToUseType rtuType) {
		this.rtuType = rtuType;
	}

	public Company getRefCompany() {
		return refCompany;
	}

	public void setRefCompany(Company refCompany) {
		this.refCompany = refCompany;
	}

	public SoftwareProduct getRefSoftwareProduct() {
		return refSoftwareProduct;
	}

	public void setRefSoftwareProduct(SoftwareProduct refSoftwareProduct) {
		this.refSoftwareProduct = refSoftwareProduct;
	}

	public FileStore getRefFileStore() {
		return refFileStore;
	}

	public void setRefFileStore(FileStore refFileStore) {
		this.refFileStore = refFileStore;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@Override
	protected String getProductPri() {
		return RheaCoreConstants.RHEA_PRI;
	}

	@Override
	protected String getTypePri() {
		return RheaCoreConstants.RheaPri.SOFTWARE_RELEASE;
	}
}
