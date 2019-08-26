package com.arrow.kronos.api;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.data.SoftwareReleaseTrans;
import com.arrow.kronos.service.SoftwareReleaseScheduleService;
import com.arrow.kronos.service.SoftwareReleaseTransService;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.DocumentAbstract;
import com.arrow.pegasus.data.FileStore;
import com.arrow.pegasus.data.TempToken;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.service.TempTokenService;
import com.arrow.rhea.client.api.ClientFileStoreApi;
import com.arrow.rhea.client.api.ClientSoftwareReleaseApi;
import com.arrow.rhea.data.SoftwareRelease;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.annotations.ApiOperation;
import moonstone.acn.client.model.AcnDeviceCategory;
import moonstone.acn.client.model.SoftwareReleaseTransModel;
import moonstone.acn.client.model.SoftwareReleaseTransStatus;
import moonstone.acn.client.model.SoftwareReleaseUpgradeModel;
import moonstone.acs.AcsLogicalException;
import moonstone.acs.AcsUtils;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.HidModel;
import moonstone.acs.client.model.StatusModel;

@RestController
@RequestMapping("/api/v1/kronos/software/releases/transactions")
public class SoftwareReleaseTransApi extends SoftwareReleaseApiAbstract {

	@Autowired
	private SoftwareReleaseScheduleService softwareReleaseScheduleService;
	@Autowired
	private SoftwareReleaseTransService softwareReleaseTransService;
	@Autowired
	private TempTokenService tempTokenService;
	// @Autowired
	// private ClientDeviceCategoryApi clientDeviceCategoryApi;
	@Autowired
	private ClientSoftwareReleaseApi clientSoftwareReleaseApi;
	@Autowired
	private ClientFileStoreApi clientFileStoreApi;

	@ApiOperation(value = "create new software release transaction for a gateway", response = HidModel.class)
	@RequestMapping(path = "/gateways/upgrade", method = RequestMethod.POST)
	public HidModel upgradeGateway(@RequestBody(required = false) SoftwareReleaseUpgradeModel body,
			HttpServletRequest request) {
		String method = "upgradeGateway";

		SoftwareReleaseUpgradeModel model = JsonUtils.fromJson(getApiPayload(), SoftwareReleaseUpgradeModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getObjectHid(), "gateway hid is empty");
		Assert.hasText(model.getToSoftwareReleaseHid(), "toSoftwareReleaseHid is empty");

		// lookup Rhea Device Category (Gateway)
		// DeviceCategory deviceCategory =
		// clientDeviceCategoryApi.findByName("Gateway");
		// Assert.notNull(deviceCategory, "deviceCategory is not found");

		return doUpgrade(model, AcnDeviceCategory.GATEWAY, method, request);
	}

	@ApiOperation(value = "create new software release transaction for a device", response = HidModel.class)
	@RequestMapping(path = "/devices/upgrade", method = RequestMethod.POST)
	public HidModel upgradeDevice(@RequestBody(required = false) SoftwareReleaseUpgradeModel body,
			HttpServletRequest request) {
		String method = "upgradeDevice";

		SoftwareReleaseUpgradeModel model = JsonUtils.fromJson(getApiPayload(), SoftwareReleaseUpgradeModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getObjectHid(), "device hid is empty");
		Assert.hasText(model.getToSoftwareReleaseHid(), "toSoftwareReleaseHid is empty");

		return doUpgrade(model, AcnDeviceCategory.DEVICE, method, request);
	}

	@ApiOperation(value = "find software release trans by hid", response = SoftwareReleaseTransModel.class)
	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public SoftwareReleaseTransModel findByHid(@PathVariable(name = "hid") String hid) {
		AccessKey accessKey = validateCanReadApplication(getProductSystemName());
		SoftwareReleaseTrans softwareReleaseTrans = getSoftwareReleaseTransService().getSoftwareReleaseTransRepository()
				.doFindByHid(hid);
		Assert.notNull(softwareReleaseTrans, "softwareReleaseTrans is not found, hid=" + hid);
		Assert.isTrue(accessKey.getApplicationId().equals(softwareReleaseTrans.getApplicationId()),
				"application does not match");

		return buildSoftwareReleaseTransModel(softwareReleaseTrans);
	}

	private SoftwareReleaseTransModel buildSoftwareReleaseTransModel(SoftwareReleaseTrans trans) {
		Assert.notNull(trans, "softwareReleaseTrans is null");
		SoftwareReleaseTransModel result = buildModel(new SoftwareReleaseTransModel(), trans);
		Application application = getCoreCacheService().findApplicationById(trans.getApplicationId());
		Assert.notNull(application, "application is not found");
		result.setApplicationHid(application.getHid());
		result.setDeviceCategory(trans.getDeviceCategory());
		if (trans.getEnded() != null) {
			result.setEnded(trans.getEnded().toString());
		}
		result.setError(trans.getError());
		SoftwareRelease fromSoftwareRelease = getClientCacheApi()
				.findSoftwareReleaseById(trans.getFromSoftwareReleaseId());
		Assert.notNull(fromSoftwareRelease, "fromSoftwareRelease is not found");
		result.setFromSoftwareReleaseHid(fromSoftwareRelease.getHid());
		DocumentAbstract asset = findById(trans.getDeviceCategory()).apply(trans.getObjectId());
		Assert.notNull(asset, "asset is not found");
		result.setObjectHid(asset.getHid());
		if (trans.getRelatedSoftwareReleaseTransId() != null) {
			SoftwareReleaseTrans relatedTrans = getSoftwareReleaseTransService().getSoftwareReleaseTransRepository()
					.findById(trans.getRelatedSoftwareReleaseTransId()).orElse(null);
			Assert.notNull(relatedTrans, "relatedTrans is not found");
			result.setRelatedSoftwareReleaseTransHid(relatedTrans.getHid());
		}
		if (trans.getStarted() != null) {
			result.setStarted(trans.getStarted().toString());
		}
		result.setStatus(SoftwareReleaseTransStatus.valueOf(trans.getStatus().name()));
		SoftwareRelease toSoftwareRelease = getClientCacheApi().findSoftwareReleaseById(trans.getToSoftwareReleaseId());
		Assert.notNull(toSoftwareRelease, "toSoftwareRelease is not found");
		result.setToSoftwareReleaseHid(toSoftwareRelease.getHid());
		result.setTimeToExpireSeconds(trans.getTimeToExpireSeconds());
		return result;
	}

	@ApiOperation(value = "mark software release transaction as received", response = StatusModel.class)
	@RequestMapping(path = "/{hid}/received", method = RequestMethod.PUT)
	public StatusModel putReceived(@PathVariable String hid, HttpServletRequest request) {
		String method = "putReceived";

		SoftwareReleaseTrans softwareReleaseTrans = getSoftwareReleaseTransService().getSoftwareReleaseTransRepository()
				.findByHid(hid);
		Assert.notNull(softwareReleaseTrans, "softwareReleaseTrans is null");

		AccessKey accessKey = findAccessKey(softwareReleaseTrans);
		Assert.notNull(accessKey, "accessKey is null");

		auditLog(method, softwareReleaseTrans.getApplicationId(), softwareReleaseTrans.getId(), accessKey.getId(),
				request);

		getSoftwareReleaseService().received(softwareReleaseTrans.getId(), accessKey.getPri());

		return StatusModel.OK;
	}

	@ApiOperation(value = "mark software release transaction as succeeded", response = StatusModel.class)
	@RequestMapping(path = "/{hid}/succeeded", method = RequestMethod.PUT)
	public StatusModel putSucceeded(@PathVariable String hid, HttpServletRequest request) {
		String method = "putSucceeded";

		SoftwareReleaseTrans softwareReleaseTrans = getSoftwareReleaseTransService().getSoftwareReleaseTransRepository()
				.findByHid(hid);
		Assert.notNull(softwareReleaseTrans, "softwareReleaseTrans is null");

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(softwareReleaseTrans.getSoftwareReleaseScheduleId())
				.orElse(null);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule not found! softwareReleaseScheduleId="
				+ softwareReleaseTrans.getSoftwareReleaseScheduleId());

		AccessKey accessKey = findAccessKey(softwareReleaseTrans);
		Assert.notNull(accessKey, "accessKey is null");

		auditLog(method, softwareReleaseTrans.getApplicationId(), softwareReleaseTrans.getId(), accessKey.getId(),
				request);

		getSoftwareReleaseService().succeeded(softwareReleaseTrans.getId(), accessKey.getPri());

		// expire the temporary token
		softwareReleaseTransService.expireTempToken(softwareReleaseSchedule, softwareReleaseTrans, accessKey.getPri());

		return StatusModel.OK;
	}

	@ApiOperation(value = "mark software release transaction as failed", response = StatusModel.class)
	@RequestMapping(path = "/{hid}/failed", method = RequestMethod.PUT)
	public StatusModel putFailed(@PathVariable String hid, @RequestBody(required = false) Map<String, String> body,
			HttpServletRequest request) {
		String method = "putFailed";

		Map<String, String> parameters = JsonUtils.fromJson(getApiPayload(), new TypeReference<Map<String, String>>() {
		});
		SoftwareReleaseTrans softwareReleaseTrans = getSoftwareReleaseTransService().getSoftwareReleaseTransRepository()
				.findByHid(hid);
		Assert.notNull(softwareReleaseTrans, "softwareReleaseTrans is null");

		SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
				.getSoftwareReleaseScheduleRepository().findById(softwareReleaseTrans.getSoftwareReleaseScheduleId())
				.orElse(null);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule not found! softwareReleaseScheduleId="
				+ softwareReleaseTrans.getSoftwareReleaseScheduleId());

		AccessKey accessKey = findAccessKey(softwareReleaseTrans);
		Assert.notNull(accessKey, "accessKey is null");

		auditLog(method, softwareReleaseTrans.getApplicationId(), softwareReleaseTrans.getId(), accessKey.getId(),
				request);

		getSoftwareReleaseService().failed(softwareReleaseTrans.getId(), parameters.get("error"), accessKey.getPri());

		// expire the temporary token
		softwareReleaseTransService.expireTempToken(softwareReleaseSchedule, softwareReleaseTrans, accessKey.getPri());

		return StatusModel.OK;
	}

	@ApiOperation(value = "retry a software release transaction", response = StatusModel.class)
	@RequestMapping(path = "/{hid}/retry", method = RequestMethod.PUT)
	public StatusModel putRetry(@PathVariable String hid, HttpServletRequest request) {
		String method = "putRetry";

		SoftwareReleaseTrans softwareReleaseTrans = getSoftwareReleaseTransService().getSoftwareReleaseTransRepository()
				.findByHid(hid);
		Assert.notNull(softwareReleaseTrans, "softwareReleaseTrans is null");

		AccessKey accessKey = findAccessKey(softwareReleaseTrans);
		Assert.notNull(accessKey, "accessKey is null");

		auditLog(method, softwareReleaseTrans.getApplicationId(), softwareReleaseTrans.getId(), accessKey.getId(),
				request);

		getSoftwareReleaseService().retry(softwareReleaseTrans.getId(), accessKey.getPri());

		return StatusModel.OK;
	}

	/*
	 * Don't need to expose, it should go through start, either api or gui AC-632
	 * 
	 * @ApiOperation(value = "create new software release transaction", response =
	 * HidModel.class)
	 * 
	 * @RequestMapping(path = "", method = RequestMethod.POST) public HidModel
	 * create(@RequestBody(required = false) SoftwareReleaseTransRegistrationModel
	 * body, HttpServletRequest request) { String method = "create";
	 * 
	 * SoftwareReleaseTransRegistrationModel model =
	 * JsonUtils.fromJson(getApiPayload(),
	 * SoftwareReleaseTransRegistrationModel.class);
	 * 
	 * Assert.notNull(model, "model is null");
	 * Assert.notNull(model.getDeviceCategory(), "deviceCategory is null");
	 * Assert.hasText(model.getObjectHid(), "objectHid is empty");
	 * Assert.hasText(model.getFromSoftwareReleaseHid(),
	 * "fromSoftwareReleaseHid is empty");
	 * Assert.hasText(model.getToSoftwareReleaseHid(),
	 * "toSoftwareReleaseHid is empty");
	 * 
	 * DocumentAbstract obj =
	 * findByHid(model.getDeviceCategory()).apply(model.getObjectHid());
	 * Assert.notNull(obj, "object is not found"); AccessKey accessKey =
	 * validateAccessKey(model.getDeviceCategory()).apply(obj.getHid());
	 * Assert.notNull(accessKey, "accessKey is null");
	 * 
	 * AuditLog auditLog = auditLog(method, accessKey.getApplicationId(), null,
	 * accessKey.getId(), request);
	 * 
	 * SoftwareReleaseTrans trans = new SoftwareReleaseTrans();
	 * trans.setObjectId(obj.getId());
	 * trans.setApplicationId(accessKey.getApplicationId());
	 * trans.setDeviceCategory(model.getDeviceCategory());
	 * 
	 * // AC-632 if (model.getDeviceCategory() == AcnDeviceCategory.DEVICE) { Device
	 * device = (Device) obj; trans.setUid(device.getUid()); } else if
	 * (model.getDeviceCategory() == AcnDeviceCategory.GATEWAY) { Gateway gateway =
	 * (Gateway) obj; trans.setUid(gateway.getUid()); }
	 * 
	 * trans.setError(model.getError());
	 * 
	 * if (StringUtils.isNotBlank(model.getSoftwareReleaseScheduleHid())) {
	 * SoftwareReleaseSchedule softwareReleaseSchedule =
	 * getSoftwareReleaseScheduleService()
	 * .getSoftwareReleaseScheduleRepository().doFindByHid(model.
	 * getSoftwareReleaseScheduleHid()); Assert.notNull(softwareReleaseSchedule,
	 * "softwareReleaseSchedule is not found");
	 * Assert.isTrue((trans.getDeviceCategory() ==
	 * softwareReleaseSchedule.getDeviceCategory()),
	 * "deviceCategory does not match");
	 * trans.setSoftwareReleaseScheduleId(softwareReleaseSchedule.getId());
	 * trans.setTimeToExpireSeconds(softwareReleaseSchedule.
	 * getTimeToExpireSeconds()); }
	 * 
	 * trans.setFromSoftwareReleaseId(findSoftwareReleaseId(model.
	 * getFromSoftwareReleaseHid()));
	 * trans.setToSoftwareReleaseId(findSoftwareReleaseId(model.
	 * getToSoftwareReleaseHid()));
	 * trans.setStatus(buildSoftwareReleaseTransStatus(model.getStatus()));
	 * 
	 * if (StringUtils.isNotBlank(model.getRelatedSoftwareReleaseTransHid())) {
	 * SoftwareReleaseTrans softwareReleaseTrans = getSoftwareReleaseTransService()
	 * .getSoftwareReleaseTransRepository().findByHid(model.
	 * getRelatedSoftwareReleaseTransHid()); Assert.notNull(softwareReleaseTrans,
	 * "softwareReleaseTrans is not found" );
	 * trans.setRelatedSoftwareReleaseTransId(softwareReleaseTrans.getId()); }
	 * 
	 * trans = getSoftwareReleaseTransService().create(trans, accessKey.getPri());
	 * 
	 * getAuditLogService().getAuditLogRepository().doSave(auditLog,
	 * accessKey.getId());
	 * 
	 * return new HidModel().withHid(trans.getHid()).withMessage("OK"); }
	 * 
	 * 
	 * @ApiOperation(value = "start software release transaction", response =
	 * StatusModel.class)
	 * 
	 * @RequestMapping(path = "/{hid}/start", method = RequestMethod.POST) public
	 * StatusModel start(@PathVariable String hid, HttpServletRequest request) {
	 * String method = "start";
	 * 
	 * SoftwareReleaseTrans softwareReleaseTrans =
	 * getSoftwareReleaseTransService().getSoftwareReleaseTransRepository()
	 * .doFindByHid(hid); Assert.notNull(softwareReleaseTrans,
	 * "softwareReleaseTrans is not found");
	 * 
	 * AccessKey accessKey = findAccessKey(softwareReleaseTrans);
	 * Assert.notNull(accessKey, "accessKey is null");
	 * 
	 * auditLog(method, softwareReleaseTrans.getApplicationId(),
	 * softwareReleaseTrans.getId(), accessKey.getId(), request);
	 * 
	 * getSoftwareReleaseService().start(softwareReleaseTrans.getId(),
	 * accessKey.getPri());
	 * 
	 * return StatusModel.OK; }
	 */

	@ApiOperation(value = "download software release file", response = StatusModel.class)
	@RequestMapping(path = "/{hid}/{token}/file", method = RequestMethod.GET)
	public void file(@PathVariable String hid, @PathVariable String token, HttpServletResponse response) {
		String method = "file";

		logDebug(method, "hid: %s, token: %s", hid, token);

		TempToken tempToken = tempTokenService.getTempTokenRepository().doFindByHid(token);
		Assert.notNull(tempToken, "tempToken is not found");

		String softwareReleaseTransId = tempToken.getProperties().get("softwareReleaseTransId");
		Assert.hasText(softwareReleaseTransId, "softwareReleaseTransId is not found");

		// lookup transaction
		SoftwareReleaseTrans softwareReleaseTrans = getSoftwareReleaseTransService().getSoftwareReleaseTransRepository()
				.doFindByHid(hid);
		Assert.notNull(softwareReleaseTrans, "softwareReleaseTrans is not found");
		Assert.isTrue(softwareReleaseTransId.equals(softwareReleaseTrans.getId()), "invalid token");
		Assert.isTrue(softwareReleaseTrans.getStatus() == SoftwareReleaseTrans.Status.RECEIVED,
				"softwareReleaseTrans must be in a RECEIVED state");

		AccessKey accessKey = findAccessKey(softwareReleaseTrans);
		Assert.notNull(accessKey, "accessKey is null");

		String who = accessKey.getPri();

		// lookup job
		SoftwareReleaseSchedule softwareReleaseSchedule = getSoftwareReleaseScheduleService()
				.getSoftwareReleaseScheduleRepository().findById(softwareReleaseTrans.getSoftwareReleaseScheduleId())
				.orElse(null);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is not found");
		Assert.isTrue(softwareReleaseSchedule.getStatus() == SoftwareReleaseSchedule.Status.INPROGRESS,
				"softwareReleaseSchedule must be in a INPROGRESS state");

		logDebug(method, "softwareReleaseSchedule: %s, softwareReleaseTransId: %s, tokenId: %s, who: %s",
				softwareReleaseSchedule.getId(), softwareReleaseTrans.getToSoftwareReleaseId(), token, who);

		// capture audit log for job
		softwareReleaseScheduleService.auditLog(softwareReleaseSchedule, softwareReleaseTrans, who,
				KronosAuditLog.SoftwareReleaseSchedule.SoftwareReleaseScheduleAssetFirmwareDownload);

		// capture audit log for transaction
		softwareReleaseTransService.auditLog(softwareReleaseTrans, who,
				KronosAuditLog.SoftwareReleaseTrans.SoftwareReleaseTransFirmwareDownload);

		boolean error = false;
		String message = "Unknown";
		if (tempToken.isExpired()
				|| Instant.now().isAfter(tempToken.getCreatedDate().plusSeconds(tempToken.getTimeToExpireSeconds()))) {

			// mark the transaction error, can not proceed without retrying to
			// produce a new temporary token
			error = true;
			message = "Unable to download firmware, temporary token has expired.";
			softwareReleaseTrans = getSoftwareReleaseService().failed(softwareReleaseTrans.getId(), message,
					accessKey.getPri());
		} else {

			// lookup softwareRelease using the toSoftwareReleaseId of the
			// softwareRelease
			SoftwareRelease softwareRelease = clientSoftwareReleaseApi
					.findById(softwareReleaseTrans.getToSoftwareReleaseId());
			Assert.notNull(softwareRelease, "toSoftwareRelease is not found");
			Assert.hasText(softwareRelease.getFileStoreId(), "fileStore does not exist");

			// lookup fileStore using the fileStoreId of the softwareRelease
			FileStore fileStore = clientFileStoreApi.findById(softwareRelease.getFileStoreId());
			Assert.notNull(fileStore, "fileStore is not found");

			// send the file to the requesting client
			MediaType contentType = StringUtils.isNotBlank(fileStore.getContentType())
					? MediaType.valueOf(fileStore.getContentType())
					: MediaType.APPLICATION_OCTET_STREAM;
			response.setContentType(contentType.toString());
			if (StringUtils.isNotBlank(fileStore.getName())) {
				response.setHeader("Content-Disposition", "attachment;filename=\"" + fileStore.getName().trim() + "\""
						+ (fileStore.getSize() > 0 ? ";size=" + fileStore.getSize() : ""));
			}
			if (fileStore.getSize() > 0) {
				response.setHeader("Content-Length", String.valueOf(fileStore.getSize()));
			}
			if (StringUtils.isNotBlank(fileStore.getMd5())) {
				response.setHeader("Content-MD5", fileStore.getMd5());
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				String transInfo = JsonUtils.toJson(softwareReleaseTrans);
				logInfo(method, "downloading file: %s, trans: %s", fileStore.getName(), transInfo);
				long size = clientFileStoreApi.readFile(fileStore.getId(), bos);
				logInfo(method, "downloaded file: %s, size: %d, bos size: %d, trans: %s", fileStore.getName(), size,
						bos.size(), transInfo);

				// long size = clientFileStoreApi.readFile(fileStore.getId(),
				// response.getOutputStream());
				// logDebug(method, "file id: %s, name: %s, size: %s", fileStore.getId(),
				// fileStore.getName(), size);

				if (size != fileStore.getSize()) {
					logWarn(method, "file size mismatched! expected=%d, actual=%d, trans: %s", fileStore.getSize(),
							size, transInfo);
				}

				logInfo(method, "sending file content to client, trans: %s", transInfo);
				StreamUtils.copy(bos.toByteArray(), response.getOutputStream());
				response.flushBuffer();
				logInfo(method, "sent file content to client successfully, trans: %s", transInfo);
			} catch (Exception e) {
				throw new AcsLogicalException("error reading or sending file", e);
			} finally {
				AcsUtils.close(bos);
			}
		}

		if (error)
			throw new AcsLogicalException(message);
	}

	private AccessKey findAccessKey(SoftwareReleaseTrans softwareReleaseTrans) {
		Assert.notNull(softwareReleaseTrans, "softwareReleaseTrans is null");

		AccessKey accessKey = null;

		switch (softwareReleaseTrans.getDeviceCategory()) {
		case DEVICE:
			Device device = getKronosCache().findDeviceById(softwareReleaseTrans.getObjectId());
			Assert.notNull(device, "device is null");
			accessKey = validateCanUpdateDevice(device.getHid());
			break;
		case GATEWAY:
			Gateway gateway = getKronosCache().findGatewayById(softwareReleaseTrans.getObjectId());
			Assert.notNull(gateway, "gateway is null");
			accessKey = validateCanWriteGateway(gateway.getHid());
			break;
		default:
			throw new AcsLogicalException(
					"Unsupported category! category=" + softwareReleaseTrans.getDeviceCategory().name());
		}

		return accessKey;
	}

	// private SoftwareReleaseTrans.Status
	// buildSoftwareReleaseTransStatus(SoftwareReleaseTransStatus model) {
	// if (model == null) {
	// return SoftwareReleaseTrans.Status.PENDING;
	// } else {
	// return SoftwareReleaseTrans.Status.valueOf(model.toString());
	// }
	// }

	private HidModel doUpgrade(SoftwareReleaseUpgradeModel model, AcnDeviceCategory deviceCategory, String method,
			HttpServletRequest request) {
		AccessKey accessKey = validateAccessKey(deviceCategory).apply(model.getObjectHid());
		Assert.notNull(accessKey, "accessKey is null");

		// convert hid to deviceId / gatewayId
		DocumentAbstract obj = findByHid(deviceCategory).apply(model.getObjectHid());
		Assert.notNull(obj, deviceCategory.name() + " is not found");
		String objectId = obj.getId();

		auditLog(method, accessKey.getApplicationId(), objectId, accessKey.getId(), request);

		// convert toSoftwareReleaseHid to softwareReleaseId (Rhea)
		String softwareReleaseId = findSoftwareReleaseId(model.getToSoftwareReleaseHid());

		// convert softwareReleaseScheduleHid to softwareReleaseScheduleId
		// (if provided)
		String softwareReleaseScheduleId = null;
		if (StringUtils.isNotBlank(model.getSoftwareReleaseScheduleHid())) {
			SoftwareReleaseSchedule softwareReleaseSchedule = softwareReleaseScheduleService
					.getSoftwareReleaseScheduleRepository().doFindByHid(model.getSoftwareReleaseScheduleHid());
			Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is not found");
			// Assert.isTrue(deviceCategory.getId().equals(softwareReleaseSchedule.getDeviceCategoryId()),
			// "deviceCategory does not match");
			Assert.isTrue((deviceCategory == softwareReleaseSchedule.getDeviceCategory()),
					"deviceCategory does not match");
			softwareReleaseScheduleId = softwareReleaseSchedule.getId();
		}

		SoftwareReleaseTrans trans = getSoftwareReleaseService().create(objectId, deviceCategory, softwareReleaseId,
				softwareReleaseScheduleId, accessKey.getPri());

		return new HidModel().withHid(trans.getHid()).withMessage("OK");
	}
}
