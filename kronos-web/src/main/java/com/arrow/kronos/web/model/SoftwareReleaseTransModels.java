package com.arrow.kronos.web.model;

import java.io.Serializable;
import java.time.Instant;

import com.arrow.kronos.data.SoftwareReleaseTrans;
import com.arrow.pegasus.webapi.data.CoreDocumentModel;

public class SoftwareReleaseTransModels {
	public static class RetrySoftwareReleaseTransactionsRequestModel implements Serializable {
		private static final long serialVersionUID = 5890688085257071211L;

		public String softwareReleaseScheduleId;
		public String[] softwareReleaseTransIds;

		public String getSoftwareReleaseScheduleId() {
			return softwareReleaseScheduleId;
		}

		public void setSoftwareReleaseScheduleId(String softwareReleaseScheduleId) {
			this.softwareReleaseScheduleId = softwareReleaseScheduleId;
		}

		public String[] getSoftwareReleaseTransIds() {
			return softwareReleaseTransIds;
		}

		public void setSoftwareReleaseTransIds(String[] softwareReleaseTransIds) {
			this.softwareReleaseTransIds = softwareReleaseTransIds;
		}
	}

	public static class CancelSoftwareReleaseTransactionsRequestModel implements Serializable {
		private static final long serialVersionUID = 1890179441927066777L;

		public String softwareReleaseScheduleId;
		public String[] softwareReleaseTransIds;

		public String getSoftwareReleaseScheduleId() {
			return softwareReleaseScheduleId;
		}

		public void setSoftwareReleaseScheduleId(String softwareReleaseScheduleId) {
			this.softwareReleaseScheduleId = softwareReleaseScheduleId;
		}

		public String[] getSoftwareReleaseTransIds() {
			return softwareReleaseTransIds;
		}

		public void setSoftwareReleaseTransIds(String[] softwareReleaseTransIds) {
			this.softwareReleaseTransIds = softwareReleaseTransIds;
		}
	}

	public static class SoftwareReleaseTransModel extends CoreDocumentModel {
		private static final long serialVersionUID = 8127272411699784249L;

		private String fromVersion;
		private String toVersion;
		private Instant started;
		private Instant ended;
		private SoftwareReleaseTrans.Status status;
		private String softwareReleaseScheduleId;
		private String error;
		private String jobName;

		public SoftwareReleaseTransModel(SoftwareReleaseTrans softwareReleaseTrans) {
			super(softwareReleaseTrans.getId(), softwareReleaseTrans.getHid());

			this.status = softwareReleaseTrans.getStatus();
			this.softwareReleaseScheduleId = softwareReleaseTrans.getSoftwareReleaseScheduleId();
			this.started = softwareReleaseTrans.getStarted();
			this.ended = softwareReleaseTrans.getEnded();
			this.error = softwareReleaseTrans.getError();
		}

		public String getSoftwareReleaseScheduleId() {
			return softwareReleaseScheduleId;
		}

		public void setSoftwareReleaseScheduleId(String softwareReleaseScheduleId) {
			this.softwareReleaseScheduleId = softwareReleaseScheduleId;
		}

		public String getFromVersion() {
			return fromVersion;
		}

		public void setFromVersion(String fromVersion) {
			this.fromVersion = fromVersion;
		}

		public String getToVersion() {
			return toVersion;
		}

		public void setToVersion(String toVersion) {
			this.toVersion = toVersion;
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

		public SoftwareReleaseTrans.Status getStatus() {
			return status;
		}

		public void setStatus(SoftwareReleaseTrans.Status status) {
			this.status = status;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getJobName() {
			return jobName;
		}

		public void setJobName(String softwareReleaseScheduleName) {
			this.jobName = softwareReleaseScheduleName;
		}
	}
}
