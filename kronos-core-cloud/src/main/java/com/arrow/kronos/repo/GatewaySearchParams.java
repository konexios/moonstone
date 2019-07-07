package com.arrow.kronos.repo;

import java.io.Serializable;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

import com.arrow.pegasus.data.GatewayType;
import com.arrow.pegasus.repo.params.PegasusDocumentSearchParams;

public class GatewaySearchParams extends PegasusDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = 6202831517260674890L;

	private Set<String> uids;
	private Set<String> userIds;
	private EnumSet<GatewayType> gatewayTypes;
	private Set<String> deviceTypeIds;
	private Set<String> nodeIds;
	private Set<String> hids;
	private Set<String> osNames;
	private Set<String> softwareNames;
	private Set<String> softwareVersions;
	private Set<String> softwareReleaseIds;
	private Boolean softwareReleaseIdDefined;
	private Instant createdBefore;
	private Instant createdAfter;
	private Instant updatedBefore;
	private Instant updatedAfter;

	public GatewaySearchParams addUids(String... uids) {
		this.uids = super.addValues(this.uids, uids);

		return this;
	}

	public Set<String> getUids() {
		return uids;
	}
	public Set<String> getHids() {
		return hids;
	}

	public GatewaySearchParams addHids(String... hids) {
		this.hids = super.addValues(this.hids, hids);
		return this;
	}

	public GatewaySearchParams addUserIds(String... userIds) {
		this.userIds = super.addValues(this.userIds, userIds);

		return this;
	}

	public Set<String> getUserIds() {
		return userIds;
	}

	public EnumSet<GatewayType> getGatewayTypes() {
		return gatewayTypes;
	}

	public GatewaySearchParams addGatewayTypes(EnumSet<GatewayType> gatewayTypes) {

		if (this.gatewayTypes != null) {
			for (GatewayType t : gatewayTypes)
				this.gatewayTypes.add(t);
		} else {
			this.gatewayTypes = gatewayTypes;
		}

		return this;
	}

	public GatewaySearchParams addDeviceTypeIds(String... deviceTypeIds) {
		this.deviceTypeIds = super.addValues(this.deviceTypeIds, deviceTypeIds);

		return this;
	}

	public Set<String> getDeviceTypeIds() {
		return deviceTypeIds;
	}

	public Set<String> getNodeIds() {
		return super.getValues(nodeIds);
	}

	public GatewaySearchParams addNodeIds(String... nodeIds) {
		this.nodeIds = super.addValues(this.nodeIds, nodeIds);

		return this;
	}

	public GatewaySearchParams addOsNames(String... osNames) {
		this.osNames = super.addValues(this.osNames, osNames);

		return this;
	}

	public Set<String> getOsNames() {
		return osNames;
	}

	public GatewaySearchParams addSoftwareNames(String... softwareNames) {
		this.softwareNames = super.addValues(this.softwareNames, softwareNames);

		return this;
	}

	public Set<String> getSoftwareNames() {
		return softwareNames;
	}

	public GatewaySearchParams addSoftwareVersions(String... softwareVersions) {
		this.softwareVersions = super.addValues(this.softwareVersions, softwareVersions);

		return this;
	}

	public Set<String> getSoftwareVersions() {
		return softwareVersions;
	}

	public GatewaySearchParams addSoftwareReleaseIds(String... softwareReleaseIds) {
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
}
