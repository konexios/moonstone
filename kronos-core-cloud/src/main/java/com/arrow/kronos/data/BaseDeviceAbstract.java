package com.arrow.kronos.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;

import com.arrow.acs.client.model.YesNoInherit;
import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.Enabled;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.arrow.rhea.data.SoftwareRelease;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BaseDeviceAbstract extends AuditableDocumentAbstract implements Enabled {
	private static final long serialVersionUID = -8929192620898064938L;

	@NotBlank
	private String applicationId;
	// @NotBlank
	private String deviceTypeId;
	@NotBlank
	private String uid;
	@NotBlank
	private String name;
	private String userId;
	private String nodeId;
	private String externalId;
	private String softwareReleaseId;
	private boolean enabled = CoreConstant.DEFAULT_ENABLED;
	private Map<String, String> info = new HashMap<>();
	private Map<String, String> properties = new HashMap<>();
	private Set<String> tags = new HashSet<>();
	private YesNoInherit persistTelemetry = YesNoInherit.INHERIT;
	private YesNoInherit indexTelemetry = YesNoInherit.INHERIT;
	// @NotBlank
	private String softwareName;
	// @NotBlank
	private String softwareVersion;

	@Transient
	@JsonIgnore
	private Application refApplication;
	@Transient
	@JsonIgnore
	private DeviceType refDeviceType;
	@Transient
	@JsonIgnore
	private User refUser;
	@Transient
	@JsonIgnore
	private Node refNode;
	@Transient
	@JsonIgnore
	private SoftwareRelease refSoftwareRelease;
	@Transient
	@JsonIgnore
	private KronosApplication refKronosApplication;

	public KronosApplication getRefKronosApplication() {
		return refKronosApplication;
	}

	public void setRefKronosApplication(KronosApplication refKronosApplication) {
		this.refKronosApplication = refKronosApplication;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getSoftwareReleaseId() {
		return softwareReleaseId;
	}

	public void setSoftwareReleaseId(String softwareReleaseId) {
		this.softwareReleaseId = softwareReleaseId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Map<String, String> getInfo() {
		return info;
	}

	public void setInfo(Map<String, String> info) {
		this.info = info;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public YesNoInherit getPersistTelemetry() {
		return persistTelemetry;
	}

	public void setPersistTelemetry(YesNoInherit persistTelemetry) {
		this.persistTelemetry = persistTelemetry;
	}

	public YesNoInherit getIndexTelemetry() {
		return indexTelemetry;
	}

	public void setIndexTelemetry(YesNoInherit indexTelemetry) {
		this.indexTelemetry = indexTelemetry;
	}

	public String getSoftwareName() {
		return softwareName;
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName = softwareName;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public DeviceType getRefDeviceType() {
		return refDeviceType;
	}

	public void setRefDeviceType(DeviceType refDeviceType) {
		this.refDeviceType = refDeviceType;
	}

	public User getRefUser() {
		return refUser;
	}

	public void setRefUser(User refUser) {
		this.refUser = refUser;
	}

	public Node getRefNode() {
		return refNode;
	}

	public void setRefNode(Node refNode) {
		this.refNode = refNode;
	}

	public SoftwareRelease getRefSoftwareRelease() {
		return refSoftwareRelease;
	}

	public void setRefSoftwareRelease(SoftwareRelease refSoftwareRelease) {
		this.refSoftwareRelease = refSoftwareRelease;
	}
}
