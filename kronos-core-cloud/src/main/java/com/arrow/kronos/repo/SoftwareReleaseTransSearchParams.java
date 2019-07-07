package com.arrow.kronos.repo;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.kronos.data.SoftwareReleaseTrans;

public class SoftwareReleaseTransSearchParams extends KronosDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = 3405784362510255315L;

	private Set<String> objectIds;
	private EnumSet<AcnDeviceCategory> deviceCategories;
	private Set<String> softwareReleaseScheduleIds;
	private Set<String> notSoftwareReleaseScheduleIds;
	private Set<String> fromSoftwareReleaseIds;
	private Set<String> toSoftwareReleaseIds;
	private EnumSet<SoftwareReleaseTrans.Status> statuses;
	private String error;
	private Set<String> relatedSoftwareReleaseTransIds;

	public Set<String> getObjectIds() {
		return objectIds;
	}

	public SoftwareReleaseTransSearchParams addObjectIds(String... objectIds) {
		this.objectIds = super.addValues(this.objectIds, objectIds);

		return this;
	}

	public EnumSet<AcnDeviceCategory> getDeviceCategories() {
		return deviceCategories;
	}

	public void setDeviceCategories(EnumSet<AcnDeviceCategory> deviceCategories) {
		this.deviceCategories = deviceCategories;
	}

	public Set<String> getSoftwareReleaseScheduleIds() {
		return softwareReleaseScheduleIds;
	}

	public SoftwareReleaseTransSearchParams addSoftwareReleaseScheduleIds(String... softwareReleaseScheduleIds) {
		this.softwareReleaseScheduleIds = super.addValues(this.softwareReleaseScheduleIds, softwareReleaseScheduleIds);

		return this;
	}

	public Set<String> getNotSoftwareReleaseScheduleIds() {
		return notSoftwareReleaseScheduleIds;
	}

	public SoftwareReleaseTransSearchParams addNotSoftwareReleaseScheduleIds(String... notSoftwareReleaseScheduleIds) {
		this.notSoftwareReleaseScheduleIds = super.addValues(this.notSoftwareReleaseScheduleIds,
		        notSoftwareReleaseScheduleIds);

		return this;
	}

	public Set<String> getFromSoftwareReleaseIds() {
		return fromSoftwareReleaseIds;
	}

	public SoftwareReleaseTransSearchParams addFromSoftwareReleaseIds(String... fromSoftwareReleaseIds) {
		this.fromSoftwareReleaseIds = super.addValues(this.fromSoftwareReleaseIds, fromSoftwareReleaseIds);

		return this;
	}

	public Set<String> getToSoftwareReleaseIds() {
		return toSoftwareReleaseIds;
	}

	public SoftwareReleaseTransSearchParams addToSoftwareReleaseIds(String... toSoftwareReleaseIds) {
		this.toSoftwareReleaseIds = super.addValues(this.toSoftwareReleaseIds, toSoftwareReleaseIds);

		return this;
	}

	public EnumSet<SoftwareReleaseTrans.Status> getStatuses() {
		return statuses;
	}

	public void setStatuses(EnumSet<SoftwareReleaseTrans.Status> statuses) {
		this.statuses = statuses;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Set<String> getRelatedSoftwareReleaseTransIds() {
		return relatedSoftwareReleaseTransIds;
	}

	public SoftwareReleaseTransSearchParams addReleatedSoftwareReleaseTransIds(
	        String... relatedSoftwareReleaseTransIds) {
		this.relatedSoftwareReleaseTransIds = super.addValues(this.relatedSoftwareReleaseTransIds,
		        relatedSoftwareReleaseTransIds);

		return this;
	}
}