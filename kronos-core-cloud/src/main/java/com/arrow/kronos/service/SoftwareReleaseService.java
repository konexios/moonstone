package com.arrow.kronos.service;

import java.time.Instant;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.data.SoftwareReleaseTrans;
import com.arrow.kronos.repo.SoftwareReleaseScheduleSearchParams;
import com.arrow.kronos.repo.SoftwareReleaseTransSearchParams;
import com.arrow.pegasus.data.FileStore;
import com.arrow.pegasus.data.TempToken;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.event.EventBuilder;
import com.arrow.pegasus.data.event.EventParameter;
import com.arrow.pegasus.service.TempTokenService;
import com.arrow.rhea.client.api.ClientCacheApi;
import com.arrow.rhea.client.api.ClientDeviceTypeApi;
import com.arrow.rhea.client.api.ClientFileStoreApi;
import com.arrow.rhea.client.api.ClientSoftwareProductApi;
import com.arrow.rhea.client.api.ClientSoftwareReleaseApi;
import com.arrow.rhea.data.SoftwareProduct;
import com.arrow.rhea.data.SoftwareRelease;

import moonstone.acn.AcnEventNames;
import moonstone.acn.client.model.AcnDeviceCategory;
import moonstone.acs.AcsLogicalException;

@Service
public class SoftwareReleaseService extends KronosServiceAbstract {

	@Autowired
	private SoftwareReleaseScheduleService softwareReleaseScheduleService;
	@Autowired
	private SoftwareReleaseTransService softwareReleaseTransService;
	@Autowired
	private ClientCacheApi rheaClientCacheApi;
	@Autowired
	private ClientFileStoreApi clientFileStoreApi;
	@Autowired
	private GatewayCommandService gatewayCommandService;
	@Autowired
	private TempTokenService tempTokenService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private GatewayService gatewayService;
	@Autowired
	private ClientDeviceTypeApi clientDeviceTypeApi;
	@Autowired
	private ClientSoftwareProductApi clientSoftwareProductApi;
	@Autowired
	private ClientSoftwareReleaseApi clientSoftwareReleaseApi;

	public boolean isManagedAsset(String applicationId, String softwareReleaseScheduleId,
			AcnDeviceCategory deviceCategory, String... objectId) {
		Map<String, SoftwareReleaseSchedule> managingSoftwareReleaseScheduleMap = getManagingSoftwareReleaseSchedule(
				applicationId, softwareReleaseScheduleId, deviceCategory, objectId);
		return managingSoftwareReleaseScheduleMap.get(objectId) != null;
	}

	public Map<String, SoftwareReleaseSchedule> getManagingSoftwareReleaseSchedule(String applicationId,
			String softwareReleaseScheduleId, AcnDeviceCategory deviceCategory, String... objectId) {
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(softwareReleaseScheduleId, "softwareReleaseScheduleId is empty");
		Assert.notNull(deviceCategory, "deviceCategory is null");
		Assert.notEmpty(objectId, "objectId is empty");

		// determine if asset is currently being managed by another job
		SoftwareReleaseTransSearchParams softwareReleaseTransSearchParams = new SoftwareReleaseTransSearchParams();
		softwareReleaseTransSearchParams.addApplicationIds(applicationId);
		softwareReleaseTransSearchParams.addNotSoftwareReleaseScheduleIds(softwareReleaseScheduleId);
		softwareReleaseTransSearchParams.setDeviceCategories(EnumSet.of(deviceCategory));
		softwareReleaseTransSearchParams.addObjectIds(objectId);

		List<SoftwareReleaseTrans> existingAssetTransactions = softwareReleaseTransService
				.getSoftwareReleaseTransRepository().findSoftwareReleaseTrans(softwareReleaseTransSearchParams);

		Map<String, SoftwareReleaseSchedule> softwareReleaseScheduleMap = new HashMap<>();
		for (SoftwareReleaseTrans existingSoftwareReleaseTrans : existingAssetTransactions) {
			if (existingSoftwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.PENDING
					|| existingSoftwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.INPROGRESS
					|| existingSoftwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.RECEIVED) {
				// asset managed by another job
				existingSoftwareReleaseTrans = softwareReleaseTransService.populateRefs(existingSoftwareReleaseTrans);
				softwareReleaseScheduleMap.put(existingSoftwareReleaseTrans.getObjectId(),
						existingSoftwareReleaseTrans.getRefSoftwareReleaseSchedule());
			}
		}

		return softwareReleaseScheduleMap;
	}

	public SoftwareReleaseTrans create(String objectId, AcnDeviceCategory deviceCategory, String toSoftwareReleaseId,
			String softwareReleaseScheduleId, String who) {
		Assert.hasText(objectId, "objectId is empty");
		Assert.notNull(deviceCategory, "deviceCategory is null");
		Assert.hasText(toSoftwareReleaseId, "toSoftwareReleaseId is empty");
		Assert.hasText(who, "who is empty");

		String method = "create";
		logInfo(method, "...");

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.findSoftwareReleaseScheduleById(softwareReleaseScheduleId);
		Assert.notNull(softwareReleaseSchedule,
				"softwareReleaseSchedule not found! softwareReleaseScheduleId=" + softwareReleaseScheduleId);
		Assert.isTrue(softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.INPROGRESS,
				"softwareReleaseSchedule must be in a INPROGRESS state");

		String applicationId = null;
		String fromSoftwareReleaseId = null;
		String uid = null;

		// check if device or gateway has an assigned software release id
		switch (deviceCategory) {
		case GATEWAY:
			Gateway gateway = getKronosCache().findGatewayById(objectId);
			Assert.notNull(gateway, "Unable to find gateway! gatewayId=" + objectId);
			if (StringUtils.isEmpty(gateway.getSoftwareReleaseId())) {
				logInfo(method, "Gateway must have an assigned software release! gatewayId=" + objectId);
				throw new AcsLogicalException("Gateway must have an assigned software release!");
			} else {
				applicationId = gateway.getApplicationId();
				fromSoftwareReleaseId = gateway.getSoftwareReleaseId();
				uid = gateway.getUid();
			}
			break;
		case DEVICE:
			Device device = getKronosCache().findDeviceById(objectId);
			Assert.notNull(device, "Unable to find device! deviceId=" + objectId);
			if (StringUtils.isEmpty(device.getSoftwareReleaseId())) {
				logInfo(method, "Device must have an assigned software release! deviceId=" + objectId);
				throw new AcsLogicalException("Device must have an assigned software release!");
			} else {
				applicationId = device.getApplicationId();
				fromSoftwareReleaseId = device.getSoftwareReleaseId();
				uid = device.getUid();
			}
			break;
		default:
			logInfo(method, "Unsupported device category! category=" + deviceCategory.name());
			throw new AcsLogicalException("Unsupported device category! category=" + deviceCategory.name());
		}

		// ensure the following properties are defined
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(fromSoftwareReleaseId, "fromSoftwareReleaseId is empty");
		Assert.hasText(uid, "uid is empty");

		// create softwareReleaseTrans and populate
		SoftwareReleaseTrans softwareReleaseTrans = new SoftwareReleaseTrans();
		softwareReleaseTrans.setApplicationId(applicationId);
		softwareReleaseTrans.setDeviceCategory(deviceCategory);
		softwareReleaseTrans.setFromSoftwareReleaseId(fromSoftwareReleaseId);
		softwareReleaseTrans.setObjectId(objectId);
		softwareReleaseTrans.setSoftwareReleaseScheduleId(softwareReleaseScheduleId);
		softwareReleaseTrans.setStatus(SoftwareReleaseTrans.Status.PENDING);
		softwareReleaseTrans.setTimeToExpireSeconds(softwareReleaseSchedule.getTimeToExpireSeconds());
		softwareReleaseTrans.setToSoftwareReleaseId(toSoftwareReleaseId);
		softwareReleaseTrans.setUid(uid);

		// determine if asset is currently being managed by another job
		Map<String, SoftwareReleaseSchedule> managingSoftwareReleaseScheduleMap = getManagingSoftwareReleaseSchedule(
				applicationId, softwareReleaseScheduleId, deviceCategory, objectId);
		SoftwareReleaseSchedule managingSoftwareReleaseSchedule = managingSoftwareReleaseScheduleMap.get(objectId);
		if (managingSoftwareReleaseSchedule != null) {
			softwareReleaseTrans.setStatus(SoftwareReleaseTrans.Status.CANCELLED);
			softwareReleaseTrans
					.setError("Asset is being managed by job: " + managingSoftwareReleaseSchedule.getName());
		}
		// SoftwareReleaseTransSearchParams softwareReleaseTransSearchParams =
		// new SoftwareReleaseTransSearchParams();
		// softwareReleaseTransSearchParams.addApplicationIds(applicationId);
		// softwareReleaseTransSearchParams.setDeviceCategories(EnumSet.of(deviceCategory));
		// softwareReleaseTransSearchParams.addObjectIds(objectId);
		// List<SoftwareReleaseTrans> existingAssetTransactions =
		// softwareReleaseTransService
		// .getSoftwareReleaseTransRepository().findSoftwareReleaseTrans(softwareReleaseTransSearchParams);
		//
		// for (SoftwareReleaseTrans existingSoftwareReleaseTrans :
		// existingAssetTransactions) {
		// if
		// (!existingSoftwareReleaseTrans.getSoftwareReleaseScheduleId().equals(softwareReleaseScheduleId)
		// && (existingSoftwareReleaseTrans.getStatus() ==
		// SoftwareReleaseTrans.Status.PENDING
		// || existingSoftwareReleaseTrans.getStatus() ==
		// SoftwareReleaseTrans.Status.INPROGRESS
		// || existingSoftwareReleaseTrans.getStatus() ==
		// SoftwareReleaseTrans.Status.RECEIVED)) {
		// // asset is being managed by another job, set status to
		// // cancelled
		// existingSoftwareReleaseTrans =
		// softwareReleaseTransService.populateRefs(existingSoftwareReleaseTrans);
		// softwareReleaseTrans.setStatus(SoftwareReleaseTrans.Status.CANCELLED);
		// softwareReleaseTrans.setError("Asset is being managed by job: "
		// +
		// existingSoftwareReleaseTrans.getRefSoftwareReleaseSchedule().getName());
		// break;
		// }
		// }

		// persist
		softwareReleaseTrans = softwareReleaseTransService.create(softwareReleaseTrans, who);

		if (softwareReleaseTrans.getStatus() != SoftwareReleaseTrans.Status.ERROR
				&& softwareReleaseTrans.getStatus() != SoftwareReleaseTrans.Status.CANCELLED) {
			switch (deviceCategory) {
			case GATEWAY:
				Gateway gateway = getKronosCache().findGatewayById(objectId);
				Assert.notNull(gateway, "Unable to find gateway! gatewayId=" + objectId);
				if (!gateway.isEnabled()) {
					softwareReleaseTrans.setStatus(SoftwareReleaseTrans.Status.ERROR);
					softwareReleaseTrans.setError("Gateway is not enabled.");
				}
				break;
			case DEVICE:
				Device device = getKronosCache().findDeviceById(objectId);
				Assert.notNull(device, "Unable to find device! deviceId=" + objectId);
				if (!device.isEnabled()) {
					softwareReleaseTrans.setStatus(SoftwareReleaseTrans.Status.ERROR);
					softwareReleaseTrans.setError("Device is not enabled.");
				}
				break;
			default:
				logInfo(method, "Unsupported device category! category=" + deviceCategory.name());
				throw new AcsLogicalException("Unsupported device category! category=" + deviceCategory.name());
			}

			if (softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.ERROR)
				softwareReleaseTrans = softwareReleaseTransService.update(softwareReleaseTrans, who);
		}

		return softwareReleaseTransService.getSoftwareReleaseTransRepository().findById(softwareReleaseTrans.getId())
				.orElse(null);
	}

	private SoftwareRelease findSoftwareRelease(String softwareReleaseId) {
		Assert.hasText(softwareReleaseId, "softwareReleaseId is empty");

		return rheaClientCacheApi.findSoftwareReleaseById(softwareReleaseId);
	}

	public SoftwareReleaseTrans start(String softwareReleaseTransId, String who) {
		Assert.hasText(softwareReleaseTransId, "softwareReleaseId is empty");
		Assert.hasText(who, "who is empty");

		String method = "start";

		SoftwareReleaseTrans softwareReleaseTrans = softwareReleaseTransService.getSoftwareReleaseTransRepository()
				.findById(softwareReleaseTransId).orElse(null);
		Assert.notNull(softwareReleaseTrans,
				"softwareReleaseTrans not found! softwareReleaseTransId=" + softwareReleaseTransId);

		// TODO any assert failure should set the transaction to error
		Assert.notNull(softwareReleaseTrans.getDeviceCategory(), "deviceCategory is null");
		Assert.hasText(softwareReleaseTrans.getToSoftwareReleaseId(), "toSoftwareReleaseId is empty");
		Assert.hasText(softwareReleaseTrans.getFromSoftwareReleaseId(), "fromSoftwareReleaseId is empty");

		softwareReleaseTrans = update(softwareReleaseTransId, SoftwareReleaseTrans.Status.INPROGRESS, null, who);

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(softwareReleaseTrans.getSoftwareReleaseScheduleId())
				.orElse(null);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule not found! softwareReleaseScheduleId="
				+ softwareReleaseTrans.getSoftwareReleaseScheduleId());

		AcnDeviceCategory deviceCategory = softwareReleaseTrans.getDeviceCategory();
		SoftwareRelease fromSoftwareRelease = findSoftwareRelease(softwareReleaseTrans.getFromSoftwareReleaseId());
		Assert.notNull(fromSoftwareRelease,
				"Unable to find softwareRelease! id=" + softwareReleaseTrans.getFromSoftwareReleaseId());
		SoftwareRelease toSoftwareRelease = findSoftwareRelease(softwareReleaseTrans.getToSoftwareReleaseId());
		Assert.notNull(toSoftwareRelease,
				"Unable to find softwareRelease! id=" + softwareReleaseTrans.getToSoftwareReleaseId());
		Assert.hasText(toSoftwareRelease.getFileStoreId(), "fileStoreId is empty");

		// lookup fileStore using the fileStoreId of the softwareRelease
		FileStore fileStore = clientFileStoreApi.findById(toSoftwareRelease.getFileStoreId());
		Assert.notNull(fileStore, "fileStore is not found");

		String applicationId = null;
		String command = null;
		String hid = null;
		String gatewayId = null;
		String fromSoftwareVersion = buildSoftwareVersion(fromSoftwareRelease);
		String toSoftwareVersion = buildSoftwareVersion(toSoftwareRelease);

		switch (deviceCategory) {
		case GATEWAY:
			Gateway gateway = getKronosCache().findGatewayById(softwareReleaseTrans.getObjectId());
			Assert.notNull(gateway, "Unable to find gateway! id=" + softwareReleaseTrans.getObjectId());
			applicationId = gateway.getApplicationId();
			command = AcnEventNames.ServerToGateway.GATEWAY_SOFTWARE_RELEASE;
			hid = gateway.getHid();
			gatewayId = gateway.getId();
			break;
		case DEVICE:
			Device device = getKronosCache().findDeviceById(softwareReleaseTrans.getObjectId());
			Assert.notNull(device, "Unable to find device! id=" + softwareReleaseTrans.getObjectId());
			applicationId = device.getApplicationId();
			command = AcnEventNames.ServerToGateway.DEVICE_SOFTWARE_RELEASE;
			hid = device.getHid();
			gatewayId = device.getGatewayId();
			break;
		default:
			logInfo(method, "Unsupported device category! category=" + deviceCategory.name());
			throw new AcsLogicalException("Unsupported device category! category=" + deviceCategory.name());
		}

		TempToken tempToken = createTempToken(who, softwareReleaseTrans, softwareReleaseSchedule, fileStore,
				applicationId, command, hid, gatewayId, fromSoftwareVersion, toSoftwareVersion);

		sendCommandToGateway(who, method, softwareReleaseTrans, softwareReleaseSchedule, fileStore, applicationId,
				command, hid, gatewayId, fromSoftwareVersion, toSoftwareVersion, tempToken);

		return softwareReleaseTrans;
	}

	/**
	 * @param who
	 * @param softwareReleaseTrans
	 * @param softwareReleaseSchedule
	 * @param fileStore
	 * @param applicationId
	 * @param command
	 * @param hid
	 * @param gatewayId
	 * @param fromSoftwareVersion
	 * @param toSoftwareVersion
	 * @return
	 */
	private TempToken createTempToken(String who, SoftwareReleaseTrans softwareReleaseTrans,
			SoftwareReleaseSchedule softwareReleaseSchedule, FileStore fileStore, String applicationId, String command,
			String hid, String gatewayId, String fromSoftwareVersion, String toSoftwareVersion) {
		// create tempToken
		TempToken tempToken = new TempToken();
		tempToken.setApplicationId(applicationId);
		tempToken.setSingleUse(true);
		// TODO future enhancement, the time to expire should be persisted on
		// the transaction when it is created and then copied to the temp token,
		// currently this logic always get the time to expire from the job and
		// would not work correctly if the user retries and enters a new time to
		// expire
		tempToken.setTimeToExpireSeconds(
				softwareReleaseSchedule.getTimeToExpireSeconds() + KronosConstants.FOTA.TEMP_TOKEN_BUFFER);
		// software release properties
		tempToken.addProperty("softwareReleaseTransId", softwareReleaseTrans.getId());
		tempToken = tempTokenService.create(tempToken, who);

		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(command, "command is empty");
		Assert.hasText(hid, "hid is empty");
		Assert.hasText(gatewayId, "gatewayId is empty");
		Assert.hasText(fromSoftwareVersion, "fromSoftwareVersion is empty");
		Assert.hasText(toSoftwareVersion, "toSoftwareVersion is empty");
		Assert.hasText(fileStore.getMd5(), "md5 is empty");
		Assert.hasText(tempToken.getHid(), "tempToken.hid is empty");

		// capture audit log for job
		softwareReleaseScheduleService.auditLog(softwareReleaseSchedule, softwareReleaseTrans, who,
				KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetTempTokenCreated);

		// capture audit log for transaction
		softwareReleaseTransService.auditLog(softwareReleaseTrans, who,
				KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransTempTokenCreated);
		return tempToken;
	}

	/**
	 * @param who
	 * @param method
	 * @param softwareReleaseTrans
	 * @param softwareReleaseSchedule
	 * @param fileStore
	 * @param applicationId
	 * @param command
	 * @param hid
	 * @param gatewayId
	 * @param fromSoftwareVersion
	 * @param toSoftwareVersion
	 * @param tempToken
	 */
	private void sendCommandToGateway(String who, String method, SoftwareReleaseTrans softwareReleaseTrans,
			SoftwareReleaseSchedule softwareReleaseSchedule, FileStore fileStore, String applicationId, String command,
			String hid, String gatewayId, String fromSoftwareVersion, String toSoftwareVersion, TempToken tempToken) {
		// send command to gateway
		long timeToExpireMilliseconds = (softwareReleaseSchedule.getTimeToExpireSeconds()
				* KronosConstants.FOTA.ONE_SECOND_IN_MILLISECONDS);
		EventBuilder builder = EventBuilder.create().applicationId(applicationId).name(command)
				.parameter(EventParameter.InString("softwareReleaseScheduleHid", softwareReleaseSchedule.getHid()))
				.parameter(EventParameter.InString("softwareReleaseTransHid", softwareReleaseTrans.getHid()))
				.parameter(EventParameter.InString("hid", hid))
				.parameter(EventParameter.InString("fromSoftwareVersion", fromSoftwareVersion))
				.parameter(EventParameter.InString("toSoftwareVersion", toSoftwareVersion))
				.parameter(EventParameter.InString("md5checksum", fileStore.getMd5()))
				.parameter(EventParameter.InString("tempToken", tempToken.getHid()));
		Event event = gatewayCommandService.sendEvent(builder.build(), gatewayId, null, who, timeToExpireMilliseconds);
		logInfo(method, "event " + command + " has been sent to gatewayId=" + gatewayId + ", eventId=" + event.getId()
				+ ", timeToExpireMilliseconds=" + timeToExpireMilliseconds);

		// capture audit log for job
		softwareReleaseScheduleService.auditLog(softwareReleaseSchedule, softwareReleaseTrans, who,
				KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetCommandSentToGateway);

		// capture audit log for transaction
		softwareReleaseTransService.auditLog(softwareReleaseTrans, who,
				KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransCommandSentToGateway);
	}

	public SoftwareReleaseTrans received(String softwareReleaseTransId, String who) {
		return update(softwareReleaseTransId, SoftwareReleaseTrans.Status.RECEIVED, null, who);
	}

	public SoftwareReleaseTrans succeeded(String softwareReleaseTransId, String who) {
		return update(softwareReleaseTransId, SoftwareReleaseTrans.Status.COMPLETE, null, who);
	}

	public SoftwareReleaseTrans failed(String softwareReleaseTransId, String error, String who) {
		return update(softwareReleaseTransId, SoftwareReleaseTrans.Status.ERROR, error, who);
	}

	public SoftwareReleaseTrans retry(String softwareReleaseTransId, String who) {
		Assert.hasText(softwareReleaseTransId, "softwareReleaseTransId is empty");
		Assert.hasLength(who, "who is empty");

		SoftwareReleaseTrans softwareReleaseTrans = softwareReleaseTransService.getSoftwareReleaseTransRepository()
				.findById(softwareReleaseTransId).orElse(null);
		Assert.notNull(softwareReleaseTrans, "Unable to find softwareReleaseTrans! id=" + softwareReleaseTransId);

		return softwareReleaseTransService.retry(softwareReleaseTrans, who);
	}

	public SoftwareReleaseTrans update(String softwareReleaseTransId, SoftwareReleaseTrans.Status status, String error,
			String who) {
		Assert.hasText(softwareReleaseTransId, "softwareReleaseTransId is empty");
		Assert.notNull(status, "status is null");
		Assert.hasLength(who, "who is empty");

		String method = "update";
		logInfo(method, "...");

		SoftwareReleaseTrans softwareReleaseTrans = softwareReleaseTransService.getSoftwareReleaseTransRepository()
				.findById(softwareReleaseTransId).orElse(null);
		Assert.notNull(softwareReleaseTrans, "Unable to find softwareReleaseTrans! id=" + softwareReleaseTransId);

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(softwareReleaseTrans.getSoftwareReleaseScheduleId())
				.orElse(null);
		Assert.notNull(softwareReleaseTrans,
				"Unable to find softwareReleaseSchedule! id=" + softwareReleaseTrans.getSoftwareReleaseScheduleId());
		Assert.isTrue(softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.INPROGRESS,
				"softwareReleaseSchedule must be in a INPROGRESS state");

		String jobAuditLogType = KronosAuditLog.SoftwareReleaseSchedule.UpdateSoftwareReleaseSchedule;
		String transAuditLogType = KronosAuditLog.SoftwareReleaseTrans.UpdateSoftwareReleaseTrans;
		switch (status) {
		case PENDING:
			break;
		case INPROGRESS:
			Assert.isTrue(softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.PENDING,
					"softwareReleaseTrans must be in a PENDING state");

			jobAuditLogType = KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetStarted;
			transAuditLogType = KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransStarted;

			softwareReleaseTrans.setStarted(Instant.now());
			softwareReleaseTrans.setEnded(null);
			break;
		case RECEIVED:
			Assert.isTrue(softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.INPROGRESS,
					"softwareReleaseTrans must be in a INPROGRESS state");

			jobAuditLogType = KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetReceived;
			transAuditLogType = KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransReceived;

			break;
		case EXPIRED:
			Assert.isTrue(softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.INPROGRESS,
					"softwareReleaseTrans must be in a INPROGRESS state");

			jobAuditLogType = KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetExpired;
			transAuditLogType = KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransExpired;

			softwareReleaseTrans.setEnded(null);
			break;
		case CANCELLED:
			Assert.isTrue(
					(softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.INPROGRESS
							|| softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.ERROR),
					"softwareReleaseTrans must be in a INPROGRESS or ERROR state");

			jobAuditLogType = KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetCancelled;
			transAuditLogType = KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransCancelled;

			softwareReleaseTrans.setEnded(null);
			break;
		case COMPLETE:
			Assert.isTrue(softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.RECEIVED,
					"softwareReleaseTrans must be in a RECEIVED state");

			softwareReleaseTrans.setEnded(Instant.now());
			jobAuditLogType = KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetSucceeded;
			transAuditLogType = KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransSucceeded;
			break;
		case ERROR:
			Assert.isTrue(softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.RECEIVED,
					"softwareReleaseTrans must be in a RECEIVED state");

			jobAuditLogType = KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetFailed;
			transAuditLogType = KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransFailed;
			break;
		default:
			logInfo(method, "Unsupported status! status=" + status.name());
			throw new AcsLogicalException("Unsupported status! status=" + status.name());
		}

		softwareReleaseTrans.setStatus(status);
		softwareReleaseTrans.setError(error);

		// be paranoid because why not! :)
		if (softwareReleaseTrans.getTimeToExpireSeconds() == null || softwareReleaseTrans.getTimeToExpireSeconds() <= 0)
			softwareReleaseTrans.setTimeToExpireSeconds(KronosConstants.FOTA.DEFAULT_TIME_TO_EXPIRE_SECONDS);

		// persist changes
		softwareReleaseTrans = softwareReleaseTransService.update(softwareReleaseTrans, who);

		// capture audit log for job
		softwareReleaseScheduleService.auditLog(softwareReleaseSchedule, softwareReleaseTrans, who, jobAuditLogType);

		// capture audit log for transaction
		softwareReleaseTransService.auditLog(softwareReleaseTrans, who, transAuditLogType);

		// update device or gateway software
		if (status == SoftwareReleaseTrans.Status.COMPLETE) {
			switch (softwareReleaseTrans.getDeviceCategory()) {
			case DEVICE:
				Device device = deviceService.getDeviceRepository().findById(softwareReleaseTrans.getObjectId())
						.orElse(null);
				device.setSoftwareReleaseId(softwareReleaseTrans.getToSoftwareReleaseId());
				deviceService.update(device, who);

				break;
			case GATEWAY:
				Gateway gateway = gatewayService.getGatewayRepository().findById(softwareReleaseTrans.getObjectId())
						.orElse(null);
				gateway.setSoftwareReleaseId(softwareReleaseTrans.getToSoftwareReleaseId());
				gatewayService.update(gateway, who);

				break;
			default:
				logInfo(method, "Unsupported device category! deviceCategory="
						+ softwareReleaseTrans.getDeviceCategory().name());
				throw new AcsLogicalException("Unsupported device category! deviceCategory="
						+ softwareReleaseTrans.getDeviceCategory().name());
			}
		}

		if (status == SoftwareReleaseTrans.Status.COMPLETE || status == SoftwareReleaseTrans.Status.CANCELLED)
			softwareReleaseScheduleService.checkAndComplete(softwareReleaseSchedule, who);

		return softwareReleaseTransService.getSoftwareReleaseTransRepository().findById(softwareReleaseTransId)
				.orElse(null);
	}

	public String buildSoftwareVersion(SoftwareRelease softwareRelease) {
		Assert.notNull(softwareRelease, "softwareRelease is null");
		return softwareRelease.getMajor() + "." + softwareRelease.getMinor()
				+ (softwareRelease.getBuild() != null ? "." + softwareRelease.getBuild() : "");
	}

	public SoftwareReleaseSchedule findActiveSoftwareReleaseSchedule(String applicationId,
			String softwareReleaseScheduleId, String objectId, AcnDeviceCategory deviceCategory) {
		Assert.hasText(objectId, "objectId is null");

		String method = "findActiveSoftwareReleaseSchedule";
		logInfo(method, "...");
		logInfo(method, "softwareReleaseScheduleId: %s, objectId: %s, deviceCategory: %s", softwareReleaseScheduleId,
				objectId, deviceCategory);

		SoftwareReleaseScheduleSearchParams params = new SoftwareReleaseScheduleSearchParams();
		params.addApplicationIds(applicationId);
		params.addObjectIds(objectId);

		List<SoftwareReleaseSchedule> softwareReleaseSchedules = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findSoftwareReleaseSchedules(params);

		SoftwareReleaseSchedule result = null;
		if (softwareReleaseSchedules != null && !softwareReleaseSchedules.isEmpty()) {
			for (SoftwareReleaseSchedule softwareReleaseSchedule : softwareReleaseSchedules) {
				if (!softwareReleaseSchedule.getId().equals(softwareReleaseScheduleId)) {

					SoftwareReleaseTransSearchParams p = new SoftwareReleaseTransSearchParams();
					p.addApplicationIds(applicationId);
					p.addIds(softwareReleaseSchedule.getId());
					p.addObjectIds(objectId);

					List<SoftwareReleaseTrans> existingSoftwareReleaseTransactions = softwareReleaseTransService
							.getSoftwareReleaseTransRepository().findSoftwareReleaseTrans(p);
					for (SoftwareReleaseTrans softwareReleaseTrans : existingSoftwareReleaseTransactions) {

						switch (softwareReleaseTrans.getStatus()) {
						case PENDING:
						case INPROGRESS:
						case RECEIVED:
						case EXPIRED:
						case ERROR:
							result = softwareReleaseSchedule;
							break;
						case COMPLETE:
						case CANCELLED:
							break;
						default:
							throw new AcsLogicalException("Unsupport state! state=" + softwareReleaseTrans.getStatus());
						}
					}

					if (result != null)
						break;
				}
			}
		}

		// SoftwareReleaseSchedule result = null;
		//
		// SoftwareReleaseTransSearchParams params = new
		// SoftwareReleaseTransSearchParams();
		//
		// if (softwareReleaseScheduleId != null) {
		// params.addNotSoftwareReleaseScheduleIds(softwareReleaseScheduleId);
		// }
		// params.addObjectIds(objectId);
		// params.setDeviceCategories(EnumSet.of(deviceCategory));
		// EnumSet<Status> statuses = EnumSet.allOf(Status.class);
		// statuses.remove(Status.COMPLETE);
		// statuses.remove(Status.ERROR);
		// params.setStatuses(statuses);
		// List<SoftwareReleaseTrans> transList =
		// softwareReleaseTransService.getSoftwareReleaseTransRepository()
		// .findSoftwareReleaseTrans(params);
		// logInfo(method, "transList size: %s", (transList == null ? 0 :
		// transList.size()));
		//
		// if (transList != null && !transList.isEmpty()) {
		// for (SoftwareReleaseTrans trans : transList) {
		// logInfo(method, "softwareReleaseTrans status: %s, deviceCategory: %s,
		// softwareReleaseScheduleId: %s",
		// trans.getStatus(), trans.getDeviceCategory(),
		// trans.getSoftwareReleaseScheduleId());
		//
		// result =
		// softwareReleaseScheduleService.getSoftwareReleaseScheduleRepository()
		// .findById(trans.getSoftwareReleaseScheduleId());
		// }
		// }
		return result;
	}

	public SoftwareReleaseSchedule cancel(SoftwareReleaseSchedule softwareReleaseSchedule, boolean cancelReceived,
			String who) {
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is null");
		return cancel(softwareReleaseSchedule, softwareReleaseSchedule.getObjectIds(), cancelReceived, who);
	}

	public SoftwareReleaseSchedule cancel(SoftwareReleaseSchedule softwareReleaseSchedule, List<String> objectIds,
			boolean cancelReceived, String who) {
		String method = "cancel";
		logDebug(method, "...");

		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is null");

		logDebug(method, "cancel %s job, softwareReleaseScheduleId: %s", softwareReleaseSchedule.getStatus(),
				softwareReleaseSchedule.getId());

		switch (softwareReleaseSchedule.getStatus()) {
		// case INPROGRESS:
		// Assert.notEmpty(objectIds, "objectIds is empty");
		// // find transactions by softwareReleaseScheduleId and objectIds
		// SoftwareReleaseTransSearchParams params = new
		// SoftwareReleaseTransSearchParams();
		// params.addSoftwareReleaseScheduleIds(softwareReleaseSchedule.getId());
		// objectIds.forEach(params::addObjectIds);
		// List<SoftwareReleaseTrans> transactions =
		// softwareReleaseTransService.getSoftwareReleaseTransRepository()
		// .findSoftwareReleaseTrans(params);
		// // cancel if found
		// if (!transactions.isEmpty()) {
		// // cancel each transaction
		// transactions.forEach(trans ->
		// softwareReleaseTransService.cancel(trans, cancelReceived, who));
		// // check and complete the job
		// softwareReleaseSchedule =
		// softwareReleaseScheduleService.checkAndComplete(softwareReleaseSchedule,
		// who);
		// } else {
		// logInfo(method, "no transactions found, softwareReleaseScheduleId:
		// %s, objectIds: %s",
		// softwareReleaseSchedule.getId(), objectIds);
		// }
		// break;
		case SCHEDULED:
			softwareReleaseSchedule = softwareReleaseScheduleService.cancel(softwareReleaseSchedule, who);
			break;
		default:
			throw new AcsLogicalException("Only SCHEDULED softwareReleaseSchedule can be cancelled");
		}
		return softwareReleaseSchedule;
	}

	public List<SoftwareReleaseSchedule> findActiveSoftwareReleaseSchedules(String applicationId, String objectId,
			AcnDeviceCategory deviceCategory) {
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(objectId, "objectId is empty");
		Assert.notNull(deviceCategory, "deviceCategory is null");

		SoftwareReleaseScheduleSearchParams params = new SoftwareReleaseScheduleSearchParams();
		// applicationId
		params.addApplicationIds(applicationId);
		// status
		EnumSet<SoftwareReleaseSchedule.Status> statuses = EnumSet.allOf(SoftwareReleaseSchedule.Status.class);
		statuses.remove(SoftwareReleaseSchedule.Status.COMPLETE);
		statuses.remove(SoftwareReleaseSchedule.Status.CANCELLED);
		params.setStatuses(statuses);
		// objectId
		params.addObjectIds(objectId);
		// deviceCategory
		params.setDeviceCategories(EnumSet.of(deviceCategory));

		return softwareReleaseScheduleService.getSoftwareReleaseScheduleRepository()
				.findSoftwareReleaseSchedules(params);
	}

	public SoftwareReleaseTrans findSoftwareReleaseTransById(String softwareReleaseTransId) {

		SoftwareReleaseTrans softwareReleaseTrans = softwareReleaseTransService.getSoftwareReleaseTransRepository()
				.findById(softwareReleaseTransId).orElse(null);
		Assert.notNull(softwareReleaseTrans, "Unable to find softwareReleaseTrans! id=" + softwareReleaseTransId);

		return softwareReleaseTrans;
	}

	/**
	 * @param deviceTypeId    Kronos deviceTypeId
	 * @param softwareName    Rhea SoftwareProduct name
	 * @param softwareVersion Rhea SoftwareRelease major.minor.build
	 * @return Rhea SoftwareRelease id; null if not found
	 */
	public String findRheaSoftwareReleaseId(String deviceTypeId, String softwareName, String softwareVersion) {
		String method = "findRheaSoftwareReleaseId";
		if (StringUtils.isBlank(deviceTypeId) || StringUtils.isBlank(softwareName)
				|| StringUtils.isBlank(softwareVersion)) {
			return null;
		}
		String rheaDeviceTypeId = findRheaDeviceTypeId(deviceTypeId);
		if (rheaDeviceTypeId == null) {
			logInfo(method, "Rhea deviceTypeId not found for Kronos deviceTypeId: %s", deviceTypeId);
			return null;
		}
		logDebug(method, "rheaDeviceTypeId: %s", rheaDeviceTypeId);
		String[] deviceTypeIds = new String[] { rheaDeviceTypeId };

		final Map<String, SoftwareProduct> softwareProducts = new HashMap<>();
		List<SoftwareRelease> softwareReleases = clientSoftwareReleaseApi
				// find software releases by Rhea deviceTypeId
				.findAll(null, null, deviceTypeIds, null, true, null).stream()
				// filter by softwareVersion
				.filter(softwareRelease -> StringUtils.equals(buildSoftwareVersion(softwareRelease), softwareVersion))
				// filter by softwareName
				.filter(softwareRelease -> {
					String softwareProductId = softwareRelease.getSoftwareProductId();
					SoftwareProduct softwareProduct = softwareProducts.get(softwareProductId);
					if (softwareProduct == null) {
						softwareProduct = clientSoftwareProductApi.findById(softwareProductId);
						if (softwareProduct != null) {
							softwareProducts.put(softwareProductId, softwareProduct);
						}
					}
					return softwareProduct != null && softwareProduct.isEnabled()
							&& StringUtils.equals(softwareProduct.getName(), softwareName);
				}).collect(Collectors.toList());
		if (softwareReleases.isEmpty()) {
			logInfo(method, "softwareRelease not found, name: %s, version: %s", softwareName, softwareVersion);
			return null;
		} else if (softwareReleases.size() > 1) {
			logWarn(method, "found multiple softwareReleases! name: %s, version: %s", softwareName, softwareVersion);
		}
		SoftwareRelease softwareRelease = softwareReleases.get(0);
		return softwareRelease.getId();
	}

	/**
	 * @param deviceTypeId Kronos deviceTypeId
	 * @return Rhea deviceTypeId; null if not found
	 */
	public String findRheaDeviceTypeId(String deviceTypeId) {
		String method = "findRheaDeviceTypeId";
		DeviceType deviceType = getKronosCache().findDeviceTypeById(deviceTypeId);
		if (deviceType == null) {
			logDebug(method, "deviceType not found for id: %s", deviceTypeId);
			return null;
		}
		String rheaDeviceTypeId = deviceType.getRheaDeviceTypeId();
		if (StringUtils.isBlank(rheaDeviceTypeId)) {
			return null;
		}
		com.arrow.rhea.data.DeviceType rheaDeviceType = clientDeviceTypeApi.findById(rheaDeviceTypeId);
		if (rheaDeviceType == null) {
			logDebug(method, "rheaDeviceType not found for id: %s", rheaDeviceTypeId);
			return null;
		}
		if (!rheaDeviceType.isEnabled()) {
			logDebug(method, "rheaDeviceType is disabled, id: %s", rheaDeviceTypeId);
			return null;
		}
		return rheaDeviceType.getId();
	}
}
