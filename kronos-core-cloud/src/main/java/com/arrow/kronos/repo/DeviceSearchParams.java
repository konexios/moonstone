package com.arrow.kronos.repo;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

public class DeviceSearchParams extends KronosDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = 7989620747482669194L;

	private Set<String> deviceTypeIds;
	private String uid;
	private Set<String> uids;
	private Set<String> partitionIds;
	private Set<String> names;
	private Set<String> userIds;
	private Set<String> gatewayIds;
	private Set<String> nodeIds;
	private Set<String> hids;
	private Set<String> tags;
	private Set<String> softwareReleaseIds;
	private Boolean softwareReleaseIdDefined;
	private Instant createdBefore;
	private Instant createdAfter;
	private Instant updatedBefore;
	private Instant updatedAfter;
	private Set<String> softwareNames;
	private Set<String> softwareVersions;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Set<String> getUids() {
		return uids;
	}

	public DeviceSearchParams addUids(String... uids) {
		this.uids = super.addValues(this.uids, uids);
		return this;
	}

	public Set<String> getHids() {
		return hids;
	}

	public DeviceSearchParams addHids(String... hids) {
		this.hids = super.addValues(this.hids, hids);
		return this;
	}

	public Set<String> getUserIds() {
		return super.getValues(userIds);
	}

	public DeviceSearchParams addUserIds(String... userIds) {
		this.userIds = super.addValues(this.userIds, userIds);

		return this;
	}

	public Set<String> getDeviceTypeIds() {
		return super.getValues(deviceTypeIds);
	}

	public DeviceSearchParams addDeviceTypeIds(String... deviceTypeIds) {
		this.deviceTypeIds = super.addValues(this.deviceTypeIds, deviceTypeIds);

		return this;
	}

	public Set<String> getPartitionIds() {
		return super.getValues(partitionIds);
	}

	public DeviceSearchParams addPartitionIds(String... partitionIds) {
		this.partitionIds = super.addValues(this.partitionIds, partitionIds);

		return this;
	}

	public Set<String> getNames() {
		return super.getValues(names);
	}

	public DeviceSearchParams addNames(String... names) {
		this.names = super.addValues(this.names, names);

		return this;
	}

	public Set<String> getGatewayIds() {
		return super.getValues(gatewayIds);
	}

	public DeviceSearchParams addGatewayIds(String... gatewayIds) {
		this.gatewayIds = super.addValues(this.gatewayIds, gatewayIds);

		return this;
	}

	public Set<String> getNodeIds() {
		return super.getValues(nodeIds);
	}

	public DeviceSearchParams addNodeIds(String... nodeIds) {
		this.nodeIds = super.addValues(this.nodeIds, nodeIds);

		return this;
	}

	public DeviceSearchParams addTags(String... tags) {
		this.tags = super.addValues(this.tags, tags);

		return this;
	}

	public Set<String> getTags() {
		return tags;
	}

	public DeviceSearchParams addSoftwareReleaseIds(String... softwareReleaseIds) {
		this.softwareReleaseIds = super.addValues(this.softwareReleaseIds, softwareReleaseIds);

		return this;
	}

	public Set<String> getSoftwareReleaseIds() {
		return softwareReleaseIds;
	}

	public Boolean getSoftwareReleaseIdDefined() {
		return softwareReleaseIdDefined;
	}

	public void setSoftwareReleaseIdDefined(Boolean softwareReleaseIdDefined) {
		this.softwareReleaseIdDefined = softwareReleaseIdDefined;
	}

	public Instant getCreatedBefore() {
		return createdBefore;
	}

	public void setCreatedBefore(Instant createdBefore) {
		this.createdBefore = createdBefore;
	}

	public DeviceSearchParams addCreatedBefore(Instant createdBefore) {
		setCreatedBefore(createdBefore);
		return this;
	}

	public Instant getCreatedAfter() {
		return createdAfter;
	}

	public void setCreatedAfter(Instant createdAfter) {
		this.createdAfter = createdAfter;
	}

	public Instant getUpdatedBefore() {
		return updatedBefore;
	}

	public void setUpdatedBefore(Instant updatedBefore) {
		this.updatedBefore = updatedBefore;
	}

	public Instant getUpdatedAfter() {
		return updatedAfter;
	}

	public void setUpdatedAfter(Instant updatedAfter) {
		this.updatedAfter = updatedAfter;
	}

	public DeviceSearchParams addSoftwareNames(String... softwareNames) {
		this.softwareNames = super.addValues(this.softwareNames, softwareNames);

		return this;
	}

	public Set<String> getSoftwareNames() {
		return softwareNames;
	}

	public DeviceSearchParams addSoftwareVersions(String... softwareVersions) {
		this.softwareVersions = super.addValues(this.softwareVersions, softwareVersions);

		return this;
	}

	public Set<String> getSoftwareVersions() {
		return softwareVersions;
	}
}
