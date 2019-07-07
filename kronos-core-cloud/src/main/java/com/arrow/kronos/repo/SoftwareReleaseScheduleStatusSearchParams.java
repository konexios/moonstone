package com.arrow.kronos.repo;

import java.io.Serializable;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

import com.arrow.kronos.data.SoftwareReleaseSchedule;

public class SoftwareReleaseScheduleStatusSearchParams extends KronosDocumentSearchParams implements Serializable {
	private static final long serialVersionUID = -2563939772039111582L;

	private Set<String> objectIds;
	private EnumSet<SoftwareReleaseSchedule.Status> statuses;
	private Instant scheduledDate;
	private boolean onDemand;
	private Set<String> requestorIds;
	private Set<String> hids;
	private Instant startDate;
	private Instant completeDate;
	private Set<String> completedStatuses;
	private Set<String> deviceTypeIds;


	public Set<String> getObjectIds() {
		return objectIds;
	}

	public SoftwareReleaseScheduleStatusSearchParams addObjectIds(String... objectIds) {
		this.objectIds = super.addValues(this.objectIds, objectIds);

		return this;
	}
	
	public void setObjectIds(Set<String> objectIds) {
		this.objectIds = objectIds;
	}

	public EnumSet<SoftwareReleaseSchedule.Status> getStatuses() {
		return statuses;
	}

	public void setStatuses(EnumSet<SoftwareReleaseSchedule.Status> statuses) {
		this.statuses = statuses;
	}

	public Boolean getOnDemand() {
		return onDemand;
	}
	
	public void setOnDemand(boolean onDemand) {
		this.onDemand = onDemand;
	}
	
	public Set<String> getRequestorIds() {
		return requestorIds;
	}

	public SoftwareReleaseScheduleStatusSearchParams addRequestorIds(String... requestorIds) {
		this.requestorIds = super.addValues(this.requestorIds, requestorIds);

		return this;
	}
	
	public Set<String> getHids() {
		return hids;
	}

	public SoftwareReleaseScheduleStatusSearchParams addHids(String... hids) {
		this.hids = super.addValues(this.hids, hids);

		return this;
	}
	
	public Instant getStartDate() {
		return startDate;
	}

	public SoftwareReleaseScheduleStatusSearchParams setStartDate(Instant startDate) {
		this.startDate = startDate;

		return this;
	}
	
	public Instant getCompletedDate() {
		return completeDate;
	}

	public SoftwareReleaseScheduleStatusSearchParams setCompletedDate(Instant completeDate) {
		this.completeDate = completeDate;

		return this;
	}
	
	public Instant getScheduledDate() {
		return scheduledDate;
	}

	public SoftwareReleaseScheduleStatusSearchParams setScheduledDate(Instant scheduledDate) {
		this.scheduledDate = scheduledDate;

		return this;
	}
	
	public Set<String> getCompletedStatuses() {
		return completedStatuses;
	}

	public SoftwareReleaseScheduleStatusSearchParams addCompletedStatuses(String... completedStatuses) {
		this.completedStatuses = super.addValues(this.completedStatuses, completedStatuses);

		return this;
	}

	public Set<String> getDeviceTypeIds() {
		return deviceTypeIds;
	}

	public void setDeviceTypeIds(Set<String> deviceTypeIds) {
		this.deviceTypeIds = deviceTypeIds;
	}

	public SoftwareReleaseScheduleStatusSearchParams addDeviceTypeIds(String... deviceTypeIds) {
		this.deviceTypeIds = super.addValues(this.deviceTypeIds, deviceTypeIds);
		return this;
	}
}
