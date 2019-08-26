package com.arrow.kronos.data;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.rhea.data.SoftwareRelease;
import com.fasterxml.jackson.annotation.JsonIgnore;

import moonstone.acn.client.model.AcnDeviceCategory;

@Document(collection = "software_release_trans")
public class SoftwareReleaseTrans extends AuditableDocumentAbstract {
	private static final long serialVersionUID = -5321843699578336941L;

	private static final Integer DEFAULT_RETRY_COUNT = 0;

	public enum Status {
		PENDING, INPROGRESS, EXPIRED, RECEIVED, COMPLETE, ERROR, CANCELLED
	}

	@NotBlank
	private String applicationId;
	@NotBlank
	private String objectId;
	@NotNull
	private AcnDeviceCategory deviceCategory;
	// @NotBlank
	private String uid;
	@NotBlank
	private String softwareReleaseScheduleId;
	@NotBlank
	private String fromSoftwareReleaseId;
	@NotBlank
	private String toSoftwareReleaseId;
	@NotNull
	private Status status = Status.PENDING;
	private String error;
	private String relatedSoftwareReleaseTransId;
	private Instant started;
	private Instant ended;
	@NotNull
	private Integer retryCount = DEFAULT_RETRY_COUNT;
	@NotNull
	/**
	 * the number of seconds until the transaction will expire
	 */
	private Long timeToExpireSeconds;

	@Transient
	@JsonIgnore
	private Application refApplication;
	@Transient
	@JsonIgnore
	private SoftwareReleaseSchedule refSoftwareReleaseSchedule;
	@Transient
	@JsonIgnore
	private SoftwareRelease refFromSoftwareRelease;
	@Transient
	@JsonIgnore
	private SoftwareRelease refToSoftwareRelease;
	@Transient
	@JsonIgnore
	private SoftwareReleaseTrans refRelatedSoftwareReleaseTrans;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public AcnDeviceCategory getDeviceCategory() {
		return deviceCategory;
	}

	public void setDeviceCategory(AcnDeviceCategory deviceCategory) {
		this.deviceCategory = deviceCategory;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSoftwareReleaseScheduleId() {
		return softwareReleaseScheduleId;
	}

	public void setSoftwareReleaseScheduleId(String softwareReleaseScheduleId) {
		this.softwareReleaseScheduleId = softwareReleaseScheduleId;
	}

	public String getFromSoftwareReleaseId() {
		return fromSoftwareReleaseId;
	}

	public void setFromSoftwareReleaseId(String fromSoftwareReleaseId) {
		this.fromSoftwareReleaseId = fromSoftwareReleaseId;
	}

	public String getToSoftwareReleaseId() {
		return toSoftwareReleaseId;
	}

	public void setToSoftwareReleaseId(String toSoftwareReleaseId) {
		this.toSoftwareReleaseId = toSoftwareReleaseId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getRelatedSoftwareReleaseTransId() {
		return relatedSoftwareReleaseTransId;
	}

	public void setRelatedSoftwareReleaseTransId(String relatedSoftwareReleaseTransId) {
		this.relatedSoftwareReleaseTransId = relatedSoftwareReleaseTransId;
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

	public Long getTimeToExpireSeconds() {
		return timeToExpireSeconds;
	}

	public void setTimeToExpireSeconds(Long timeToExpireSeconds) {
		this.timeToExpireSeconds = timeToExpireSeconds;
	}

	public Application getRefApplication() {
		return refApplication;
	}

	public void setRefApplication(Application refApplication) {
		this.refApplication = refApplication;
	}

	public SoftwareReleaseSchedule getRefSoftwareReleaseSchedule() {
		return refSoftwareReleaseSchedule;
	}

	public void setRefSoftwareReleaseSchedule(SoftwareReleaseSchedule refSoftwareReleaseSchedule) {
		this.refSoftwareReleaseSchedule = refSoftwareReleaseSchedule;
	}

	public SoftwareRelease getRefFromSoftwareRelease() {
		return refFromSoftwareRelease;
	}

	public void setRefFromSoftwareRelease(SoftwareRelease refFromSoftwareRelease) {
		this.refFromSoftwareRelease = refFromSoftwareRelease;
	}

	public SoftwareRelease getRefToSoftwareRelease() {
		return refToSoftwareRelease;
	}

	public void setRefToSoftwareRelease(SoftwareRelease refToSoftwareRelease) {
		this.refToSoftwareRelease = refToSoftwareRelease;
	}

	public SoftwareReleaseTrans getRefRelatedSoftwareReleaseTrans() {
		return refRelatedSoftwareReleaseTrans;
	}

	public void setRefRelatedSoftwareReleaseTrans(SoftwareReleaseTrans refRelatedSoftwareReleaseTrans) {
		this.refRelatedSoftwareReleaseTrans = refRelatedSoftwareReleaseTrans;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.SOFTWARE_RELEASE_TRANS;
	}
}