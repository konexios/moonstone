package com.arrow.kronos.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.BaseDeviceAbstract;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.data.SoftwareReleaseTrans;
import com.arrow.kronos.data.SoftwareReleaseTrans.Status;
import com.arrow.kronos.repo.SoftwareReleaseTransRepository;
import com.arrow.kronos.repo.SoftwareReleaseTransSearchParams;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.TempToken;
import com.arrow.pegasus.repo.params.TempTokenSearchParams;
import com.arrow.pegasus.service.TempTokenService;

@Service
public class SoftwareReleaseTransService extends KronosServiceAbstract {

	@Autowired
	private SoftwareReleaseTransRepository softwareReleaseTransRepository;
	@Autowired
	private SoftwareReleaseService softwareReleaseService;
	@Autowired
	private SoftwareReleaseScheduleService softwareReleaseScheduleService;
	@Autowired
	private TempTokenService tempTokenService;

	public SoftwareReleaseTransRepository getSoftwareReleaseTransRepository() {
		return softwareReleaseTransRepository;
	}

	public SoftwareReleaseTrans create(SoftwareReleaseTrans softwareReleaseTrans, String who) {
		String method = "create";

		logicalCheck(softwareReleaseTrans, method, who);

		// insert
		softwareReleaseTrans = softwareReleaseTransRepository.doInsert(softwareReleaseTrans, who);

		// audit log
		auditLog(softwareReleaseTrans, who, KronosAuditLog.SoftwareReleaseTrans.CreateSoftwareReleaseTrans);

		return softwareReleaseTrans;
	}

	public SoftwareReleaseTrans update(SoftwareReleaseTrans softwareReleaseTrans, String who) {
		String method = "update";

		logicalCheck(softwareReleaseTrans, method, who);

		// update
		softwareReleaseTrans = softwareReleaseTransRepository.doSave(softwareReleaseTrans, who);

		// audit log
		auditLog(softwareReleaseTrans, who, KronosAuditLog.SoftwareReleaseTrans.UpdateSoftwareReleaseTrans);

		return softwareReleaseTrans;
	}

	public SoftwareReleaseTrans populateRefs(SoftwareReleaseTrans softwareReleaseTrans) {
		if (softwareReleaseTrans != null) {
			if (softwareReleaseTrans.getRefApplication() == null
			        && !StringUtils.isEmpty(softwareReleaseTrans.getApplicationId())) {
				softwareReleaseTrans.setRefApplication(
				        getCoreCacheService().findApplicationById(softwareReleaseTrans.getApplicationId()));
			}

			if (softwareReleaseTrans.getRefFromSoftwareRelease() == null
			        && !StringUtils.isEmpty(softwareReleaseTrans.getFromSoftwareReleaseId())) {
				softwareReleaseTrans.setRefFromSoftwareRelease(getRheaClientCacheApi()
				        .findSoftwareReleaseById(softwareReleaseTrans.getFromSoftwareReleaseId()));
			}

			if (softwareReleaseTrans.getRefToSoftwareRelease() == null
			        && !StringUtils.isEmpty(softwareReleaseTrans.getToSoftwareReleaseId())) {
				softwareReleaseTrans.setRefToSoftwareRelease(
				        getRheaClientCacheApi().findSoftwareReleaseById(softwareReleaseTrans.getToSoftwareReleaseId()));
			}

			if (softwareReleaseTrans.getRefRelatedSoftwareReleaseTrans() == null
			        && !StringUtils.isEmpty(softwareReleaseTrans.getRelatedSoftwareReleaseTransId())) {
				softwareReleaseTrans.setRefRelatedSoftwareReleaseTrans(softwareReleaseService
				        .findSoftwareReleaseTransById(softwareReleaseTrans.getRelatedSoftwareReleaseTransId()));
			}

			if (softwareReleaseTrans.getRefSoftwareReleaseSchedule() == null
			        && !StringUtils.isEmpty(softwareReleaseTrans.getSoftwareReleaseScheduleId())) {
				softwareReleaseTrans.setRefSoftwareReleaseSchedule(softwareReleaseScheduleService
				        .findSoftwareReleaseScheduleById(softwareReleaseTrans.getSoftwareReleaseScheduleId()));
			}
		}
		return softwareReleaseTrans;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return softwareReleaseTransRepository.deleteByApplicationId(applicationId);
	}

	public void deleteBy(Gateway gateway, String who) {
		String method = "deleteBy";

		// logical checks
		if (gateway == null) {
			logInfo(method, "gateway is null");
			throw new AcsLogicalException("gateway is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		Long count = softwareReleaseTransRepository.deleteByObjectIdAndDeviceCategory(gateway.getId(),
		        AcnDeviceCategory.GATEWAY.name());

		logInfo(method,
		        "Software Release Trans have been deleted for gateway id=" + gateway.getId() + ", total " + count);
	}

	public void deleteBy(Device device, String who) {
		String method = "deleteBy";

		// logical checks
		if (device == null) {
			logInfo(method, "device is null");
			throw new AcsLogicalException("device is null");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}

		Long count = softwareReleaseTransRepository.deleteByObjectIdAndDeviceCategory(device.getId(),
		        AcnDeviceCategory.DEVICE.name());

		logInfo(method,
		        "Software Release Trans have been deleted for device id=" + device.getId() + ", total " + count);
	}

	public List<SoftwareReleaseTrans> moveToError(String[] softwareReleaseTransIds, String who) {
		Assert.notEmpty(softwareReleaseTransIds, "softwareReleaseTransIds is empty");
		Assert.hasText(who, "who is empty");

		SoftwareReleaseTransSearchParams params = new SoftwareReleaseTransSearchParams();
		params.addIds(softwareReleaseTransIds);
		List<SoftwareReleaseTrans> softwareReleaseTransactions = softwareReleaseTransRepository
		        .findSoftwareReleaseTrans(params);

		List<SoftwareReleaseTrans> result = new ArrayList<>();
		for (SoftwareReleaseTrans softwareReleaseTrans : softwareReleaseTransactions) {
			softwareReleaseTrans = moveToError(softwareReleaseTrans, who);
			result.add(softwareReleaseTrans);
		}

		return result;
	}

	public SoftwareReleaseTrans moveToError(SoftwareReleaseTrans softwareReleaseTrans, String who) {
		Assert.notNull(softwareReleaseTrans, "softwareReleaseTrans is null");
		Assert.hasText(who, "who is empty");
		Assert.isTrue(softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.RECEIVED,
		        "softwareReleaseTrans must be in a RECEIVED state");

		String method = "moveToError";
		logDebug(method, "softwareReleaseTransId: %s, status: %s", softwareReleaseTrans.getId(),
		        softwareReleaseTrans.getStatus());

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
		        .getSoftwareReleaseScheduleRepository().findById(softwareReleaseTrans.getSoftwareReleaseScheduleId())
		        .orElse(null);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule not found! softwareReleaseScheduleId="
		        + softwareReleaseTrans.getSoftwareReleaseScheduleId());
		Assert.isTrue(softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.INPROGRESS,
		        "softwareReleaseSchedule must be in an INPROGRESS state");

		softwareReleaseTrans.setStatus(Status.ERROR);
		softwareReleaseTrans.setError("Manually moved to failed");
		softwareReleaseTrans.setEnded(null);
		softwareReleaseTrans = update(softwareReleaseTrans, who);
		expireTempToken(softwareReleaseSchedule, softwareReleaseTrans, who);

		// audit log for job
		softwareReleaseScheduleService.auditLog(softwareReleaseSchedule, softwareReleaseTrans, who,
		        KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetManuallyFailed);

		// audit log for transaction
		auditLog(softwareReleaseTrans, who, KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransManuallyFailed);

		return softwareReleaseTrans;
	}

	public List<SoftwareReleaseTrans> retryInProgressAsset(String[] softwareReleaseTransIds, String who) {
		Assert.notEmpty(softwareReleaseTransIds, "softwareReleaseTransIds is empty");
		Assert.hasText(who, "who is empty");

		SoftwareReleaseTransSearchParams params = new SoftwareReleaseTransSearchParams();
		params.addIds(softwareReleaseTransIds);
		List<SoftwareReleaseTrans> softwareReleaseTransactions = softwareReleaseTransRepository
		        .findSoftwareReleaseTrans(params);

		List<SoftwareReleaseTrans> result = new ArrayList<>();
		for (SoftwareReleaseTrans softwareReleaseTrans : softwareReleaseTransactions) {
			softwareReleaseTrans = retryInProgressAsset(softwareReleaseTrans, who);
			result.add(softwareReleaseTrans);
		}

		return result;
	}

	// AC-864, AC-865
	public SoftwareReleaseTrans retryInProgressAsset(SoftwareReleaseTrans softwareReleaseTrans, String who) {
		Assert.notNull(softwareReleaseTrans, "softwareReleaseTrans is null");
		Assert.hasText(who, "who is empty");
		Assert.isTrue(softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.INPROGRESS,
		        "softwareReleaseTrans must be in a INPROGRESS state");

		String method = "retryInProgressAsset";
		logDebug(method, "softwareReleaseTransId: %s, status: %s", softwareReleaseTrans.getId(),
		        softwareReleaseTrans.getStatus());

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
		        .getSoftwareReleaseScheduleRepository().findById(softwareReleaseTrans.getSoftwareReleaseScheduleId())
		        .orElse(null);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule not found! softwareReleaseScheduleId="
		        + softwareReleaseTrans.getSoftwareReleaseScheduleId());
		Assert.isTrue(softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.INPROGRESS,
		        "softwareReleaseSchedule must be in an INPROGRESS state");

		// audit log for job
		softwareReleaseScheduleService.auditLog(softwareReleaseSchedule, softwareReleaseTrans, who,
		        KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetManuallyRetried);

		// audit log for transaction
		auditLog(softwareReleaseTrans, who, KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransManuallyRetried);

		// step 1) mark asset RECEIVED
		softwareReleaseTrans.setStatus(Status.RECEIVED);
		softwareReleaseTrans.setError("Manually moved to received");
		softwareReleaseTrans.setEnded(null);
		softwareReleaseTrans = update(softwareReleaseTrans, who);

		// audit log for job
		softwareReleaseScheduleService.auditLog(softwareReleaseSchedule, softwareReleaseTrans, who,
		        KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetManuallyReceived);

		// audit log for transaction
		auditLog(softwareReleaseTrans, who, KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransManuallyReceived);

		// step 2) mark asset failed
		moveToError(softwareReleaseTrans, who);

		// step 3) retry asset
		retry(softwareReleaseTrans, who);

		return softwareReleaseTrans;
	}

	public SoftwareReleaseTrans expire(SoftwareReleaseTrans softwareReleaseTrans, String who) {
		Assert.notNull(softwareReleaseTrans, "softwareReleaseTrans is null");
		Assert.hasText(who, "who is empty");

		String method = "expire";
		logDebug(method, "softwareReleaseTransId: %s, status: %s", softwareReleaseTrans.getId(),
		        softwareReleaseTrans.getStatus());

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
		        .getSoftwareReleaseScheduleRepository().findById(softwareReleaseTrans.getSoftwareReleaseScheduleId())
		        .orElse(null);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule not found! softwareReleaseScheduleId="
		        + softwareReleaseTrans.getSoftwareReleaseScheduleId());
		Assert.isTrue(softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.INPROGRESS,
		        "softwareReleaseSchedule must be in an INPROGRESS state");

		switch (softwareReleaseTrans.getStatus()) {
		case INPROGRESS:
			break;
		case PENDING:
		case RECEIVED:
		case COMPLETE:
		case CANCELLED:
		case ERROR:
		case EXPIRED:
			throw new AcsLogicalException("Invalid state! Software Release Trans must be in an In Progress state.");
		default:
			throw new AcsLogicalException("Unsupported state! state=" + softwareReleaseTrans.getStatus());
		}

		softwareReleaseTrans.setStatus(Status.EXPIRED);
		softwareReleaseTrans.setError(null);
		softwareReleaseTrans.setEnded(null);
		softwareReleaseTrans = update(softwareReleaseTrans, who);
		expireTempToken(softwareReleaseSchedule, softwareReleaseTrans, who);

		// audit log for job
		softwareReleaseScheduleService.auditLog(softwareReleaseSchedule, softwareReleaseTrans, who,
		        KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetExpired);

		// audit log for transaction
		auditLog(softwareReleaseTrans, who, KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransExpired);

		return softwareReleaseTrans;
	}

	public void expireTempToken(SoftwareReleaseSchedule softwareReleaseSchedule,
	        SoftwareReleaseTrans softwareReleaseTrans, String who) {
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is null");
		Assert.notNull(softwareReleaseTrans, "softwareReleaseTrans is null");
		Assert.hasText(who, "who is empty");

		String method = "expireTempToken";
		logDebug(method, "softwareReleaseTransId: %s", softwareReleaseTrans.getId());

		TempTokenSearchParams params = new TempTokenSearchParams();
		params.addSoftwareReleaseTransIds(softwareReleaseTrans.getId());
		params.addApplicationIds(softwareReleaseTrans.getApplicationId());
		params.setExpired(false);

		List<TempToken> tokens = tempTokenService.getTempTokenRepository().findBy(params);

		boolean expiredSomeTokens = false;
		for (TempToken tempToken : tokens) {
			tempToken.setExpired(true);
			tempToken = tempTokenService.update(tempToken, who);

			expiredSomeTokens = true;
		}

		if (expiredSomeTokens) {
			// audit log for job
			softwareReleaseScheduleService.auditLog(softwareReleaseSchedule, softwareReleaseTrans, who,
			        KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetTempTokenExpired);

			// audit log for transaction
			auditLog(softwareReleaseTrans, who,
			        KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransTempTokenExpired);
		}
	}

	public List<SoftwareReleaseTrans> retry(String[] softwareReleaseTransIds, String who) {
		Assert.notEmpty(softwareReleaseTransIds, "softwareReleaseTransIds is empty");
		Assert.hasText(who, "who is empty");

		SoftwareReleaseTransSearchParams params = new SoftwareReleaseTransSearchParams();
		params.addIds(softwareReleaseTransIds);
		List<SoftwareReleaseTrans> softwareReleaseTransactions = softwareReleaseTransRepository
		        .findSoftwareReleaseTrans(params);

		List<SoftwareReleaseTrans> result = new ArrayList<>();
		for (SoftwareReleaseTrans softwareReleaseTrans : softwareReleaseTransactions) {
			softwareReleaseTrans = retry(softwareReleaseTrans, who);
			result.add(softwareReleaseTrans);
		}

		return result;
	}

	public SoftwareReleaseTrans retry(SoftwareReleaseTrans softwareReleaseTrans, String who) {
		Assert.notNull(softwareReleaseTrans, "softwareReleaseTrans is null");
		Assert.hasText(softwareReleaseTrans.getSoftwareReleaseScheduleId(), "softwareReleaseScheduleId is empty");
		Assert.hasText(who, "who is empty");

		String method = "retry";
		logDebug(method, "softwareReleaseTransId: %s, status: %s, who: %s", softwareReleaseTrans.getId(),
		        softwareReleaseTrans.getStatus(), who);

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
		        .getSoftwareReleaseScheduleRepository().findById(softwareReleaseTrans.getSoftwareReleaseScheduleId())
		        .orElse(null);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule not found! softwareReleaseScheduleId="
		        + softwareReleaseTrans.getSoftwareReleaseScheduleId());
		Assert.isTrue(softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.INPROGRESS,
		        "softwareReleaseSchedule must be in an INPROGRESS state");

		switch (softwareReleaseTrans.getStatus()) {
		case EXPIRED:
		case ERROR:
			break;
		case PENDING:
		case INPROGRESS:
		case RECEIVED:
		case COMPLETE:
		case CANCELLED: {
			logError(method,
			        "Invalid state! Asset's state must be either EXPIRED or ERROR in order to retry. status: %s",
			        softwareReleaseTrans.getStatus().name());
			throw new AcsLogicalException(
			        "Invalid state! Asset's state must be either EXPIRED or ERROR in order to retry.");
		}
		default:
			throw new AcsLogicalException("Unsupported state! state=" + softwareReleaseTrans.getStatus());
		}

		// set retry properties
		softwareReleaseTrans.setStatus(Status.PENDING);
		softwareReleaseTrans.setError(null);
		softwareReleaseTrans.setStarted(null);
		softwareReleaseTrans.setEnded(null);
		softwareReleaseTrans.setRetryCount((softwareReleaseTrans.getRetryCount() + 1));
		softwareReleaseTrans = update(softwareReleaseTrans, who);
		expireTempToken(softwareReleaseSchedule, softwareReleaseTrans, who);

		// audit log for job
		softwareReleaseScheduleService.auditLog(softwareReleaseSchedule, softwareReleaseTrans, who,
		        KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetRetried);

		// audit log for transaction
		auditLog(softwareReleaseTrans, who, KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransRetried);

		// re-start firmware update
		softwareReleaseService.start(softwareReleaseTrans.getId(), who);

		return softwareReleaseTrans;
	}

	public List<SoftwareReleaseTrans> cancel(String[] softwareReleaseTransIds, String who) {
		Assert.notEmpty(softwareReleaseTransIds, "softwareReleaseTransIds is empty");
		Assert.hasText(who, "who is empty");

		String method = "cancel";

		SoftwareReleaseTransSearchParams params = new SoftwareReleaseTransSearchParams();
		params.addIds(softwareReleaseTransIds);
		List<SoftwareReleaseTrans> softwareReleaseTransactions = softwareReleaseTransRepository
		        .findSoftwareReleaseTrans(params);
		logDebug(method, "softwareReleaseTransactions: %s", softwareReleaseTransactions.size());

		List<SoftwareReleaseTrans> result = new ArrayList<>();
		boolean checkAndComplete = false;
		for (int i = 0; i < softwareReleaseTransactions.size(); i++) {
			if (i == (softwareReleaseTransactions.size() - 1))
				checkAndComplete = true;

			SoftwareReleaseTrans softwareReleaseTrans = softwareReleaseTransactions.get(i);
			softwareReleaseTrans = cancel(softwareReleaseTrans, checkAndComplete, who);
			result.add(softwareReleaseTrans);
		}

		return result;
	}

	public SoftwareReleaseTrans cancel(SoftwareReleaseTrans softwareReleaseTrans, boolean checkAndComplete,
	        String who) {
		Assert.notNull(softwareReleaseTrans, "softwareReleaseTrans is null");
		Assert.hasText(who, "who is empty");

		String method = "cancel";
		logDebug(method, "softwareReleaseTransId: %s, status: %s", softwareReleaseTrans.getId(),
		        softwareReleaseTrans.getStatus());

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
		        .getSoftwareReleaseScheduleRepository().findById(softwareReleaseTrans.getSoftwareReleaseScheduleId())
		        .orElse(null);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule not found! softwareReleaseScheduleId="
		        + softwareReleaseTrans.getSoftwareReleaseScheduleId());
		Assert.isTrue(softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.INPROGRESS,
		        "softwareReleaseSchedule must be in an INPROGRESS state");

		switch (softwareReleaseTrans.getStatus()) {
		case INPROGRESS:
		case EXPIRED:
		case ERROR:
			break;
		case PENDING:
		case RECEIVED:
		case COMPLETE:
		case CANCELLED:
			logError(method,
			        "Invalid state! Asset's state must be either INPROGRESS, EXPIRED or ERROR in order to retry. status: %s",
			        softwareReleaseTrans.getStatus().name());
			throw new AcsLogicalException(
			        "Invalid state! Asset's state must be either INPROGRESS, EXPIRED or ERROR in order to retry.");
		default:
			throw new AcsLogicalException("Unsupported state! state=" + softwareReleaseTrans.getStatus());
		}

		softwareReleaseTrans.setStatus(Status.CANCELLED);
		softwareReleaseTrans.setError(null);
		softwareReleaseTrans.setEnded(Instant.now());
		softwareReleaseTrans = update(softwareReleaseTrans, who);
		expireTempToken(softwareReleaseSchedule, softwareReleaseTrans, who);

		// audit log for job
		softwareReleaseScheduleService.auditLog(softwareReleaseSchedule, softwareReleaseTrans, who,
		        KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetCancelled);

		// audit log for transaction
		auditLog(softwareReleaseTrans, who, KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransCancelled);

		if (checkAndComplete)
			softwareReleaseScheduleService.checkAndComplete(softwareReleaseSchedule, who);

		return softwareReleaseTrans;
	}

	private void logicalCheck(SoftwareReleaseTrans softwareReleaseTrans, String method, String who) {

		logInfo(method, "...");

		if (softwareReleaseTrans == null) {
			logInfo(method, "softwareReleaseTrans is null");
			throw new AcsLogicalException("softwareReleaseTrans is null");
		}

		if (StringUtils.isEmpty(softwareReleaseTrans.getApplicationId())) {
			logInfo(method, "applicationId is empty");
			throw new AcsLogicalException("applicationId is empty");
		}

		if (StringUtils.isEmpty(softwareReleaseTrans.getObjectId())) {
			logInfo(method, "objectId is empty");
			throw new AcsLogicalException("objectId is empty");
		}

		if (softwareReleaseTrans.getDeviceCategory() == null) {
			logInfo(method, "deviceCategory is null");
			throw new AcsLogicalException("deviceCategory is null");
		}

		if (StringUtils.isEmpty(softwareReleaseTrans.getFromSoftwareReleaseId())) {
			logInfo(method, "fromSoftwareReleaseId is empty");
			throw new AcsLogicalException("fromSoftwareReleaseId is empty");
		}

		if (StringUtils.isEmpty(softwareReleaseTrans.getToSoftwareReleaseId())) {
			logInfo(method, "toSoftwareReleaseId is empty");
			throw new AcsLogicalException("toSoftwareReleaseId is empty");
		}

		if (StringUtils.isEmpty(softwareReleaseTrans.getSoftwareReleaseScheduleId())) {
			logInfo(method, "softwareReleaseScheduleId is empty");
			throw new AcsLogicalException("softwareReleaseScheduleId is empty");
		}

		SoftwareReleaseTrans.Status status = softwareReleaseTrans.getStatus();
		if (status == null) {
			logInfo(method, "status is null");
			throw new AcsLogicalException("status is null");
		} else if (status == SoftwareReleaseTrans.Status.ERROR
		        && StringUtils.isEmpty(softwareReleaseTrans.getError())) {
			logInfo(method, "error is empty when status is Error");
			throw new AcsLogicalException("error is empty when status is Error");
		}

		if (StringUtils.isEmpty(who)) {
			logInfo(method, "who is empty");
			throw new AcsLogicalException("who is empty");
		}
	}

	public void auditLog(SoftwareReleaseTrans softwareReleaseTrans, String who, String type) {

		BaseDeviceAbstract asset = getAsset(softwareReleaseTrans.getDeviceCategory(),
		        softwareReleaseTrans.getObjectId());

		getAuditLogService().save(AuditLogBuilder.create().type(type)
		        .applicationId(softwareReleaseTrans.getApplicationId()).objectId(softwareReleaseTrans.getId())
		        .parameter("softwareReleaseScheduleId", softwareReleaseTrans.getSoftwareReleaseScheduleId())
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
