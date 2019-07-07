package com.arrow.kronos.service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acn.client.model.RightToUseType;
import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.BaseDeviceAbstract;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.EligibleFirmwareChangeGroup;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.data.SoftwareReleaseSchedule.Status;
import com.arrow.kronos.data.SoftwareReleaseTrans;
import com.arrow.kronos.repo.DeviceSearchParams;
import com.arrow.kronos.repo.DeviceTypeSearchParams;
import com.arrow.kronos.repo.GatewaySearchParams;
import com.arrow.kronos.repo.SoftwareReleaseScheduleRepository;
import com.arrow.kronos.repo.SoftwareReleaseScheduleSearchParams;
import com.arrow.kronos.repo.SoftwareReleaseTransSearchParams;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.arrow.rhea.client.api.ClientRTURequestApi;
import com.arrow.rhea.client.api.ClientSoftwareReleaseApi;
import com.arrow.rhea.data.RTURequest;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareRelease;

@Service
public class SoftwareReleaseScheduleService extends KronosServiceAbstract {

    private static final String EMAIL_DELIMITER = "\\s*,\\s*";

    @Autowired
    private SoftwareReleaseScheduleRepository softwareReleaseScheduleRepository;
    @Autowired
    private SoftwareReleaseTransService softwareReleaseTransService;
    @Autowired
    private SoftwareReleaseService softwareReleaseService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private DeviceTypeService deviceTypeService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private GatewayService gatewayService;
    // @Autowired
    // private ClientDeviceCategoryApi rheaClientDeviceCategoryApi;
    @Autowired
    private ClientSoftwareReleaseApi rheaClientSoftwareReleaseApi;

    @Autowired
    private KronosHttpRequestCache requestCache;
    @Autowired
    private ClientRTURequestApi clientRTURequestApi;

    public SoftwareReleaseScheduleRepository getSoftwareReleaseScheduleRepository() {
        return softwareReleaseScheduleRepository;
    }

    public SoftwareReleaseSchedule checkAndStart(SoftwareReleaseSchedule softwareReleaseSchedule, String who) {
        Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is null");
        Assert.hasLength(who, "who is empty");
        Assert.isTrue(softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.SCHEDULED,
                "softwareReleaseSchedule must be in a SCHEDULED state");
        Assert.notEmpty(softwareReleaseSchedule.getObjectIds(), "objectIds are empty");

        String method = "checkAndStart";
        logInfo(method, "...");

        // update softwareReleaseSchedule to INPROGRESS state
        softwareReleaseSchedule.setStatus(SoftwareReleaseSchedule.Status.INPROGRESS);
        softwareReleaseSchedule.setStarted(Instant.now());
        softwareReleaseSchedule = doUpdate(softwareReleaseSchedule, who);

        // create and start the softwareReleaseTrans for each objectId
        for (String objectId : softwareReleaseSchedule.getObjectIds()) {
            // TODO future enhancement, the time to expire should be persisted
            // on the transaction when it is created
            // create softwareReleaseTrans
            SoftwareReleaseTrans softwareReleaseTrans = softwareReleaseService.create(objectId,
                    softwareReleaseSchedule.getDeviceCategory(), softwareReleaseSchedule.getSoftwareReleaseId(),
                    softwareReleaseSchedule.getId(), who);

            if (softwareReleaseTrans != null
                    && softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.PENDING) {
                // start softwareReleaseTrans
                softwareReleaseService.start(softwareReleaseTrans.getId(), who);
            }
        }

        softwareReleaseSchedule = checkAndSendOnStartNotification(softwareReleaseSchedule, who);

        return softwareReleaseSchedule;
    }

    public SoftwareReleaseSchedule checkAndComplete(SoftwareReleaseSchedule softwareReleaseSchedule, String who) {
        Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is null");
        Assert.hasLength(who, "who is empty");
        Assert.isTrue(softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.INPROGRESS,
                "softwareReleaseSchedule must be in a INPROGRESS state");

        String method = "checkAndComplete";
        logInfo(method, "...");

        SoftwareReleaseTransSearchParams params = new SoftwareReleaseTransSearchParams();
        params.addSoftwareReleaseScheduleIds(softwareReleaseSchedule.getId());
        List<SoftwareReleaseTrans> softwareReleaseTransList = softwareReleaseTransService
                .getSoftwareReleaseTransRepository().findSoftwareReleaseTrans(params);

        int endStateCount = 0;
        for (SoftwareReleaseTrans softwareReleaseTrans : softwareReleaseTransList) {
            if (softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.COMPLETE
                    || softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.CANCELLED) {
                endStateCount++;
            } else if (softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.INPROGRESS) {

                Instant now = Instant.now();
                Instant startDate = softwareReleaseTrans.getStarted();
                Instant expireDate = startDate.plusSeconds(softwareReleaseTrans.getTimeToExpireSeconds());
                if (now.isAfter(expireDate))
                    softwareReleaseTransService.expire(softwareReleaseTrans, who);
            }
        }

        if (endStateCount == softwareReleaseTransList.size()) {
            softwareReleaseSchedule.setStatus(SoftwareReleaseSchedule.Status.COMPLETE);
            softwareReleaseSchedule.setEnded(Instant.now());
            // can no longer complete with error as of AC-201806-P2
            softwareReleaseSchedule.setCompleteWithError(false);
            softwareReleaseSchedule = doUpdate(softwareReleaseSchedule, who);
            softwareReleaseSchedule = checkAndSendOnEndNotification(softwareReleaseSchedule, who);
        }

        return softwareReleaseSchedule;
    }

    public SoftwareReleaseSchedule checkAndSendOnStartNotification(SoftwareReleaseSchedule softwareReleaseSchedule,
            String who) {
        Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is null");
        Assert.hasLength(who, "who is empty");
        Assert.isTrue(softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.INPROGRESS,
                "softwareReleaseSchedule must be in a INPROGRESS state");

        String method = "checkAndSendOnStartNotification";
        logInfo(method, "...");

        if (softwareReleaseSchedule.isNotifyOnStart()) {
            if (StringUtils.isNotBlank(softwareReleaseSchedule.getNotifyEmails())) {
                emailService.sendSoftwareReleaseScheduleStartEmail(
                        softwareReleaseSchedule.getNotifyEmails().split(EMAIL_DELIMITER),
                        new EmailService.ModelBuilder().withParameter("jobName", softwareReleaseSchedule.getName())
                                .withParameter("requestor", getRequestor(softwareReleaseSchedule))
                                .withParameter("assets", softwareReleaseSchedule.getObjectIds().size())
                                .withParameter("deviceType", getDeviceType(softwareReleaseSchedule))
                                .withParameter("hwVersion", getHardwareVersion(softwareReleaseSchedule))
                                .withParameter("newSwVersion", getNewSoftwareVersion(softwareReleaseSchedule))
                                .withParameter("started", softwareReleaseSchedule.getStarted()).build());
            } else {
                logInfo(method, "empty notifyEmails");
            }
        }
        return softwareReleaseSchedule;
    }

    public SoftwareReleaseSchedule checkAndSendOnEndNotification(SoftwareReleaseSchedule softwareReleaseSchedule,
            String who) {
        Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is null");
        Assert.hasLength(who, "who is empty");
        Assert.isTrue(softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.COMPLETE,
                "softwareReleaseSchedule must be in a COMPLETE state");

        String method = "checkAndSendOnEndNotification";
        logInfo(method, "...");

        if (softwareReleaseSchedule.isNotifyOnEnd()) {
            if (StringUtils.isNotBlank(softwareReleaseSchedule.getNotifyEmails())) {
                emailService.sendSoftwareReleaseScheduleEndEmail(
                        softwareReleaseSchedule.getNotifyEmails().split(EMAIL_DELIMITER),
                        new EmailService.ModelBuilder().withParameter("jobName", softwareReleaseSchedule.getName())
                                .withParameter("requestor", getRequestor(softwareReleaseSchedule))
                                .withParameter("assets", softwareReleaseSchedule.getObjectIds().size())
                                .withParameter("deviceType", getDeviceType(softwareReleaseSchedule))
                                .withParameter("hwVersion", getHardwareVersion(softwareReleaseSchedule))
                                .withParameter("newSwVersion", getNewSoftwareVersion(softwareReleaseSchedule))
                                .withParameter("started", softwareReleaseSchedule.getStarted())
                                .withParameter("completed", softwareReleaseSchedule.getEnded())
                                .withParameter("status", softwareReleaseSchedule.getStatus().toString()).build());
            } else {
                logInfo(method, "empty notifyEmails");
            }
        }
        return softwareReleaseSchedule;
    }

    public SoftwareReleaseSchedule create(SoftwareReleaseSchedule softwareReleaseSchedule, String who) {
        String method = "create";
        logInfo(method, "...");

        // logical checks
        if (softwareReleaseSchedule == null) {
            logInfo(method, "softwareReleaseSchedule is null");
            throw new AcsLogicalException("softwareReleaseSchedule is null");
        }

        // status
        softwareReleaseSchedule.setStatus(SoftwareReleaseSchedule.Status.SCHEDULED);

        // use default time to expire if not provided
        if (softwareReleaseSchedule.getTimeToExpireSeconds() == null
                || softwareReleaseSchedule.getTimeToExpireSeconds() <= 0) {
            softwareReleaseSchedule.setTimeToExpireSeconds(KronosConstants.FOTA.DEFAULT_TIME_TO_EXPIRE_SECONDS);
        }

        // temporary to be backwards compatable until the property is removed
        softwareReleaseSchedule.setTransactionExpiration(
                softwareReleaseSchedule.getTimeToExpireSeconds() * KronosConstants.FOTA.ONE_SECOND_IN_MILLISECONDS);

        validateSoftwareReleaseSchedule(softwareReleaseSchedule);

        if (StringUtils.isEmpty(who)) {
            logInfo(method, "who is empty");
            throw new AcsLogicalException("who is empty");
        }

        // insert
        softwareReleaseSchedule = softwareReleaseScheduleRepository.doInsert(softwareReleaseSchedule, who);

        // audit log
        getAuditLogService().save(AuditLogBuilder.create()
                .type(KronosAuditLog.SoftwareReleaseSchedule.CreateSoftwareReleaseSchedule)
                .applicationId(softwareReleaseSchedule.getApplicationId()).objectId(softwareReleaseSchedule.getId())
                .parameter("deviceCategory", softwareReleaseSchedule.getDeviceCategory().name())
                .parameter("objectIds", concateObjectIds(softwareReleaseSchedule))
                .parameter("status", softwareReleaseSchedule.getStatus().name()).by(who));

        softwareReleaseSchedule = checkAndSendOnSubmitNotification(softwareReleaseSchedule);

        return softwareReleaseSchedule;
    }

    public SoftwareReleaseSchedule checkAndSendOnSubmitNotification(SoftwareReleaseSchedule softwareReleaseSchedule) {
        Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is null");
        Assert.isTrue(softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.SCHEDULED,
                "softwareReleaseSchedule must be in SCHEDULED state");

        String method = "checkAndSendOnSubmitNotification";
        logInfo(method, "...");

        if (softwareReleaseSchedule.getNotifyOnSubmit()) {
            if (StringUtils.isNotBlank(softwareReleaseSchedule.getNotifyEmails())) {
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                        .withZone(ZoneOffset.UTC);

                emailService.sendSoftwareReleaseScheduleSubmitEmail(
                        softwareReleaseSchedule.getNotifyEmails().split(EMAIL_DELIMITER),
                        new EmailService.ModelBuilder().withParameter("jobName", softwareReleaseSchedule.getName())
                                .withParameter("requestor", getRequestor(softwareReleaseSchedule))
                                .withParameter("assets", softwareReleaseSchedule.getObjectIds().size())
                                .withParameter("deviceType", getDeviceType(softwareReleaseSchedule))
                                .withParameter("hwVersion", getHardwareVersion(softwareReleaseSchedule))
                                .withParameter("newSwVersion", getNewSoftwareVersion(softwareReleaseSchedule))
                                .withParameter("schedule",
                                        softwareReleaseSchedule.getOnDemand() ? "On Demand"
                                                : formatter.format(softwareReleaseSchedule.getScheduledDate()))
                                .withParameter("onDemand", softwareReleaseSchedule.getOnDemand())
                                .withParameter("scheduledDate", softwareReleaseSchedule.getScheduledDate()).build());
            } else {
                logInfo(method, "empty notifyEmails");
            }
        }
        return softwareReleaseSchedule;
    }

    public SoftwareReleaseSchedule update(SoftwareReleaseSchedule softwareReleaseSchedule, String who) {
        String method = "update";
        logInfo(method, "...");

        // logical checks
        if (softwareReleaseSchedule == null) {
            logInfo(method, "softwareReleaseSchedule is null");
            throw new AcsLogicalException("softwareReleaseSchedule is null");
        }
        SoftwareReleaseSchedule schedule = softwareReleaseScheduleRepository.findById(softwareReleaseSchedule.getId())
                .orElse(null);
        if (schedule == null) {
            throw new AcsLogicalException("softwareReleaseSchedule is not found");
        }
        if (schedule.getStatus() != SoftwareReleaseSchedule.Status.SCHEDULED) {
            throw new AcsLogicalException("softwareReleaseSchedule is not in SCHEDULED status");
        }
        if (softwareReleaseSchedule.getTimeToExpireSeconds() == null
                || softwareReleaseSchedule.getTimeToExpireSeconds() <= 0) {
            softwareReleaseSchedule.setTimeToExpireSeconds(KronosConstants.FOTA.DEFAULT_TIME_TO_EXPIRE_SECONDS);
        }
        softwareReleaseSchedule.setTransactionExpiration(
                softwareReleaseSchedule.getTimeToExpireSeconds() * KronosConstants.FOTA.ONE_SECOND_IN_MILLISECONDS);

        validateSoftwareReleaseSchedule(softwareReleaseSchedule);

        if (StringUtils.isEmpty(who)) {
            logInfo(method, "who is empty");
            throw new AcsLogicalException("who is empty");
        }

        return doUpdate(schedule, softwareReleaseSchedule, who);
    }

    public SoftwareReleaseSchedule populateRefs(SoftwareReleaseSchedule softwareReleaseSchedule) {
        if (softwareReleaseSchedule == null) {
            return softwareReleaseSchedule;
        }
        // refApplication
        if (softwareReleaseSchedule.getRefApplication() == null
                && StringUtils.isNotEmpty(softwareReleaseSchedule.getApplicationId())) {
            softwareReleaseSchedule.setRefApplication(
                    requestCache.findApplicationById(softwareReleaseSchedule.getApplicationId(), false));
        }
        // refDeviceCategory
        // if (softwareReleaseSchedule.getRefDeviceCategory() == null
        // && softwareReleaseSchedule.getDeviceCategory() != null) {
        // softwareReleaseSchedule.setRefDeviceCategory(
        // rheaClientDeviceCategoryApi.findByName(softwareReleaseSchedule.getDeviceCategory().name()));
        // }
        // refDeviceType
        if (softwareReleaseSchedule.getRefDeviceType() == null
                && StringUtils.isNotEmpty(softwareReleaseSchedule.getDeviceTypeId())) {
            softwareReleaseSchedule
                    .setRefDeviceType(requestCache.findDeviceTypeById(softwareReleaseSchedule.getDeviceTypeId()));
        }
        // refHardwareVersion
        if (softwareReleaseSchedule.getRefHardwareVersion() == null
                && StringUtils.isNotEmpty(softwareReleaseSchedule.getHardwareVersionId())) {
            softwareReleaseSchedule.setRefHardwareVersion(
                    requestCache.findRheaDeviceTypeById(softwareReleaseSchedule.getHardwareVersionId()));
        }
        // refSoftwareRelease
        if (softwareReleaseSchedule.getRefSoftwareRelease() == null
                && StringUtils.isNotEmpty(softwareReleaseSchedule.getSoftwareReleaseId())) {
            softwareReleaseSchedule.setRefSoftwareRelease(
                    requestCache.findSoftwareReleaseById(softwareReleaseSchedule.getSoftwareReleaseId()));
        }
        return softwareReleaseSchedule;
    }

    public SoftwareReleaseSchedule findSoftwareReleaseScheduleById(String softwareReleaseScheduleId) {
        SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleRepository
                .findById(softwareReleaseScheduleId).orElse(null);
        Assert.notNull(softwareReleaseSchedule,
                "Unable to find softwareReleaseSchedule! id=" + softwareReleaseScheduleId);

        return softwareReleaseSchedule;
    }

    public Long deleteByApplicationId(String applicationId, String who) {
        String method = "deleteByApplicationId";
        Assert.hasText(applicationId, "applicationId is empty");
        Assert.hasText(who, "who is empty");
        logInfo(method, "applicationId: %s, who: %s", applicationId, who);
        return softwareReleaseScheduleRepository.deleteByApplicationId(applicationId);
    }

    public SoftwareReleaseSchedule cancel(SoftwareReleaseSchedule softwareReleaseSchedule, String who) {
        Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is null");
        Assert.hasText(who, "who is empty");
        Assert.isTrue(softwareReleaseSchedule.getStatus() == Status.SCHEDULED,
                "softwareReleaseSchedule must be in either SCHEDULED state");

        String method = "cancel";
        logDebug(method, "...");

        // update software release transactions
        // SoftwareReleaseTransSearchParams params = new
        // SoftwareReleaseTransSearchParams();
        // params.addSoftwareReleaseScheduleIds(softwareReleaseSchedule.getId());
        // List<SoftwareReleaseTrans> softwareReleaseTransactions =
        // softwareReleaseTransService
        // .getSoftwareReleaseTransRepository().findSoftwareReleaseTrans(params);
        //
        // for (SoftwareReleaseTrans softwareReleaseTrans :
        // softwareReleaseTransactions) {
        // switch (softwareReleaseTrans.getStatus()) {
        // case PENDING:
        // case INPROGRESS:
        // case ERROR:
        // case EXPIRED:
        // // update software release trans (asset)
        // softwareReleaseTransService.cancel(softwareReleaseTrans, false, who);
        // break;
        // case COMPLETE:
        // case RECEIVED:
        // // do nothing, these status are considered to be EOL (end of
        // // life)
        // break;
        // default:
        // throw new AcsLogicalException("Unsupported state! state=" +
        // softwareReleaseTrans.getStatus());
        // }
        // }

        // update software release schedule (job)
        softwareReleaseSchedule.setStatus(Status.CANCELLED);
        softwareReleaseSchedule.setEnded(Instant.now());
        softwareReleaseSchedule = doUpdate(softwareReleaseSchedule, who);

        return softwareReleaseSchedule;
    }

    private String concateObjectIds(SoftwareReleaseSchedule softwareReleaseSchedule) {
        if (softwareReleaseSchedule == null)
            return null;

        StringBuffer objectIds = new StringBuffer();
        for (int i = 0; i < softwareReleaseSchedule.getObjectIds().size(); i++) {
            objectIds.append(softwareReleaseSchedule.getObjectIds().get(i));
            if (i < softwareReleaseSchedule.getObjectIds().size() - 1) {
                objectIds.append(", ");
            }
        }

        return objectIds.toString();
    }

    private void validateSoftwareReleaseSchedule(SoftwareReleaseSchedule softwareReleaseSchedule) {
        Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is null");

        String method = "validateSoftwareReleaseSchedule";

        if (StringUtils.isEmpty(softwareReleaseSchedule.getApplicationId())) {
            logInfo(method, "applicationId is empty");
            throw new AcsLogicalException("applicationId is empty");
        }

        if (!softwareReleaseSchedule.getOnDemand() && softwareReleaseSchedule.getScheduledDate() == null) {
            logInfo(method, "scheduledDate is null");
            throw new AcsLogicalException("scheduledDate is null");
        }

        if (StringUtils.isEmpty(softwareReleaseSchedule.getSoftwareReleaseId())) {
            logInfo(method, "softwareReleaseId is empty");
            throw new AcsLogicalException("softwareReleaseId is empty");
        }

        if (softwareReleaseSchedule.getDeviceCategory() == null) {
            logInfo(method, "deviceCategory is null");
            throw new AcsLogicalException("deviceCategory is null");
        }

        if (softwareReleaseSchedule.getObjectIds() == null || softwareReleaseSchedule.getObjectIds().isEmpty()) {
            logInfo(method, "objectIds are null or empty");
            throw new AcsLogicalException("objectIds are null or empty");
        }
        for (String objectId : softwareReleaseSchedule.getObjectIds()) {
            SoftwareReleaseSchedule schedule = softwareReleaseService.findActiveSoftwareReleaseSchedule(
                    softwareReleaseSchedule.getApplicationId(), softwareReleaseSchedule.getId(), objectId,
                    softwareReleaseSchedule.getDeviceCategory());
            if (schedule != null) {
                String assetName = getObjectName(objectId, softwareReleaseSchedule.getDeviceCategory());
                logInfo(method, "Asset " + assetName + " is already being managed by job: " + schedule.getName());
                throw new AcsLogicalException(
                        "Asset " + assetName + " is already being managed by job: " + schedule.getName());
            }
        }

        if (softwareReleaseSchedule.getStatus() == null) {
            logInfo(method, "status is null");
            throw new AcsLogicalException("status is null");
        }
        if (softwareReleaseSchedule.getStatus() != SoftwareReleaseSchedule.Status.SCHEDULED
                && softwareReleaseSchedule.getStatus() != SoftwareReleaseSchedule.Status.CANCELLED) {
            throw new AcsLogicalException("invalid status");
        }

        if (StringUtils.isBlank(softwareReleaseSchedule.getName())) {
            logInfo(method, "name is empty");
            throw new AcsLogicalException("name is empty");
        }

        if (StringUtils.isBlank(softwareReleaseSchedule.getDeviceTypeId())) {
            logInfo(method, "deviceTypeId is empty");
            throw new AcsLogicalException("deviceTypeId is empty");
        }

        if (StringUtils.isBlank(softwareReleaseSchedule.getHardwareVersionId())) {
            logInfo(method, "hardwareVersionId is empty");
            throw new AcsLogicalException("hardwareVersionId is empty");
        }
    }

    private String getObjectName(String objectId, AcnDeviceCategory deviceCategory) {
        String objectName = "";
        switch (deviceCategory) {
        case GATEWAY:
            Gateway gateway = requestCache.findGatewayById(objectId);
            Assert.notNull(gateway, "Unable to find gateway! gatewayId=" + objectId);
            objectName = gateway.getName();
            break;
        case DEVICE:
            Device device = requestCache.findDeviceById(objectId);
            Assert.notNull(device, "Unable to find device! deviceId=" + objectId);
            objectName = device.getName();
            break;
        }
        return objectName;
    }

    private String getRequestor(SoftwareReleaseSchedule softwareReleaseSchedule) {
        User user = requestCache.findUserById(softwareReleaseSchedule.getCreatedBy(), false);
        return user != null && user.getContact() != null ? user.getContact().fullName() : "";
    }

    private String getDeviceType(SoftwareReleaseSchedule softwareReleaseSchedule) {
        DeviceType deviceType = requestCache.findDeviceTypeById(softwareReleaseSchedule.getDeviceTypeId());
        return deviceType != null ? deviceType.getName() : "";
    }

    private String getHardwareVersion(SoftwareReleaseSchedule softwareReleaseSchedule) {
        com.arrow.rhea.data.DeviceType deviceType = requestCache
                .findRheaDeviceTypeById(softwareReleaseSchedule.getHardwareVersionId());
        return deviceType != null ? deviceType.getName() : "";
    }

    private String getNewSoftwareVersion(SoftwareReleaseSchedule softwareReleaseSchedule) {
        SoftwareRelease softwareRelease = requestCache
                .findSoftwareReleaseById(softwareReleaseSchedule.getSoftwareReleaseId());
        SoftwareProduct softwareProduct = requestCache.findSoftwareProductById(softwareRelease.getSoftwareProductId());
        String softwareVersion = softwareRelease != null ? softwareReleaseService.buildSoftwareVersion(softwareRelease)
                : "";
        return (softwareProduct != null ? softwareProduct.getName() + " " : "") + softwareVersion;
    }

    private SoftwareReleaseSchedule doUpdate(SoftwareReleaseSchedule softwareReleaseSchedule, String who) {
        Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is null");
        Assert.notNull(softwareReleaseSchedule.getId(), "softwareReleaseScheduleId is null");
        SoftwareReleaseSchedule oldSoftwarereleaseSchedule = softwareReleaseScheduleRepository
                .findById(softwareReleaseSchedule.getId()).orElse(null);
        return doUpdate(oldSoftwarereleaseSchedule, softwareReleaseSchedule, who);
    }

    private SoftwareReleaseSchedule doUpdate(SoftwareReleaseSchedule oldSoftwareReleaseSchedule,
            SoftwareReleaseSchedule schedule, String who) {
        String method = "doUpdate";
        logInfo(method, "...");

        // update
        SoftwareReleaseSchedule newSoftwareReleaseSchedule = softwareReleaseScheduleRepository.doSave(schedule, who);

        // audit log
        AuditLogBuilder logBuilder = AuditLogBuilder.create()
                .type(KronosAuditLog.SoftwareReleaseSchedule.UpdateSoftwareReleaseSchedule)
                .applicationId(newSoftwareReleaseSchedule.getApplicationId())
                .objectId(newSoftwareReleaseSchedule.getId()).by(who)
                .parameter("deviceCategory", newSoftwareReleaseSchedule.getDeviceCategory().name());
        if (oldSoftwareReleaseSchedule != null) {
            if (oldSoftwareReleaseSchedule.getStatus() != newSoftwareReleaseSchedule.getStatus()) {
                addLogParameter(logBuilder, "Status", () -> oldSoftwareReleaseSchedule.getStatus().name(),
                        () -> newSoftwareReleaseSchedule.getStatus().name());
            }
            if (oldSoftwareReleaseSchedule.getOnDemand() != newSoftwareReleaseSchedule.getOnDemand()
                    || !Objects.equals(oldSoftwareReleaseSchedule.getScheduledDate(),
                            newSoftwareReleaseSchedule.getScheduledDate())) {
                addLogParameter(logBuilder, "OnDemand", oldSoftwareReleaseSchedule::getOnDemand,
                        newSoftwareReleaseSchedule::getOnDemand);
                addLogParameter(logBuilder, "ScheduledDate", () -> oldSoftwareReleaseSchedule.getScheduledDate(),
                        () -> newSoftwareReleaseSchedule.getScheduledDate());
            }
            addLogParameter(logBuilder, "ObjectIds", () -> concateObjectIds(oldSoftwareReleaseSchedule),
                    () -> concateObjectIds(newSoftwareReleaseSchedule));
        }

        getAuditLogService().save(logBuilder);

        return newSoftwareReleaseSchedule;
    }

    private AuditLogBuilder addLogParameter(AuditLogBuilder logBuilder, String paramName,
            Supplier<Object> oldValueSupplier, Supplier<Object> newValueSupplier) {
        Object oldVal = oldValueSupplier.get();
        Object newVal = newValueSupplier.get();
        logBuilder.parameter("old" + paramName, oldVal != null ? oldVal.toString() : null).parameter("new" + paramName,
                newVal != null ? newVal.toString() : null);
        return logBuilder;
    }

    private Collection<EligibleFirmwareChangeGroup> getInnerEligibleModels(String applicationId,
            List<DeviceType> deviceTypes) {
        Assert.hasText(applicationId, "applicationId is empty");

        String method = "getInnerEligibleModels";

        Application application = requestCache.findApplicationById(applicationId, false);
        Assert.notNull(application, "application not found! applicationId: " + applicationId);

        // STEP 1
        // lookup active assets types that have a rheaDeviceTypeId
        List<DeviceType> assetTypes = new ArrayList<>();
        if (deviceTypes != null) {
            assetTypes = deviceTypes;
        } else {
            DeviceTypeSearchParams assetTypeSearchParams = new DeviceTypeSearchParams();
            assetTypeSearchParams.addApplicationIds(applicationId);
            assetTypeSearchParams.setEnabled(true);
            assetTypeSearchParams.setRheaDeviceTypeDefined(true);
            assetTypes = deviceTypeService.getDeviceTypeRepository().findDeviceTypes(assetTypeSearchParams);
        }
        logDebug(method, "assetTypes size: %s", (assetTypes == null ? 0 : assetTypes.size()));

        Map<String, EligibleFirmwareChangeGroup> eligibleGroupResult = new HashMap<>();
        if (assetTypes != null && !assetTypes.isEmpty()) {
            // derive if the firmware management feature has been purchased
            boolean hasFirmwareManagementFeature = false;
            if (application.getProductFeatures() != null && !application.getProductFeatures().isEmpty()
                    && application.getProductFeatures().contains(KronosConstants.ProductFeatures.FIRMWARE_MANAGEMENT))
                hasFirmwareManagementFeature = true;
            logDebug(method, "hasFirmwareManagementFeature: " + hasFirmwareManagementFeature);

            Map<String, DeviceType> assetTypeMap = new HashMap<String, DeviceType>();
            assetTypes.stream().forEach(assetType -> {
                assetTypeMap.put(assetType.getId(), assetType);
            });

            Set<EligibleFirmwareChangeGroup> logicalEligibleGroupSet = new HashSet<>();
            if (!assetTypeMap.isEmpty()) {
                // STEP 2
                // derive a distinct set of asset type, hardware version,
                // firmware version based on devices
                DeviceSearchParams deviceSearchParams = new DeviceSearchParams();
                deviceSearchParams.addApplicationIds(applicationId);
                deviceSearchParams.setEnabled(true);
                deviceSearchParams.setSoftwareReleaseIdDefined(true);
                deviceSearchParams
                        .addDeviceTypeIds(assetTypeMap.keySet().toArray(new String[assetTypeMap.keySet().size()]));

                List<Device> devices = deviceService.getDeviceRepository().doFindAllDevices(deviceSearchParams);
                logDebug(method, "devices size: %s", devices.size());
                devices.stream().forEach(device -> {
                    DeviceType assetType = assetTypeMap.get(device.getDeviceTypeId());
                    Assert.notNull(assetType,
                            "assetType is missing from map! assetTypeId: " + device.getDeviceTypeId());
                    logicalEligibleGroupSet.add(
                            new EligibleFirmwareChangeGroup(device.getDeviceTypeId(), assetType.getRheaDeviceTypeId(),
                                    device.getSoftwareReleaseId(), assetType.getDeviceCategory()));
                });
                logDebug(method, "logicalEligibleGroupSet size: %s", logicalEligibleGroupSet.size());

                // STEP 3
                // derive a distinct set of asset type, hardware version,
                // firmware version based on gateways
                GatewaySearchParams gatewaySearchParams = new GatewaySearchParams();
                gatewaySearchParams.addApplicationIds(applicationId);
                gatewaySearchParams.setEnabled(true);
                gatewaySearchParams.setSoftwareReleaseIdDefined(true);
                gatewaySearchParams
                        .addDeviceTypeIds(assetTypeMap.keySet().toArray(new String[assetTypeMap.keySet().size()]));

                List<Gateway> gateways = gatewayService.getGatewayRepository().findGateways(gatewaySearchParams);
                logDebug(method, "gateways size: %s", gateways.size());
                gateways.stream().forEach(gateway -> {
                    DeviceType assetType = assetTypeMap.get(gateway.getDeviceTypeId());
                    Assert.notNull(assetType,
                            "assetType is missing from map! assetTypeId: " + gateway.getDeviceTypeId());
                    logicalEligibleGroupSet.add(
                            new EligibleFirmwareChangeGroup(gateway.getDeviceTypeId(), assetType.getRheaDeviceTypeId(),
                                    gateway.getSoftwareReleaseId(), assetType.getDeviceCategory()));
                });
                logDebug(method, "logicalEligibleGroupSet size: %s", logicalEligibleGroupSet.size());
            }
            logDebug(method, "logicalEligibleGroupSet size: %s", logicalEligibleGroupSet.size());

            // STEP 4
            // find software releases based on logical groups

            // if firmware management feature was purchased then include right
            // to use types
            EnumSet<RightToUseType> rtuTypes = EnumSet.of(RightToUseType.Unrestricted);
            if (hasFirmwareManagementFeature) {
                rtuTypes.add(RightToUseType.Public);
            }

            String[] rtuTypeNames = new String[rtuTypes.size()];
            int rightToUseNameCount = 0;
            for (RightToUseType rtut : rtuTypes) {
                rtuTypeNames[rightToUseNameCount++] = rtut.name();
            }

            String[] rheaDeviceTypeIds = logicalEligibleGroupSet.stream()
                    .map(logicalGroup -> logicalGroup.rheaDeviceTypeId).distinct().toArray(String[]::new);

            List<SoftwareRelease> softwareReleases = rheaClientSoftwareReleaseApi.findAll(null, null, rheaDeviceTypeIds,
                    rtuTypeNames, true, null);

            if (hasFirmwareManagementFeature) {
                softwareReleases.addAll(getPrivateApprovedSwReleases(application.getCompanyId(), rheaDeviceTypeIds));
            }

            logDebug(method, "softwareReleases size: %s", softwareReleases.size());

            // STEP 5
            // evaluate each software release
            softwareReleases.forEach(softwareRelease -> {
                softwareRelease.setRefSoftwareProduct(
                        requestCache.findSoftwareProductById(softwareRelease.getSoftwareProductId()));
                logDebug(method, "checking software release: %s major: %s minor: %s build: %s",
                        softwareRelease.getRefSoftwareProduct().getName(), softwareRelease.getMajor(),
                        softwareRelease.getMinor(), softwareRelease.getBuild());

                List<String> upgradeableFromIds = softwareRelease.getUpgradeableFromIds();
                List<String> deviceTypeIds = softwareRelease.getDeviceTypeIds();
                if (!upgradeableFromIds.isEmpty() && !deviceTypeIds.isEmpty()) {
                    for (EligibleFirmwareChangeGroup eligibleGroup : logicalEligibleGroupSet) {
                        if (upgradeableFromIds.contains(eligibleGroup.softwareReleaseId)
                                && deviceTypeIds.contains(eligibleGroup.rheaDeviceTypeId)) {
                            String key = eligibleGroup.softwareReleaseId + "|" + eligibleGroup.rheaDeviceTypeId;
                            EligibleFirmwareChangeGroup existingEligibleGroup = eligibleGroupResult.get(key);
                            if (existingEligibleGroup == null)
                                existingEligibleGroup = eligibleGroup;

                            existingEligibleGroup.newSoftwareReleaseIds.add(softwareRelease.getId());
                            eligibleGroupResult.put(key, eligibleGroup);
                        }
                    }
                }
            });

            // STEP 6
            // derive the count of devices and gateways
            SoftwareReleaseScheduleSearchParams params = new SoftwareReleaseScheduleSearchParams();
            params.addApplicationIds(applicationId);
            if (!eligibleGroupResult.isEmpty()) {
                Set<String> scheduledDevices = getSoftwareReleaseScheduleRepository()
                        .findSoftwareReleaseSchedules(params).stream()
                        .filter(schedule -> schedule.getStatus() == Status.SCHEDULED
                                || schedule.getStatus() == Status.INPROGRESS)
                        .map(SoftwareReleaseSchedule::getObjectIds).flatMap(Collection::stream)
                        .collect(Collectors.toSet());
                eligibleGroupResult.values().forEach(eligibleGroup -> {
                    switch (eligibleGroup.category) {
                    case DEVICE:
                        DeviceSearchParams deviceSearchParams = new DeviceSearchParams();
                        deviceSearchParams.addApplicationIds(applicationId);
                        deviceSearchParams.setEnabled(true);
                        deviceSearchParams.addDeviceTypeIds(eligibleGroup.deviceTypeId);
                        deviceSearchParams.addSoftwareReleaseIds(eligibleGroup.softwareReleaseId);
                        List<Device> devices = deviceService.getDeviceRepository().doFindAllDevices(deviceSearchParams);
                        int numberOfDevices = 0;
                        for (Device device : devices) {
                            if (!scheduledDevices.contains(device.getId())) {
                                numberOfDevices++;
                            }
                        }
                        eligibleGroup.setNumberOfDevices(numberOfDevices);
                        break;
                    case GATEWAY:
                        GatewaySearchParams gatewaySearchParams = new GatewaySearchParams();
                        gatewaySearchParams.addApplicationIds(applicationId);
                        gatewaySearchParams.setEnabled(true);
                        gatewaySearchParams.addDeviceTypeIds(eligibleGroup.deviceTypeId);
                        gatewaySearchParams.addSoftwareReleaseIds(eligibleGroup.softwareReleaseId);
                        List<Gateway> gateways = gatewayService.getGatewayRepository()
                                .findGateways(gatewaySearchParams);
                        int numberOfGateways = 0;
                        for (Gateway gateway : gateways) {
                            if (!scheduledDevices.contains(gateway.getId())) {
                                numberOfGateways++;
                            }
                        }
                        eligibleGroup.setNumberOfDevices(numberOfGateways);
                        break;
                    }

                    logDebug(method,
                            "eligibleGroup: deviceTypeId: %s, rheaDeviceTypeId: %s, softwareReleaseId: %s, newSoftwareReleaseId: %s, numberOfDevices: %s, newSoftwareVersions: %s",
                            eligibleGroup.deviceTypeId, eligibleGroup.rheaDeviceTypeId, eligibleGroup.softwareReleaseId,
                            eligibleGroup.newSoftwareReleaseIds, eligibleGroup.numberOfDevices,
                            eligibleGroup.newSoftwareReleaseIds.size());
                });
            }
        }

        return eligibleGroupResult.values();
    }

    public List<EligibleFirmwareChangeGroup> getEligibleUpgrades(String applicationId) {
        return getEligibleUpgrades(applicationId, null);
    }

    public List<EligibleFirmwareChangeGroup> getEligibleUpgrades(String applicationId, List<DeviceType> assetTypes) {
        Assert.hasText(applicationId, "applicationId is empty");

        Collection<EligibleFirmwareChangeGroup> eligibleGroups = getInnerEligibleModels(applicationId, assetTypes);
        if (eligibleGroups != null)
            return new ArrayList<>(eligibleGroups);
        else
            return Collections.emptyList();
    }

    public long getNumberOfEligibleUpgrades(String applicationId, boolean includePublicRTU, boolean includePrivateRTU) {
        return getInnerEligibleModels(applicationId, null).stream().mapToLong(entry -> entry.getNumberOfDevices())
                .sum();
    }

    public List<SoftwareRelease> getPrivateApprovedSwReleases(String companyId, String[] deviceTypesId) {
        List<SoftwareRelease> result = new ArrayList<>();
        List<String> swReleaseApprovedIds = new ArrayList<>();

        List<RTURequest> rtuRequests = clientRTURequestApi.findAll(new String[] { companyId },
                new String[] { "Approved" }, null);
        if (rtuRequests != null && !rtuRequests.isEmpty()) {
            swReleaseApprovedIds = rtuRequests.stream().map(rtu -> rtu.getSoftwareReleaseId())
                    .collect(Collectors.toList());
            if (!swReleaseApprovedIds.isEmpty()) {
                List<SoftwareRelease> softwareReleases = rheaClientSoftwareReleaseApi.findAll(null, null, deviceTypesId,
                        new String[] { RightToUseType.Private.name() }, true, null);

                for (SoftwareRelease softwareRelease : softwareReleases) {
                    if (swReleaseApprovedIds.contains(softwareRelease.getId()))
                        result.add(softwareRelease);
                }
            }
        }

        return result;
    }

    public void auditLog(SoftwareReleaseSchedule softwareReleaseSchedule, SoftwareReleaseTrans softwareReleaseTrans,
            String who, String type) {

        BaseDeviceAbstract asset = getAsset(softwareReleaseTrans.getDeviceCategory(),
                softwareReleaseTrans.getObjectId());

        getAuditLogService().save(AuditLogBuilder.create().type(type)
                .applicationId(softwareReleaseTrans.getApplicationId()).objectId(softwareReleaseSchedule.getId())
                .parameter("softwareReleaseTransId", softwareReleaseTrans.getId())
                .parameter("deviceCategory", softwareReleaseTrans.getDeviceCategory().name())
                .parameter("objectId", softwareReleaseTrans.getObjectId())
                .parameter("assetName", (asset == null ? "Unknown" : asset.getName()))
                .parameter("assetUid", (asset == null ? "Unknown" : asset.getUid()))
                .parameter("fromSoftwareReleaseId", softwareReleaseTrans.getFromSoftwareReleaseId())
                .parameter("toSoftwareReleaseId", softwareReleaseTrans.getToSoftwareReleaseId())
                .parameter("status", softwareReleaseTrans.getStatus().name())
                .parameter("error",
                        (StringUtils.isEmpty(softwareReleaseTrans.getError()) ? "N/A"
                                : softwareReleaseTrans.getError()))
                .parameter("retryCount", softwareReleaseTrans.getRetryCount().toString()).by(who));
    }

    private BaseDeviceAbstract getAsset(AcnDeviceCategory category, String objectId) {
        BaseDeviceAbstract result = null;
        switch (category) {
        case DEVICE:
            Device device = getKronosCache().findDeviceById(objectId);
            if (device != null)
                result = device;
            break;
        case GATEWAY:
            Gateway gateway = getKronosCache().findGatewayById(objectId);
            if (gateway != null)
                result = gateway;
            break;
        }
        return result;
    }
}
