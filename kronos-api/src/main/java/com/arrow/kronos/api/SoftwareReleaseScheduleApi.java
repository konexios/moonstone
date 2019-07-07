package com.arrow.kronos.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.acn.client.model.AcnDeviceCategory;
import com.arrow.acn.client.model.CreateSoftwareReleaseScheduleModel;
import com.arrow.acn.client.model.SoftwareReleaseScheduleAutomationModel;
import com.arrow.acn.client.model.SoftwareReleaseScheduleModel;
import com.arrow.acn.client.model.SoftwareReleaseScheduleStatus;
import com.arrow.acn.client.model.SoftwareReleaseTransModel;
import com.arrow.acn.client.model.SoftwareReleaseTransStatus;
import com.arrow.acn.client.model.UpdateSoftwareReleaseScheduleModel;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.client.model.HidModel;
import com.arrow.acs.client.model.PagingResultModel;
import com.arrow.acs.client.model.StatusModel;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.api.model.SoftwareReleaseScheduleCancelModel;
import com.arrow.kronos.data.BaseDeviceAbstract;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.SoftwareReleaseSchedule;
import com.arrow.kronos.data.SoftwareReleaseTrans;
import com.arrow.kronos.repo.SoftwareReleaseScheduleSearchParams;
import com.arrow.kronos.repo.SoftwareReleaseTransSearchParams;
import com.arrow.kronos.util.ApiUtil;
import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.DocumentAbstract;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.arrow.rhea.data.SoftwareRelease;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/kronos/software/releases/schedules")
public class SoftwareReleaseScheduleApi extends SoftwareReleaseApiAbstract {

	private static final String FIRMWARE_UPDATE_REQUEST_VIA_API = "Firmware Update Request via API";
	// private static final String CANCELLATION_MESSAGE = "Cancelled via API";

	@Autowired
	private ApiUtil apiUtil;

	@ApiOperation(value = "create new software release schedule", response = HidModel.class)
	@RequestMapping(path = "", method = RequestMethod.POST)
	public HidModel create(@RequestBody(required = false) CreateSoftwareReleaseScheduleModel body,
			HttpServletRequest request) {
		String method = "create";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());

		AuditLog auditLog = auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);

		CreateSoftwareReleaseScheduleModel model = JsonUtils.fromJson(getApiPayload(),
				CreateSoftwareReleaseScheduleModel.class);

		SoftwareReleaseSchedule softwareReleaseSchedule = buildSoftwareReleaseSchedule(new SoftwareReleaseSchedule(),
				model);
		softwareReleaseSchedule.setApplicationId(accessKey.getApplicationId());

		softwareReleaseSchedule = getSoftwareReleaseScheduleService().create(softwareReleaseSchedule,
				accessKey.getPri());

		auditLog.setObjectId(softwareReleaseSchedule.getId());
		getAuditLogService().getAuditLogRepository().doSave(auditLog, accessKey.getId());

		return new HidModel().withHid(softwareReleaseSchedule.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "create and start new software release schedule", response = HidModel.class)
	@RequestMapping(path = "/start", method = RequestMethod.POST)
	public HidModel createAndStart(@RequestBody(required = false) SoftwareReleaseScheduleAutomationModel body,
			HttpServletRequest request) {
		String method = "createAndStart";

		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());

		AuditLog auditLog = auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);

		SoftwareReleaseScheduleAutomationModel model = JsonUtils.fromJson(getApiPayload(),
				SoftwareReleaseScheduleAutomationModel.class);
		Assert.notNull(model, "model is null");
		Assert.hasText(model.getUserHid(), "userHid is empty");
		User user = getCoreCacheService().findUserByHid(model.getUserHid());
		Assert.notNull(user, "user is not found");
		Assert.isTrue(user.getCompanyId().equals(accessKey.getCompanyId()), "invalid user");
		Assert.notNull(model.getDeviceCategory(), "deviceCategory is null");

		SoftwareReleaseSchedule schedule = buildSoftwareReleaseScheduleList(accessKey, model);
		schedule = getSoftwareReleaseScheduleService().create(schedule, user.getId());
		schedule = getSoftwareReleaseScheduleService().checkAndStart(schedule, user.getId());

		auditLog.setObjectId(schedule.getId());
		getAuditLogService().getAuditLogRepository().doSave(auditLog, accessKey.getId());

		return new HidModel().withHid(schedule.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "update existing software release schedule", response = HidModel.class)
	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel update(@PathVariable(name = "hid") String hid,
			@RequestBody(required = false) UpdateSoftwareReleaseScheduleModel body, HttpServletRequest request) {
		String method = "update";

		AccessKey accessKey = validateCanWriteApplication(getProductSystemName());
		UpdateSoftwareReleaseScheduleModel model = JsonUtils.fromJson(getApiPayload(),
				UpdateSoftwareReleaseScheduleModel.class);

		SoftwareReleaseSchedule softwareReleaseSchedule = getSoftwareReleaseScheduleService()
				.getSoftwareReleaseScheduleRepository().doFindByHid(hid);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is not found");

		auditLog(method, softwareReleaseSchedule.getApplicationId(), softwareReleaseSchedule.getId(), accessKey.getId(),
				request);

		softwareReleaseSchedule = buildSoftwareReleaseSchedule(softwareReleaseSchedule, model);
		softwareReleaseSchedule.setStatus(buildSoftwareReleaseScheduleStatus(model.getStatus()));

		softwareReleaseSchedule = getSoftwareReleaseScheduleService().update(softwareReleaseSchedule,
				accessKey.getPri());

		return new HidModel().withHid(softwareReleaseSchedule.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "find software release schedule by hid", response = SoftwareReleaseScheduleModel.class)
	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public SoftwareReleaseScheduleModel findByHid(@PathVariable(name = "hid") String hid) {
		AccessKey accessKey = validateCanReadApplication(getProductSystemName());
		SoftwareReleaseSchedule softwareReleaseSchedule = getSoftwareReleaseScheduleService()
				.getSoftwareReleaseScheduleRepository().doFindByHid(hid);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is not found");
		Assert.isTrue(accessKey.getApplicationId().equals(softwareReleaseSchedule.getApplicationId()),
				"application does not match");
		return buildSoftwareReleaseScheduleModel(softwareReleaseSchedule);
	}

	@ApiOperation(value = "find software release schedules")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public PagingResultModel<SoftwareReleaseScheduleModel> findAllBy(
			@RequestParam(name = "fromScheduledDate", required = false) String fromScheduledDate,
			@RequestParam(name = "toScheduledDate", required = false) String toScheduledDate,
			@RequestParam(name = "softwareReleaseHids", required = false) Set<String> softwareReleaseHids,
			@RequestParam(name = "deviceCategory", required = false) String deviceCategory,
			@RequestParam(name = "objectHids", required = false) Set<String> objectHids,
			@RequestParam(name = "statuses", required = false) Set<String> statuses,
			@RequestParam(name = "notifyOnStart", required = false) String notifyOnStart,
			@RequestParam(name = "notifyOnEnd", required = false) String notifyOnEnd,
			@RequestParam(name = "notifyOnSubmit", required = false) String notifyOnSubmit,
			@RequestParam(name = "onDemand", required = false) String onDemand,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "deviceTypeHids", required = false) Set<String> deviceTypeHids,
			@RequestParam(name = "hardwareVersionHids", required = false) Set<String> hardwareVersionHids,
			@RequestParam(name = "_page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "_size", required = false, defaultValue = "100") int size) {

		Assert.isTrue(page >= 0, "page must be positive");
		Assert.isTrue(size >= 0 && size <= KronosConstants.PageResult.MAX_SIZE,
				"size must be between 0 and " + KronosConstants.PageResult.MAX_SIZE);

		AccessKey accessKey = validateCanReadApplication(getProductSystemName());

		PagingResultModel<SoftwareReleaseScheduleModel> result = new PagingResultModel<>();
		result.setPage(page);
		PageRequest pageRequest = PageRequest.of(page, size);

		SoftwareReleaseScheduleSearchParams params = new SoftwareReleaseScheduleSearchParams();

		params.addApplicationIds(accessKey.getApplicationId());

		if (StringUtils.hasText(fromScheduledDate)) {
			params.setFromScheduledDate(Instant.parse(fromScheduledDate));
		}

		if (StringUtils.hasText(toScheduledDate)) {
			params.setToScheduledDate(Instant.parse(toScheduledDate));
		}

		if (softwareReleaseHids != null) {
			softwareReleaseHids.forEach(hid -> params.addSoftwareReleaseIds(findSoftwareReleaseId(hid)));
		}

		// if (StringUtils.hasText(deviceCategoryHid)) {
		if (StringUtils.hasText(deviceCategory)) {
			// DeviceCategory deviceCategory =
			// getClientCacheApi().findDeviceCategoryByHid(deviceCategoryHid);
			// Assert.notNull(deviceCategory, "deviceCategory is not found");
			AcnDeviceCategory acnDeviceCategory = AcnDeviceCategory.valueOf(deviceCategory);
			// params.addDeviceCategoryIds(deviceCategory.getId());
			params.setDeviceCategories(EnumSet.of(acnDeviceCategory));
			if (objectHids != null) {
				objectHids.stream()
						.forEach(hid -> params.addObjectIds(findObject(findByHid(acnDeviceCategory), hid).getId()));
			}
		} else {
			Assert.isTrue(objectHids == null || objectHids.isEmpty(), "deviceCategory is required");
		}

		if (statuses != null) {
			EnumSet<SoftwareReleaseSchedule.Status> scheduleStatuses = EnumSet
					.noneOf(SoftwareReleaseSchedule.Status.class);
			statuses.forEach(status -> scheduleStatuses.add(SoftwareReleaseSchedule.Status.valueOf(status.toString())));
			params.setStatuses(scheduleStatuses);
		}

		if (StringUtils.hasText(notifyOnStart)) {
			params.setNotifyOnStart(Boolean.parseBoolean(notifyOnStart));
		}

		if (StringUtils.hasText(notifyOnEnd)) {
			params.setNotifyOnEnd(Boolean.parseBoolean(notifyOnEnd));
		}

		if (StringUtils.hasText(notifyOnSubmit)) {
			params.setNotifyOnSubmit(Boolean.parseBoolean(notifyOnSubmit));
		}

		if (StringUtils.hasText(onDemand)) {
			params.setOnDemand(Boolean.parseBoolean(onDemand));
		}

		if (StringUtils.hasText(name)) {
			params.setName(name);
		}

		if (deviceTypeHids != null) {
			deviceTypeHids.forEach(hid -> params.addDeviceTypeIds(findDeviceTypeId(hid)));
		}

		if (hardwareVersionHids != null) {
			hardwareVersionHids.forEach(hid -> params.addHardwareVersionIds(findHardwareVersionId(hid)));
		}

		Page<SoftwareReleaseSchedule> schedules = getSoftwareReleaseScheduleService()
				.getSoftwareReleaseScheduleRepository().findSoftwareReleaseSchedules(pageRequest, params);

		List<SoftwareReleaseScheduleModel> data = schedules.getContent().stream()
				.map(schedule -> buildSoftwareReleaseScheduleModel(schedule))
				.collect(Collectors.toCollection(ArrayList::new));

		result.withTotalPages(schedules.getTotalPages()).withTotalSize(schedules.getTotalElements())
				.withSize(schedules.getNumberOfElements()).withData(data);
		return result;
	}

	@ApiOperation(value = "list all transactions for provided schedule object")
	@RequestMapping(path = "/{hid}/transactions", method = RequestMethod.GET)
	public PagingResultModel<SoftwareReleaseTransModel> listTransactions(
			@ApiParam(value = "software release schedule hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "sortField") @RequestParam(name = "sortField", required = false) String sortField,
			@ApiParam(value = "sortDirection") @RequestParam(name = "sortDirection", required = false) String sortDirection,
			@ApiParam(value = "page index") @RequestParam(name = "_page", required = false) Integer page,
			@ApiParam(value = "items per page") @RequestParam(name = "_size", required = false) Integer size) {

		AccessKey accessKey = validateCanReadApplication(getProductSystemName());

		SoftwareReleaseSchedule softwareReleaseSchedule = getSoftwareReleaseScheduleService()
				.getSoftwareReleaseScheduleRepository().doFindByHid(hid);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is not found");
		Assert.isTrue(accessKey.getApplicationId().equals(softwareReleaseSchedule.getApplicationId()),
				"application does not match");

		size = apiUtil.validateSize(size);
		page = apiUtil.validatePage(page);
		PageRequest pageRequest = apiUtil.buildPageRequest(page, size, sortField, sortDirection);

		SoftwareReleaseTransSearchParams params = new SoftwareReleaseTransSearchParams();
		params.addApplicationIds(accessKey.getApplicationId());
		params.addSoftwareReleaseScheduleIds(softwareReleaseSchedule.getId());
		Page<SoftwareReleaseTrans> transactions = getSoftwareReleaseTransService().getSoftwareReleaseTransRepository()
				.findSoftwareReleaseTrans(pageRequest, params);
		List<SoftwareReleaseTransModel> data = transactions.getContent().stream()
				.map(this::buildSoftwareReleaseTransModel).collect(Collectors.toCollection(ArrayList::new));

		PagingResultModel<SoftwareReleaseTransModel> result = new PagingResultModel<>();
		result.withPage(page).withTotalPages(transactions.getTotalPages())
				.withTotalSize(transactions.getTotalElements()).withSize(transactions.getNumberOfElements())
				.withData(data);
		return result;
	}

	@ApiOperation(value = "cancel software release schedule", hidden = true)
	@RequestMapping(path = "/{hid}/cancel", method = RequestMethod.PUT)
	public StatusModel cancel(@PathVariable(name = "hid") String hid, HttpServletRequest request) {
		String method = "cancel";

		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());

		SoftwareReleaseSchedule softwareReleaseSchedule = getSoftwareReleaseScheduleService()
				.getSoftwareReleaseScheduleRepository().doFindByHid(hid);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is not found");

		auditLog(method, accessKey.getApplicationId(), softwareReleaseSchedule.getId(), accessKey.getId(), request);

		Assert.isTrue(accessKey.getApplicationId().equals(softwareReleaseSchedule.getApplicationId()),
				"application does not match");
		if (!canWrite(softwareReleaseSchedule, accessKey)) {
			throw new NotAuthorizedException();
		}

		getSoftwareReleaseService().cancel(softwareReleaseSchedule, true, accessKey.getId());

		return StatusModel.OK;
	}

	@ApiOperation(value = "cancel software release schedule for specific asset hids", hidden = true)
	@RequestMapping(path = "/cancel", method = RequestMethod.PUT)
	public StatusModel cancel(@RequestBody(required = false) SoftwareReleaseScheduleCancelModel body,
			HttpServletRequest request) {
		String method = "cancel";

		AccessKey accessKey = getValidatedAccessKey(getProductSystemName());

		auditLog(method, accessKey.getApplicationId(), null, accessKey.getId(), request);

		SoftwareReleaseScheduleCancelModel model = JsonUtils.fromJson(getApiPayload(),
				SoftwareReleaseScheduleCancelModel.class);
		Assert.notNull(model, "model is null");
		Assert.notEmpty(model.getObjectHids(), "objectHids is empty");

		// find assets by objectHids
		List<BaseDeviceAbstract> assets = model.getObjectHids().stream()
				.map(objectHid -> (BaseDeviceAbstract) findObject(findByHid(model.getDeviceCategory()), objectHid))
				.collect(Collectors.toList());

		// find active jobs
		Map<SoftwareReleaseSchedule, List<String>> softwareReleaseSchedules = new TreeMap<>(
				(x, y) -> x.getId().compareTo(y.getId()));
		assets.forEach(asset -> {
			List<SoftwareReleaseSchedule> schedules = getSoftwareReleaseService().findActiveSoftwareReleaseSchedules(
					accessKey.getApplicationId(), asset.getId(), model.getDeviceCategory());
			if (!schedules.stream().allMatch(softwareReleaseSchedule -> canWrite(softwareReleaseSchedule, accessKey))) {
				throw new NotAuthorizedException();
			}
			if (softwareReleaseSchedules.size() > 1) {
				logWarn(method, "found multiple active jobs!! %s uid: %s, hid: %s", model.getDeviceCategory(),
						asset.getUid(), asset.getHid());
			}
			schedules.forEach(schedule -> {
				softwareReleaseSchedules.putIfAbsent(schedule, new ArrayList<>());
				softwareReleaseSchedules.get(schedule).add(asset.getId());
			});
		});

		String message = "";
		if (softwareReleaseSchedules.isEmpty()) {
			message = "No active jobs found";
		} else {
			// cancel active jobs
			softwareReleaseSchedules.forEach((softwareReleaseSchedule, objectIds) -> getSoftwareReleaseService()
					.cancel(softwareReleaseSchedule, objectIds, true, accessKey.getId()));
		}
		return new StatusModel().withStatus("OK").withMessage(message);
	}

	@ApiOperation(value = "check and complete existing software release schedule", hidden = true)
	@RequestMapping(path = "/{hid}/complete", method = RequestMethod.PUT)
	public StatusModel checkAndComplete(@PathVariable(name = "hid") String hid, HttpServletRequest request) {
		// String method = "checkAndComplete";

		AccessKey accessKey = validateRootAccess();

		SoftwareReleaseSchedule softwareReleaseSchedule = getSoftwareReleaseScheduleService()
				.getSoftwareReleaseScheduleRepository().doFindByHid(hid);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is not found");

		// NOT NEEDED FOR THIS METHOD, CRON RUNS EVERY MINUTE
		// auditLog(method, softwareReleaseSchedule.getApplicationId(),
		// softwareReleaseSchedule.getId(), accessKey.getId(),
		// request);

		softwareReleaseSchedule = getSoftwareReleaseScheduleService().checkAndComplete(softwareReleaseSchedule,
				accessKey.getPri());

		return new StatusModel().withStatus("OK").withMessage(softwareReleaseSchedule.getStatus().toString());
	}

	@ApiOperation(value = "check and start existing software release schedule", hidden = true)
	@RequestMapping(path = "/{hid}/start", method = RequestMethod.PUT)
	public StatusModel checkAndStart(@PathVariable(name = "hid") String hid, HttpServletRequest request) {
		String method = "checkAndStart";

		AccessKey accessKey = validateRootAccess();

		SoftwareReleaseSchedule softwareReleaseSchedule = getSoftwareReleaseScheduleService()
				.getSoftwareReleaseScheduleRepository().doFindByHid(hid);
		Assert.notNull(softwareReleaseSchedule, "softwareReleaseSchedule is not found");

		auditLog(method, softwareReleaseSchedule.getApplicationId(), softwareReleaseSchedule.getId(), accessKey.getId(),
				request);

		softwareReleaseSchedule = getSoftwareReleaseScheduleService().checkAndStart(softwareReleaseSchedule,
				accessKey.getId());

		return new StatusModel().withStatus("OK").withMessage(softwareReleaseSchedule.getStatus().toString());
	}

	private SoftwareReleaseSchedule.Status buildSoftwareReleaseScheduleStatus(SoftwareReleaseScheduleStatus model) {
		if (model == null) {
			return SoftwareReleaseSchedule.Status.SCHEDULED;
		} else {
			return SoftwareReleaseSchedule.Status.valueOf(model.toString());
		}
	}

	private SoftwareReleaseScheduleStatus buildSoftwareReleaseScheduleStatusModel(
			SoftwareReleaseSchedule.Status status) {
		Assert.notNull(status, "status is null");
		return SoftwareReleaseScheduleStatus.valueOf(status.toString());
	}

	private SoftwareReleaseScheduleModel buildSoftwareReleaseScheduleModel(SoftwareReleaseSchedule schedule) {
		String method = "buildSoftwareReleaseScheduleModel";
		Assert.notNull(schedule, "softwareReleaseSchedule is null");
		SoftwareReleaseScheduleModel result = buildModel(new SoftwareReleaseScheduleModel(), schedule);

		Application application = getCoreCacheService().findApplicationById(schedule.getApplicationId());
		Assert.notNull(application, "application is not found");
		result.setApplicationHid(application.getHid());

		if (schedule.getDeviceCategory() != null) {
			result.setDeviceCategory(schedule.getDeviceCategory());
			schedule.getObjectIds().forEach(id -> {
				try {
					result.withObjectHid(findObject(findById(schedule.getDeviceCategory()), id).getHid());
				} catch (IllegalArgumentException ex) {
					logError(method, "object with id %s is not found", id);
				}
			});
		}
		result.setComments(schedule.getComments());
		result.setHid(schedule.getHid());
		result.setNotifyEmails(schedule.getNotifyEmails());
		result.setNotifyOnEnd(schedule.isNotifyOnEnd());
		result.setNotifyOnStart(schedule.isNotifyOnStart());
		if (schedule.getScheduledDate() != null) {
			result.setScheduledDate(schedule.getScheduledDate().toString());
		}
		SoftwareRelease softwareRelease = getClientCacheApi().findSoftwareReleaseById(schedule.getSoftwareReleaseId());
		if (softwareRelease != null) {
			result.setSoftwareReleaseHid(softwareRelease.getHid());
		}

		result.setStatus(buildSoftwareReleaseScheduleStatusModel(schedule.getStatus()));
		result.setName(schedule.getName());
		result.setOnDemand(schedule.getOnDemand());
		result.setNotifyOnSubmit(schedule.getNotifyOnSubmit());

		if (schedule.getDeviceTypeId() != null) {
			DeviceType deviceType = getKronosCache().findDeviceTypeById(schedule.getDeviceTypeId());
			Assert.notNull(deviceType, "deviceType is not found");
			result.setDeviceTypeHid(deviceType.getHid());
		}

		if (schedule.getHardwareVersionId() != null) {
			com.arrow.rhea.data.DeviceType hwVersion = getClientCacheApi()
					.findDeviceTypeById(schedule.getHardwareVersionId());
			if (hwVersion != null) {
				result.setHardwareVersionHid(hwVersion.getHid());
			}
		}

		result.setTimeToExpireSeconds(schedule.getTimeToExpireSeconds());

		return result;
	}

	private SoftwareReleaseSchedule buildSoftwareReleaseSchedule(SoftwareReleaseSchedule schedule,
			CreateSoftwareReleaseScheduleModel model) {
		Assert.notNull(model.getDeviceCategory(), "deviceCategory is null");

		if (schedule == null)
			schedule = new SoftwareReleaseSchedule();
		schedule.setDeviceCategory(model.getDeviceCategory());

		schedule.setComments(model.getComments());
		schedule.setNotifyEmails(model.getNotifyEmails());
		schedule.setNotifyOnEnd(model.isNotifyOnEnd());
		schedule.setNotifyOnStart(model.isNotifyOnStart());
		schedule.setNotifyOnSubmit(model.isNotifyOnSubmit());
		schedule.setOnDemand(model.isOnDemand());

		Assert.notEmpty(model.getObjectHids(), "objectHids is null");
		schedule.setObjectIds(
				model.getObjectHids().stream().map(hid -> findObject(findByHid(model.getDeviceCategory()), hid).getId())
						.collect(Collectors.toCollection(ArrayList::new)));

		if (model.getScheduledDate() != null) {
			schedule.setScheduledDate(model.getScheduledDate());
		}

		Assert.hasText(model.getSoftwareReleaseHid(), "softwareReleaseHid is empty");
		schedule.setSoftwareReleaseId(findSoftwareReleaseId(model.getSoftwareReleaseHid()));

		schedule.setName(model.getName());
		schedule.setDeviceTypeId(findDeviceTypeId(model.getDeviceTypeHid()));
		schedule.setHardwareVersionId(findHardwareVersionId(model.getHardwareVersionHid()));
		schedule.setTimeToExpireSeconds(model.getTimeToExpireSeconds());

		return schedule;
	}

	private SoftwareReleaseSchedule buildSoftwareReleaseScheduleList(AccessKey accessKey,
			SoftwareReleaseScheduleAutomationModel model) {
		Assert.notNull(model, "model is null");
		Assert.notEmpty(model.getObjectHids(), "objectHids is empty");
		Assert.hasText(model.getSoftwareReleaseHid(), "softwareReleaseHid is empty");

		SoftwareRelease softwareRelease = getClientCacheApi().findSoftwareReleaseByHid(model.getSoftwareReleaseHid());
		Assert.notNull(softwareRelease, "software release is not found");

		boolean canWriteApplication = accessKey.canWrite(accessKey.getRefApplication());
		Map<String, List<String>> objectsByDeviceType = model.getObjectHids().stream().map(hid -> {
			BaseDeviceAbstract asset = (BaseDeviceAbstract) findObject(findByHid(model.getDeviceCategory()), hid);
			logInfo("buildSoftwareReleaseSchedule", "%s %s", asset.getSoftwareReleaseId(),
					softwareRelease.getUpgradeableFromIds());
			Assert.isTrue(asset.isEnabled(), "asset " + hid + " is disabled");
			Assert.isTrue(asset.getApplicationId().equals(accessKey.getApplicationId()), "application does not match");
			Assert.isTrue(
					softwareRelease.getUpgradeableFromIds() != null && asset.getSoftwareReleaseId() != null
							&& softwareRelease.getUpgradeableFromIds().contains(asset.getSoftwareReleaseId()),
					"asset hid: " + hid + " is not upgradeable to softwareReleaseHid: " + softwareRelease.getHid());
			if (!canWriteApplication && !canWrite(model.getDeviceCategory(), accessKey, asset)) {
				throw new NotAuthorizedException();
			}
			return asset;
		}).collect(Collectors.groupingBy(BaseDeviceAbstract::getDeviceTypeId,
				Collectors.mapping(BaseDeviceAbstract::getId, Collectors.toList())));
		Assert.isTrue(objectsByDeviceType.keySet().size() == 1, "deviceTypes don't match");

		String deviceTypeId = objectsByDeviceType.keySet().iterator().next();
		SoftwareReleaseSchedule schedule = new SoftwareReleaseSchedule();
		// deviceType
		DeviceType deviceType = getKronosCache().findDeviceTypeById(deviceTypeId);
		Assert.notNull(deviceType, "deviceType is not found");
		Assert.isTrue(deviceType.isEnabled(), "deviceType is disabled");
		schedule.setDeviceTypeId(deviceTypeId);
		// softwareReleaseId
		Assert.isTrue(softwareRelease.getDeviceTypeIds().contains(deviceType.getRheaDeviceTypeId()),
				"softwareRelease deviceType does not match");
		schedule.setSoftwareReleaseId(softwareRelease.getId());
		// applicationId
		schedule.setApplicationId(accessKey.getApplicationId());
		// objectIds
		schedule.setObjectIds(objectsByDeviceType.get(deviceTypeId));
		// deviceCategory
		schedule.setDeviceCategory(model.getDeviceCategory());
		// hardwareVersionId
		Assert.hasText(deviceType.getRheaDeviceTypeId(), "rheaDeviceTypeId is empty");
		com.arrow.rhea.data.DeviceType hardwareVersion = getSoftwareReleaseScheduleService().getRheaClientCacheApi()
				.findDeviceTypeById(deviceType.getRheaDeviceTypeId());
		Assert.notNull(hardwareVersion, "hardwareVersion is not found");
		schedule.setHardwareVersionId(hardwareVersion.getId());
		// name
		String jobName = FIRMWARE_UPDATE_REQUEST_VIA_API;
		if (!StringUtils.isEmpty(model.getJobName()))
			jobName = model.getJobName();
		schedule.setName(jobName);
		// notify
		schedule.setNotifyOnEnd(false);
		schedule.setNotifyOnStart(false);
		schedule.setNotifyOnSubmit(false);
		// onDemand
		schedule.setOnDemand(true);
		// expiration
		schedule.setTimeToExpireSeconds(model.getTimeToExpireSeconds());

		return schedule;
	}

	public SoftwareReleaseTransModel buildSoftwareReleaseTransModel(SoftwareReleaseTrans trans) {
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

	private String findDeviceTypeId(String hid) {
		Assert.hasText(hid, "deviceTypeHid is empty");
		DeviceType deviceType = getKronosCache().findDeviceTypeByHid(hid);
		Assert.notNull(deviceType, "deviceType is not found");
		return deviceType.getId();
	}

	private String findHardwareVersionId(String hid) {
		Assert.hasText(hid, "hardwareVersionHid is empty");
		com.arrow.rhea.data.DeviceType hwVersion = getSoftwareReleaseScheduleService().getRheaClientCacheApi()
				.findDeviceTypeByHid(hid);
		Assert.notNull(hwVersion, "hwVersion is not found");
		return hwVersion.getId();
	}
}
