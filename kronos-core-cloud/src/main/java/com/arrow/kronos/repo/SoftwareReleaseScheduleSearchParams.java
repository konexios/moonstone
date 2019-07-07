package com.arrow.kronos.repo;

import java.io.Serializable;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.kronos.data.SoftwareReleaseSchedule;

public class SoftwareReleaseScheduleSearchParams extends KronosDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = -2563939772039111582L;

	private Instant fromScheduledDate;
	private Instant toScheduledDate;
	private Set<String> softwareReleaseIds;
	// private Set<String> deviceCategoryIds;
	private EnumSet<AcnDeviceCategory> deviceCategories;
	private Set<String> objectIds;
	private EnumSet<SoftwareReleaseSchedule.Status> statuses;
	private Boolean notifyOnStart;
	private Boolean notifyOnEnd;
	private Boolean notifyOnSubmit;
	private Boolean onDemand;
	private String name;
	private Set<String> deviceTypeIds;
	private Set<String> hardwareVersionIds;

	public Instant getFromScheduledDate() {
		return fromScheduledDate;
	}

	public void setFromScheduledDate(Instant fromScheduledDate) {
		this.fromScheduledDate = fromScheduledDate;
	}

	public Instant getToScheduledDate() {
		return toScheduledDate;
	}

	public void setToScheduledDate(Instant toScheduledDate) {
		this.toScheduledDate = toScheduledDate;
	}

	public Set<String> getSoftwareReleaseIds() {
		return softwareReleaseIds;
	}

	public SoftwareReleaseScheduleSearchParams addSoftwareReleaseIds(String... softwareReleaseIds) {
		this.softwareReleaseIds = super.addValues(this.softwareReleaseIds, softwareReleaseIds);

		return this;
	}

	// public Set<String> getDeviceCategoryIds() {
	// return deviceCategoryIds;
	// }
	//
	// public SoftwareReleaseScheduleSearchParams addDeviceCategoryIds(String...
	// deviceCategoryIds) {
	// this.deviceCategoryIds = super.addValues(this.deviceCategoryIds,
	// deviceCategoryIds);
	//
	// return this;
	// }

	public EnumSet<AcnDeviceCategory> getDeviceCategories() {
		return deviceCategories;
	}

	public void setDeviceCategories(EnumSet<AcnDeviceCategory> deviceCategories) {
		this.deviceCategories = deviceCategories;
	}

	public Set<String> getObjectIds() {
		return objectIds;
	}

	public SoftwareReleaseScheduleSearchParams addObjectIds(String... objectIds) {
		this.objectIds = super.addValues(this.objectIds, objectIds);

		return this;
	}

	public EnumSet<SoftwareReleaseSchedule.Status> getStatuses() {
		return statuses;
	}

	public void setStatuses(EnumSet<SoftwareReleaseSchedule.Status> statuses) {
		this.statuses = statuses;
	}

	public Boolean getNotifyOnStart() {
		return notifyOnStart;
	}

	public void setNotifyOnStart(Boolean notifyOnStart) {
		this.notifyOnStart = notifyOnStart;
	}

	public Boolean getNotifyOnEnd() {
		return notifyOnEnd;
	}

	public void setNotifyOnEnd(Boolean notifyOnEnd) {
		this.notifyOnEnd = notifyOnEnd;
	}

	public Boolean getNotifyOnSubmit() {
		return notifyOnSubmit;
	}

	public void setNotifyOnSubmit(Boolean notifyOnSubmit) {
		this.notifyOnSubmit = notifyOnSubmit;
	}

	public Boolean getOnDemand() {
		return onDemand;
	}

	public void setOnDemand(Boolean onDemand) {
		this.onDemand = onDemand;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getDeviceTypeIds() {
		return deviceTypeIds;
	}

	public void setDeviceTypeIds(Set<String> deviceTypeIds) {
		this.deviceTypeIds = deviceTypeIds;
	}

	public SoftwareReleaseScheduleSearchParams addDeviceTypeIds(String... deviceTypeIds) {
		this.deviceTypeIds = super.addValues(this.deviceTypeIds, deviceTypeIds);
		return this;
	}

	public Set<String> getHardwareVersionIds() {
		return hardwareVersionIds;
	}

	public void setHardwareVersionIds(Set<String> hardwareVersionIds) {
		this.hardwareVersionIds = hardwareVersionIds;
	}

	public SoftwareReleaseScheduleSearchParams addHardwareVersionIds(String... hardwareVersionIds) {
		this.hardwareVersionIds = super.addValues(this.hardwareVersionIds, hardwareVersionIds);
		return this;
	}
}
