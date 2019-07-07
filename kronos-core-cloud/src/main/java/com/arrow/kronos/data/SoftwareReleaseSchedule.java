package com.arrow.kronos.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.AuditableDocumentAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.rhea.data.SoftwareRelease;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = SoftwareReleaseSchedule.COLLECTION_NAME)
public class SoftwareReleaseSchedule extends AuditableDocumentAbstract {
    public static final String COLLECTION_NAME = "software_release_schedule";
    private static final long serialVersionUID = -7160023697297690214L;

    public enum Status {
        SCHEDULED, INPROGRESS, COMPLETE, CANCELLED
    }

    public final static boolean DEFAULT_NOTIFY = true;
    public final static boolean DEFAULT_ON_DEMAND = false;
    public final static boolean DEFAULT_WITH_ERROR = false;

    @NotBlank
    private String applicationId;
    private Instant scheduledDate;
    @NotBlank
    private String softwareReleaseId;
    @NotNull
    private AcnDeviceCategory deviceCategory;
    private String comments;
    @NotEmpty
    private List<String> objectIds = new ArrayList<String>();
    @NotNull
    private Status status = Status.SCHEDULED;
    private boolean notifyOnSubmit = DEFAULT_NOTIFY;
    private boolean notifyOnStart = DEFAULT_NOTIFY;
    private boolean notifyOnEnd = DEFAULT_NOTIFY;
    private boolean completeWithError = DEFAULT_WITH_ERROR;
    private String notifyEmails;
    private Instant started;
    private Instant ended;
    @NotBlank
    private String name;
    private boolean onDemand = DEFAULT_ON_DEMAND;
    @NotBlank
    private String deviceTypeId;
    @NotBlank
    private String hardwareVersionId;
    @Deprecated
    /**
     * the time to live in milliseconds
     */
    private Long transactionExpiration;
    /**
     * the number of seconds until the transaction will expire
     */
    private Long timeToExpireSeconds;

    @Transient
    @JsonIgnore
    private Application refApplication;
    @Transient
    @JsonIgnore
    private SoftwareRelease refSoftwareRelease;
    @Transient
    @JsonIgnore
    private DeviceType refDeviceType;
    @Transient
    @JsonIgnore
    private com.arrow.rhea.data.DeviceType refHardwareVersion;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Instant getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Instant scheduledDate) {
        this.scheduledDate = scheduledDate;
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

    public String getNotifyEmails() {
        return notifyEmails;
    }

    public void setNotifyEmails(String notifyEmails) {
        this.notifyEmails = notifyEmails;
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

    public Application getRefApplication() {
        return refApplication;
    }

    public void setRefApplication(Application refApplication) {
        this.refApplication = refApplication;
    }

    public SoftwareRelease getRefSoftwareRelease() {
        return refSoftwareRelease;
    }

    public void setRefSoftwareRelease(SoftwareRelease refSoftwareRelease) {
        this.refSoftwareRelease = refSoftwareRelease;
    }

    public DeviceType getRefDeviceType() {
        return refDeviceType;
    }

    public void setRefDeviceType(DeviceType refDeviceType) {
        this.refDeviceType = refDeviceType;
    }

    public com.arrow.rhea.data.DeviceType getRefHardwareVersion() {
        return refHardwareVersion;
    }

    public void setRefHardwareVersion(com.arrow.rhea.data.DeviceType refHardwareVersion) {
        this.refHardwareVersion = refHardwareVersion;
    }

    @Override
    protected String getProductPri() {
        return KronosConstants.KRONOS_PRI;
    }

    @Override
    protected String getTypePri() {
        return KronosConstants.KronosPri.SOFTWARE_RELEASE_SCHEDULE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getOnDemand() {
        return onDemand;
    }

    public void setOnDemand(boolean onDemand) {
        this.onDemand = onDemand;
    }

    public boolean getNotifyOnSubmit() {
        return notifyOnSubmit;
    }

    public void setNotifyOnSubmit(boolean notifyOnSubmit) {
        this.notifyOnSubmit = notifyOnSubmit;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getHardwareVersionId() {
        return hardwareVersionId;
    }

    public void setHardwareVersionId(String hardwareVersionId) {
        this.hardwareVersionId = hardwareVersionId;
    }

    public boolean getCompleteWithError() {
        return completeWithError;
    }

    public void setCompleteWithError(boolean completeWithError) {
        this.completeWithError = completeWithError;
    }

    @Deprecated
    public Long getTransactionExpiration() {
        return transactionExpiration;
    }

    @Deprecated
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
