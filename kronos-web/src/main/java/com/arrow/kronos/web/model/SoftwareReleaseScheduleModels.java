package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.kronos.data.BaseDeviceAbstract;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.data.SoftwareReleaseSchedule.Status;
import com.arrow.kronos.data.SoftwareReleaseTrans;
import com.arrow.kronos.web.model.DeviceTypeModels.DeviceTypeOption;
import com.arrow.kronos.web.model.RheaModels.SoftwareReleaseOption;
import com.arrow.pegasus.webapi.data.CoreDefinitionModelOption;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;
import com.arrow.pegasus.webapi.data.CoreUserModels.UserOption;

public class SoftwareReleaseScheduleModels {

	public static class AvailableFirmwareVersion implements Serializable {
		private static final long serialVersionUID = -5425557624114264695L;

		private String softwareReleaseId;
		private String softwareReleaseName;

		public AvailableFirmwareVersion() {
		}

		public AvailableFirmwareVersion(String softwareReleaseId, String softwareReleaseName) {
			this.softwareReleaseId = softwareReleaseId;
			this.softwareReleaseName = softwareReleaseName;
		}

		public String getSoftwareReleaseId() {
			return softwareReleaseId;
		}

		public void setSoftwareReleaseId(String softwareReleaseId) {
			this.softwareReleaseId = softwareReleaseId;
		}

		public String getSoftwareReleaseName() {
			return softwareReleaseName;
		}

		public void setSoftwareReleaseName(String softwareReleaseName) {
			this.softwareReleaseName = softwareReleaseName;
		}
	}

	public static class EligibleFirmwareChangeGroup implements Serializable {
		private static final long serialVersionUID = 3456782850563131128L;

		private String assetTypeName;
		private String assetTypeId;
		private long numberOfAssets;
		private String hardwareVersionName;
		private String currentFirmwareVersionName;
		private List<AvailableFirmwareVersion> availableFirmwareVersionNames = new ArrayList<AvailableFirmwareVersion>();

		public EligibleFirmwareChangeGroup() {
		}

		public String getAssetTypeName() {
			return assetTypeName;
		}

		public void setAssetTypeName(String assetTypeName) {
			this.assetTypeName = assetTypeName;
		}

		public String getAssetTypeId() {
			return assetTypeId;
		}

		public void setAssetTypeId(String assetTypeId) {
			this.assetTypeId = assetTypeId;
		}

		public long getNumberOfAssets() {
			return numberOfAssets;
		}

		public void setNumberOfAssets(long numberOfAssets) {
			this.numberOfAssets = numberOfAssets;
		}

		public String getHardwareVersionName() {
			return hardwareVersionName;
		}

		public void setHardwareVersionName(String hardwareVersionName) {
			this.hardwareVersionName = hardwareVersionName;
		}

		public String getCurrentFirmwareVersionName() {
			return currentFirmwareVersionName;
		}

		public void setCurrentFirmwareVersionName(String currentFirmwareVersionName) {
			this.currentFirmwareVersionName = currentFirmwareVersionName;
		}

		public List<AvailableFirmwareVersion> getAvailableFirmwareVersionNames() {
			return availableFirmwareVersionNames;
		}

		public void setAvailableFirmwareVersionNames(List<AvailableFirmwareVersion> availableFirmwareVersionNames) {
			if (availableFirmwareVersionNames != null)
				this.availableFirmwareVersionNames = availableFirmwareVersionNames;
		}

		public EligibleFirmwareChangeGroup withAssetTypeName(String assetTypeName) {
			setAssetTypeName(assetTypeName);

			return this;
		}

		public EligibleFirmwareChangeGroup withAssetTypeId(String assetTypeId) {
			setAssetTypeId(assetTypeId);

			return this;
		}

		public EligibleFirmwareChangeGroup withNumberOfAssets(long numberOfAssets) {
			setNumberOfAssets(numberOfAssets);

			return this;
		}

		public EligibleFirmwareChangeGroup withHardwareVersionName(String hardwareVersionName) {
			setHardwareVersionName(hardwareVersionName);

			return this;
		}

		public EligibleFirmwareChangeGroup withCurrentFirmwareVersion(String currentFirmwareVersion) {
			setCurrentFirmwareVersionName(currentFirmwareVersion);

			return this;
		}

		public EligibleFirmwareChangeGroup withAvailableFirmwareVersionNames(
				List<AvailableFirmwareVersion> availableFirmwareVersionNames) {
			setAvailableFirmwareVersionNames(availableFirmwareVersionNames);

			return this;
		}
	}

	public static class SoftwareReleaseScheduleList extends CoreDocumentModel {
		private static final long serialVersionUID = 3935243319087674743L;

		private long scheduledDate;
		private String softwareReleaseName;
		private Status status;
		private boolean notifyOnStart;
		private boolean notifyOnEnd;

		public SoftwareReleaseScheduleList(SoftwareReleaseSchedule softwareReleaseSchedule,
				String softwareReleaseName) {
			super(softwareReleaseSchedule.getId(), softwareReleaseSchedule.getHid());

			this.scheduledDate = softwareReleaseSchedule.getScheduledDate().toEpochMilli();
			this.softwareReleaseName = softwareReleaseName;
			this.status = softwareReleaseSchedule.getStatus();
			this.notifyOnStart = softwareReleaseSchedule.isNotifyOnStart();
			this.notifyOnEnd = softwareReleaseSchedule.isNotifyOnEnd();
		}

		public long getScheduledDate() {
			return scheduledDate;
		}

		public void setScheduledDate(long scheduledDate) {
			this.scheduledDate = scheduledDate;
		}

		public String getSoftwareReleaseName() {
			return softwareReleaseName;
		}

		public void setSoftwareReleaseName(String softwareReleaseName) {
			this.softwareReleaseName = softwareReleaseName;
		}

		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		public boolean isNotifyOnStart() {
			return notifyOnStart;
		}

		public void setNotifyOnStart(boolean notifyOnStart) {
			this.notifyOnStart = notifyOnStart;
		}

		public boolean isNotifyOnEnd() {
			return notifyOnEnd;
		}

		public void setNotifyOnEnd(boolean notifyOnEnd) {
			this.notifyOnEnd = notifyOnEnd;
		}

	}

	public static class SoftwareReleaseScheduleSelectionOptions implements Serializable {
		private static final long serialVersionUID = -291898569039562292L;

		private List<DeviceTypeOption> deviceTypes;
		private List<SoftwareReleaseOption> softwareReleases;
		private List<String> timezones;
		private List<ObjectModel> availableObjects;
		private String defaultSoftwareReleaseEmails;
		private SoftwareReleaseScheduleSelection selection;

		public List<DeviceTypeOption> getDeviceTypes() {
			return deviceTypes;
		}

		public void setDeviceTypes(List<DeviceTypeOption> deviceTypes) {
			this.deviceTypes = deviceTypes;
		}

		public List<SoftwareReleaseOption> getSoftwareReleases() {
			return softwareReleases;
		}

		public void setSoftwareReleases(List<SoftwareReleaseOption> softwareReleases) {
			this.softwareReleases = softwareReleases;
		}

		public List<String> getTimezones() {
			return timezones;
		}

		public void setTimezones(List<String> timezones) {
			this.timezones = timezones;
		}

		public List<ObjectModel> getAvailableObjects() {
			return availableObjects;
		}

		public void setAvailableObjects(List<ObjectModel> availableObjects) {
			this.availableObjects = availableObjects;
		}

		public String getDefaultSoftwareReleaseEmails() {
			return defaultSoftwareReleaseEmails;
		}

		public void setDefaultSoftwareReleaseEmails(String defaultSoftwareReleaseEmails) {
			this.defaultSoftwareReleaseEmails = defaultSoftwareReleaseEmails;
		}

		public SoftwareReleaseScheduleSelection getSelection() {
			return selection;
		}

		public void setSelection(SoftwareReleaseScheduleSelection selection) {
			this.selection = selection;
		}

	}

	public static class SoftwareReleaseScheduleSelection implements Serializable {
		private static final long serialVersionUID = 8000701095941084298L;

		private String softwareReleaseScheduleId;
		private String deviceTypeId;
		private String softwareReleaseId;
		private AcnDeviceCategory deviceCategory;

		public String getSoftwareReleaseScheduleId() {
			return softwareReleaseScheduleId;
		}

		public void setSoftwareReleaseScheduleId(String softwareReleaseScheduleId) {
			this.softwareReleaseScheduleId = softwareReleaseScheduleId;
		}

		public String getDeviceTypeId() {
			return deviceTypeId;
		}

		public void setDeviceTypeId(String deviceTypeId) {
			this.deviceTypeId = deviceTypeId;
		}

		public String getSoftwareReleaseId() {
			return softwareReleaseId;
		}

		public void setSoftwareReleaseId(String softwareReleaseId) {
			this.softwareReleaseId = softwareReleaseId;
		}

		public AcnDeviceCategory getDeviceCategory() {
			return deviceCategory;
		}

		public void setDeviceCategory(AcnDeviceCategory deviceCategory) {
			this.deviceCategory = deviceCategory;
		}
	}

	public static class SoftwareReleaseScheduleDetailsModel extends CoreDocumentModel {
		private static final long serialVersionUID = 3729221553515187373L;

		private String jobName;
		private Long scheduledDate;
		private String localTimezone;
		private String targetTimezone;
		private String softwareReleaseId;
		private String deviceTypeId;
		private AcnDeviceCategory deviceCategory;
		private String comments;
		private List<String> objectIds;
		private boolean notifyOnSubmitted;
		private boolean notifyOnStart;
		private boolean notifyOnEnd;
		private String notifyEmails;
		private boolean onDemand;
		@Deprecated
		private Long transactionExpiration;
		private Long timeToExpireSeconds;

		public String getJobName() {
			return jobName;
		}

		public void setJobName(String jobName) {
			this.jobName = jobName;
		}

		public SoftwareReleaseScheduleDetailsModel() {
			super(null, null);
		}

		public SoftwareReleaseScheduleDetailsModel(SoftwareReleaseSchedule softwareReleaseSchedule) {
			super(softwareReleaseSchedule.getId(), softwareReleaseSchedule.getHid());

			if (softwareReleaseSchedule.getScheduledDate() != null) {
				this.scheduledDate = softwareReleaseSchedule.getScheduledDate().toEpochMilli();
			}

			this.softwareReleaseId = softwareReleaseSchedule.getSoftwareReleaseId();
			this.deviceTypeId = softwareReleaseSchedule.getDeviceTypeId();
			this.deviceCategory = softwareReleaseSchedule.getDeviceCategory();
			this.comments = softwareReleaseSchedule.getComments();
			this.notifyOnSubmitted = softwareReleaseSchedule.getNotifyOnSubmit();
			this.notifyOnStart = softwareReleaseSchedule.isNotifyOnStart();
			this.notifyOnEnd = softwareReleaseSchedule.isNotifyOnEnd();
			this.notifyEmails = softwareReleaseSchedule.getNotifyEmails();
			this.objectIds = softwareReleaseSchedule.getObjectIds();
			this.jobName = softwareReleaseSchedule.getName();
			this.onDemand = softwareReleaseSchedule.getOnDemand();
			this.transactionExpiration = softwareReleaseSchedule.getTransactionExpiration();
			this.timeToExpireSeconds = softwareReleaseSchedule.getTimeToExpireSeconds();
		}

		public Long getScheduledDate() {
			return scheduledDate;
		}

		public void setScheduledDate(Long scheduledDate) {
			this.scheduledDate = scheduledDate;
		}

		public String getLocalTimezone() {
			return localTimezone;
		}

		public void setLocalTimezone(String localTimezone) {
			this.localTimezone = localTimezone;
		}

		public String getTargetTimezone() {
			return targetTimezone;
		}

		public void setTargetTimezone(String targetTimezone) {
			this.targetTimezone = targetTimezone;
		}

		public String getSoftwareReleaseId() {
			return softwareReleaseId;
		}

		public void setSoftwareReleaseId(String softwareReleaseId) {
			this.softwareReleaseId = softwareReleaseId;
		}

		public String getDeviceTypeId() {
			return deviceTypeId;
		}

		public void setDeviceTypeId(String deviceTypeId) {
			this.deviceTypeId = deviceTypeId;
		}

		public AcnDeviceCategory getDeviceCategory() {
			return deviceCategory;
		}

		public void setDeviceCategory(AcnDeviceCategory deviceCategory) {
			this.deviceCategory = deviceCategory;
		}

		public String getComments() {
			return comments;
		}

		public void setComments(String comments) {
			this.comments = comments;
		}

		public List<String> getObjectIds() {
			return objectIds;
		}

		public void setObjectIds(List<String> objectIds) {
			this.objectIds = objectIds;
		}

		public boolean isNotifyOnSubmitted() {
			return notifyOnSubmitted;
		}

		public void setNotifyOnSubmitted(boolean notifyOnSubmitted) {
			this.notifyOnSubmitted = notifyOnSubmitted;
		}

		public boolean isNotifyOnStart() {
			return notifyOnStart;
		}

		public void setNotifyOnStart(boolean notifyOnStart) {
			this.notifyOnStart = notifyOnStart;
		}

		public boolean isNotifyOnEnd() {
			return notifyOnEnd;
		}

		public void setNotifyOnEnd(boolean notifyOnEnd) {
			this.notifyOnEnd = notifyOnEnd;
		}

		public String getNotifyEmails() {
			return notifyEmails;
		}

		public void setNotifyEmails(String notifyEmails) {
			this.notifyEmails = notifyEmails;
		}

		public boolean isOnDemand() {
			return onDemand;
		}

		public void setOnDemand(boolean onDemand) {
			this.onDemand = onDemand;
		}

		public Long getTransactionExpiration() {
			return transactionExpiration;
		}

		public void setTransactionExpiration(Long transactionExpiration) {
			this.transactionExpiration = transactionExpiration;
		}

		public Long getTimeToExpireSeconds() {
			return timeToExpireSeconds;
		}

		public void setTimeToExpireSeconds(Long timeToExpireSeconds) {
			this.timeToExpireSeconds = timeToExpireSeconds;
		}
	}

	public static class ObjectModel extends CoreDocumentModel {
		private static final long serialVersionUID = 2341478415102374300L;

		private String name;
		private String ownerId;
		private String ownerName;
		private String nodeId;
		private String nodeName;
		private String softwareReleaseId;
		private String softwareReleaseName;
		private String typeId;
		private String uid;
		private String hwVersionName;
		private boolean isScheduled;
		private Long latestSoftwareUpgrade;

		public ObjectModel() {
			super(null, null);
		}

		public ObjectModel(Device device, boolean isScheduled) {
			super(device.getId(), device.getHid());

			name = device.getName();
			ownerId = device.getUserId();
			nodeId = device.getNodeId();
			softwareReleaseId = device.getSoftwareReleaseId();
			typeId = device.getDeviceTypeId();
			uid = device.getUid();
			this.isScheduled = isScheduled;
			// TODO: Fix me
			latestSoftwareUpgrade = null;
		}

		public ObjectModel(Gateway gateway, boolean isScheduled) {
			super(gateway.getId(), gateway.getHid());

			name = gateway.getName();
			ownerId = gateway.getUserId();
			nodeId = gateway.getNodeId();
			softwareReleaseId = gateway.getSoftwareReleaseId();
			typeId = gateway.getDeviceTypeId();
			uid = gateway.getUid();
			this.isScheduled = isScheduled;
			// TODO: Fix me
			latestSoftwareUpgrade = null;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getOwnerId() {
			return ownerId;
		}

		public void setOwnerId(String ownerId) {
			this.ownerId = ownerId;
		}

		public String getOwnerName() {
			return ownerName;
		}

		public void setOwnerName(String ownerName) {
			this.ownerName = ownerName;
		}

		public String getNodeId() {
			return nodeId;
		}

		public void setNodeId(String nodeId) {
			this.nodeId = nodeId;
		}

		public String getNodeName() {
			return nodeName;
		}

		public void setNodeName(String nodeName) {
			this.nodeName = nodeName;
		}

		public String getSoftwareReleaseId() {
			return softwareReleaseId;
		}

		public void setSoftwareReleaseId(String softwareReleaseId) {
			this.softwareReleaseId = softwareReleaseId;
		}

		public String getSoftwareReleaseName() {
			return softwareReleaseName;
		}

		public void setSoftwareReleaseName(String softwareReleaseName) {
			this.softwareReleaseName = softwareReleaseName;
		}

		public String getTypeId() {
			return typeId;
		}

		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getHwVersionName() {
			return hwVersionName;
		}

		public void setHwVersionName(String hwVersionName) {
			this.hwVersionName = hwVersionName;
		}

		public Long getLatestSoftwareUpgrade() {
			return latestSoftwareUpgrade;
		}

		public void setLatestSoftwareUpgrade(Long latestSoftwareUpgrade) {
			this.latestSoftwareUpgrade = latestSoftwareUpgrade;
		}

		public boolean isScheduled() {
			return isScheduled;
		}

		public void setScheduled(boolean scheduled) {
			isScheduled = scheduled;
		}
	}

	public static class SoftwareReleaseSchedulePendingOptions implements Serializable {
		private static final long serialVersionUID = -291898569039562292L;

		private List<DeviceTypeOption> deviceTypes;
		private List<UserOption> requestors;
		private List<String> startDates;
		private List<String> completedDates;
		private List<CompletedStatus> statuses;

		public List<DeviceTypeOption> getDeviceTypes() {
			return deviceTypes;
		}

		public void setDeviceTypes(List<DeviceTypeOption> deviceTypes) {
			this.deviceTypes = deviceTypes;
		}

		public List<UserOption> getRequestors() {
			return requestors;
		}

		public void setRequestors(List<UserOption> requestorsOptions) {
			this.requestors = requestorsOptions;
		}

		public List<String> getStartDates() {
			return startDates;
		}

		public void setStartDates(List<String> startDates) {
			this.startDates = startDates;
		}

		public List<String> getCompletedDates() {
			return completedDates;
		}

		public void setCompletedDates(List<String> completedDates) {
			this.completedDates = completedDates;
		}

		public List<CompletedStatus> getStatuses() {
			return statuses;
		}

		public void setStatuses(List<CompletedStatus> statuses) {
			this.statuses = statuses;
		}
	}

	public static class SoftwareReleaseScheduleSummaryOptions implements Serializable {
		private static final long serialVersionUID = 8000701095941084298L;

		private long totalDevices;
		private long totalGateways;
		private long pendingJobs;
		private long inProgressJobs;
		private long processedJobs;
		private long eligibleUpgrades;

		public long getTotalDevices() {
			return totalDevices;
		}

		public void setTotalDevices(long totalDevices) {
			this.totalDevices = totalDevices;
		}

		public long getTotalGateways() {
			return totalGateways;
		}

		public void setTotalGateways(long totalGateways) {
			this.totalGateways = totalGateways;
		}

		public long getPendingJobs() {
			return pendingJobs;
		}

		public void setPendingJobs(long pendingJobs) {
			this.pendingJobs = pendingJobs;
		}

		public long getInProgressJobs() {
			return inProgressJobs;
		}

		public void setInProgressJobs(long inProgressJobs) {
			this.inProgressJobs = inProgressJobs;
		}

		public long getProcessedJobs() {
			return processedJobs;
		}

		public void setProcessedJobs(long processedJobs) {
			this.processedJobs = processedJobs;
		}

		public long getEligibleUpgrades() {
			return eligibleUpgrades;
		}

		public void setEligibleUpgrades(long eligibleUpgrades) {
			this.eligibleUpgrades = eligibleUpgrades;
		}
	}

	public static class RequestorOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = -5200848137968936124L;

		public RequestorOption() {
			super(null, null, null);
		}

		public RequestorOption(DeviceType deviceType) {
			super(deviceType.getId(), deviceType.getHid(), deviceType.getName());
		}
	}

	public static class SoftwareReleaseTransProgressMetrics implements Serializable {
		private static final long serialVersionUID = 6688182360759057294L;

		private SoftwareReleaseTransStateMetrics pending;
		private SoftwareReleaseTransStateMetrics inprogress;
		private SoftwareReleaseTransStateMetrics received;
		private SoftwareReleaseTransStateMetrics complete;
		private SoftwareReleaseTransStateMetrics cancelled;
		private SoftwareReleaseTransStateMetrics expired;
		private SoftwareReleaseTransStateMetrics failed;
		private SoftwareReleaseTransStateMetrics endOfLife;

		public SoftwareReleaseTransProgressMetrics() {
			pending = new SoftwareReleaseTransStateMetrics();
			inprogress = new SoftwareReleaseTransStateMetrics();
			received = new SoftwareReleaseTransStateMetrics();
			complete = new SoftwareReleaseTransStateMetrics();
			cancelled = new SoftwareReleaseTransStateMetrics();
			expired = new SoftwareReleaseTransStateMetrics();
			failed = new SoftwareReleaseTransStateMetrics();
			endOfLife = new SoftwareReleaseTransStateMetrics();
		}

		public SoftwareReleaseTransStateMetrics getPending() {
			return pending;
		}

		public void setPending(SoftwareReleaseTransStateMetrics pending) {
			this.pending = pending;
		}

		public SoftwareReleaseTransStateMetrics getInprogress() {
			return inprogress;
		}

		public void setInprogress(SoftwareReleaseTransStateMetrics inprogress) {
			this.inprogress = inprogress;
		}

		public SoftwareReleaseTransStateMetrics getReceived() {
			return received;
		}

		public void setReceived(SoftwareReleaseTransStateMetrics received) {
			this.received = received;
		}

		public SoftwareReleaseTransStateMetrics getComplete() {
			return complete;
		}

		public void setComplete(SoftwareReleaseTransStateMetrics complete) {
			this.complete = complete;
		}

		public SoftwareReleaseTransStateMetrics getCancelled() {
			return cancelled;
		}

		public void setCancelled(SoftwareReleaseTransStateMetrics cancelled) {
			this.cancelled = cancelled;
		}

		public SoftwareReleaseTransStateMetrics getFailed() {
			return failed;
		}

		public void setFailed(SoftwareReleaseTransStateMetrics failed) {
			this.failed = failed;
		}

		public SoftwareReleaseTransStateMetrics getExpired() {
			return expired;
		}

		public void setExpired(SoftwareReleaseTransStateMetrics expired) {
			this.expired = expired;
		}

		public SoftwareReleaseTransStateMetrics getEndOfLife() {
			return endOfLife;
		}

		public void setEndOfLife(SoftwareReleaseTransStateMetrics endOfLife) {
			this.endOfLife = endOfLife;
		}
	}

	public static class SoftwareReleaseTransStateMetrics implements Serializable {
		private static final long serialVersionUID = -7913920322626006020L;

		private int count = 0;
		private double percent = 0;

		public SoftwareReleaseTransStateMetrics() {
			count = 0;
			percent = 0;
		}

		public SoftwareReleaseTransStateMetrics(int count, double percent) {
			this.count = count;
			this.percent = percent;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public double getPercent() {
			return percent;
		}

		public void setPercent(double percent) {
			this.percent = percent;
		}
	}

	public static class SoftwareReleaseScheduleStatusModel extends CoreDocumentModel {
		private static final long serialVersionUID = -579610468934983267L;

		private int devices;
		private String name;
		private String requestor;
		private String deviceType;
		private String hwVersion;
		private String start;
		private String newSwVersion;
		private boolean onDemand;
		private Instant scheduledDate;
		private Instant started;
		private Instant completed;
		private String complete;
		// private CompletedStatus status;
		private SoftwareReleaseSchedule.Status status;
		private SoftwareReleaseTransProgressMetrics progressMetrics;

		public SoftwareReleaseScheduleStatusModel() {
			super(null, null);

			progressMetrics = new SoftwareReleaseTransProgressMetrics();
		}

		public SoftwareReleaseScheduleStatusModel(SoftwareReleaseSchedule softwareReleaseSchedule) {
			super(softwareReleaseSchedule.getId(), softwareReleaseSchedule.getHid());
			this.devices = softwareReleaseSchedule.getObjectIds().size();
			this.name = softwareReleaseSchedule.getName();

			progressMetrics = new SoftwareReleaseTransProgressMetrics();
		}

		public SoftwareReleaseScheduleStatusModel withRequestor(String requestor) {
			setRequestor(requestor);
			return this;
		}

		public SoftwareReleaseScheduleStatusModel withDeviceType(String deviceType) {
			setDeviceType(deviceType);
			return this;
		}

		public SoftwareReleaseScheduleStatusModel withNewSwVersion(String newSwVersion) {
			setNewSwVersion(newSwVersion);
			return this;
		}

		public SoftwareReleaseScheduleStatusModel withHwVersion(String hwVersion) {
			setHwVersion(hwVersion);
			return this;
		}

		public SoftwareReleaseScheduleStatusModel withScheduledDate(Instant scheduledDate) {
			setScheduledDate(scheduledDate);
			return this;
		}

		public SoftwareReleaseScheduleStatusModel withOnDemand(boolean onDemand) {
			setOnDemand(onDemand);
			return this;
		}

		public SoftwareReleaseScheduleStatusModel withStarted(Instant started) {
			setStarted(started);
			return this;
		}

		public SoftwareReleaseScheduleStatusModel withComplete(String complete) {
			setComplete(complete);
			return this;
		}

		public SoftwareReleaseScheduleStatusModel withCompleted(Instant ended) {
			setCompleted(ended);
			return this;
		}

//		public SoftwareReleaseScheduleStatusModel withStatus(CompletedStatus status) {
//			setStatus(status);
//			return this;
//		}

		public SoftwareReleaseScheduleStatusModel withStatus(SoftwareReleaseSchedule.Status status) {
			setStatus(status);
			return this;
		}

		public int getDevices() {
			return devices;
		}

		public void setDevices(int devices) {
			this.devices = devices;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getRequestor() {
			return requestor;
		}

		public void setRequestor(String requestor) {
			this.requestor = requestor;
		}

		public String getDeviceType() {
			return deviceType;
		}

		public void setDeviceType(String deviceType) {
			this.deviceType = deviceType;
		}

		public String getHwVersion() {
			return hwVersion;
		}

		public void setHwVersion(String hwVersion) {
			this.hwVersion = hwVersion;
		}

		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getNewSwVersion() {
			return newSwVersion;
		}

		public void setNewSwVersion(String newSwVersion) {
			this.newSwVersion = newSwVersion;
		}

		public Instant getScheduledDate() {
			return scheduledDate;
		}

		public void setScheduledDate(Instant scheduledDate) {
			this.scheduledDate = scheduledDate;
		}

		public boolean isOnDemand() {
			return onDemand;
		}

		public void setOnDemand(boolean onDemand) {
			this.onDemand = onDemand;
		}

		public Instant getStarted() {
			return started;
		}

		public void setStarted(Instant started) {
			this.started = started;
		}

		public String getComplete() {
			return complete;
		}

		public void setComplete(String complete) {
			this.complete = complete;
		}

		public Instant getCompleted() {
			return completed;
		}

		public void setCompleted(Instant ended) {
			this.completed = ended;
		}

//		public CompletedStatus getStatus() {
//			return status;
//		}

//		public void setStatus(CompletedStatus status) {
//			this.status = status;
//		}

		public SoftwareReleaseSchedule.Status getStatus() {
			return status;
		}

		public void setStatus(SoftwareReleaseSchedule.Status status) {
			this.status = status;
		}

		public SoftwareReleaseTransProgressMetrics getProgressMetrics() {
			return progressMetrics;
		}

		public void setProgressMetrics(SoftwareReleaseTransProgressMetrics progressMetrics) {
			this.progressMetrics = progressMetrics;
		}
	}

	public enum CompletedStatus {
		Complete, Cancelled
	}

	public static class SoftwareReleaseTransComparator implements Comparator<SoftwareReleaseTrans> {
		public int compare(SoftwareReleaseTrans a, SoftwareReleaseTrans b) {
			if (a.getStarted() != null && b.getStarted() != null)
				return -a.getStarted().compareTo(b.getStarted());
			else if (a.getEnded() != null && b.getEnded() != null)
				return -a.getEnded().compareTo(b.getEnded());
			else if (a.getStarted() != null && b.getEnded() != null)
				return -a.getStarted().compareTo(b.getEnded());
			if (a.getStarted() != null && b.getEnded() != null)
				return -a.getStarted().compareTo(b.getEnded());
			return 0;
		}
	}

	public static class SoftwareReleaseScheduleAssetModel extends CoreDefinitionModelOption {

		private static final long serialVersionUID = 1829010509142504043L;

		private String uid;
		private String owner;
		private String group;
		private String firmwareFrom;
		private String firmwareTo;
		private String status;
		private String error;
		private Instant started;
		private Instant ended;
		private Integer retryCount = 0;
		private long remainingMinutes = 0;

		public SoftwareReleaseScheduleAssetModel(String id, String hid, String name) {
			super(id, hid, name);
		}

		public SoftwareReleaseScheduleAssetModel(BaseDeviceAbstract asset) {
			super(asset.getId(), asset.getHid(), asset.getName());
			this.uid = asset.getUid();
		}

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getOwner() {
			return owner;
		}

		public void setOwner(String owner) {
			this.owner = owner;
		}

		public String getGroup() {
			return group;
		}

		public void setGroup(String group) {
			this.group = group;
		}

		public String getFirmwareFrom() {
			return firmwareFrom;
		}

		public void setFirmwareFrom(String firmwareFrom) {
			this.firmwareFrom = firmwareFrom;
		}

		public String getFirmwareTo() {
			return firmwareTo;
		}

		public void setFirmwareTo(String firmwareTo) {
			this.firmwareTo = firmwareTo;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getError() {
			return error;
		}

		public void setError(String alerts) {
			this.error = alerts;
		}

		public Instant getStarted() {
			return started;
		}

		public void setStarted(Instant started) {
			this.started = started;
		}

		public Instant getEnded() {
			return ended;
		}

		public void setEnded(Instant ended) {
			this.ended = ended;
		}

		public Integer getRetryCount() {
			return retryCount;
		}

		public void setRetryCount(Integer retryCount) {
			this.retryCount = retryCount;
		}

		public long getRemainingMinutes() {
			return remainingMinutes;
		}

		public void setRemainingMinutes(long remainingMinutes) {
			this.remainingMinutes = remainingMinutes;
		}
	}

	public static class SoftwareReleaseScheduleAuditModel extends SoftwareReleaseScheduleStatusModel {
		private static final long serialVersionUID = 5605510052807116456L;

		private Status scheduleStatus;
		private String comments;
		private boolean onDemand;

		public SoftwareReleaseScheduleAuditModel(SoftwareReleaseSchedule softwareReleaseSchedule) {
			super(softwareReleaseSchedule);
			this.scheduleStatus = softwareReleaseSchedule.getStatus();
			this.comments = softwareReleaseSchedule.getComments();
			this.onDemand = softwareReleaseSchedule.getOnDemand();
		}

		public Status getScheduleStatus() {
			return scheduleStatus;
		}

		public String getComments() {
			return comments;
		}

		public boolean isOnDemand() {
			return onDemand;
		}
	}

	public static class AssetOption extends CoreDefinitionModelOption {
		private static final long serialVersionUID = 6286389158839274713L;

		public AssetOption() {
			super(null, null, null);
		}

		public AssetOption(BaseDeviceAbstract gateway) {
			super(gateway.getId(), gateway.getHid(), gateway.getName());
		}
	}

	public static class AuditLogUserOption implements Serializable {

		private static final long serialVersionUID = 2921724119335050934L;

		private Set<String> ids = new HashSet<>();
		private String name;

		public AuditLogUserOption(String name, Set<String> ids) {
			this.name = name;
			this.ids.addAll(ids);
		}

		public Set<String> getIds() {
			return ids;
		}

		public String getName() {
			return name;
		}
	}
}
