package com.arrow.kronos.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.KronosConstants;
import com.arrow.kronos.data.ConfigBackup;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceEvent;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.IbmDevice;
import com.arrow.kronos.data.IoTProvider;
import com.arrow.kronos.data.KronosApplication;
import com.arrow.kronos.data.KronosUser;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.data.TestResult;
import com.arrow.kronos.repo.ConfigBackupSearchParams;
import com.arrow.kronos.repo.DeviceEventSearchParams;
import com.arrow.kronos.repo.DeviceSearchParams;
import com.arrow.kronos.repo.TestResultSearchParams;
import com.arrow.kronos.service.ConfigBackupService;
import com.arrow.kronos.service.DeviceEventService;
import com.arrow.kronos.service.IbmIntegrationService;
import com.arrow.kronos.service.KronosApplicationService;
import com.arrow.kronos.service.KronosUserService;
import com.arrow.kronos.service.NodeService;
import com.arrow.kronos.service.TestResultService;
import com.arrow.kronos.util.ApiUtil;
import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AuditLog;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.location.LastLocation;
import com.arrow.pegasus.data.location.LocationObjectType;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserStatus;
import com.arrow.pegasus.service.LastLocationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import moonstone.acn.client.model.AcnDeviceCategory;
import moonstone.acn.client.model.AuditLogModel;
import moonstone.acn.client.model.AvailableFirmwareModel;
import moonstone.acn.client.model.ConfigBackupModel;
import moonstone.acn.client.model.CreateConfigBackupModel;
import moonstone.acn.client.model.DeviceEventModel;
import moonstone.acn.client.model.DeviceModel;
import moonstone.acn.client.model.DeviceRegistrationModel;
import moonstone.acn.client.model.LastLocationModel;
import moonstone.acn.client.model.LastLocationRegistrationModel;
import moonstone.acn.client.model.TestResultModel;
import moonstone.acs.AcsLogicalException;
import moonstone.acs.JsonUtils;
import moonstone.acs.client.model.ExternalHidModel;
import moonstone.acs.client.model.HidModel;
import moonstone.acs.client.model.PagingResultModel;
import moonstone.acs.client.model.StatusModel;

@RestController
@RequestMapping("/api/v1/kronos/devices")
public class DeviceApi extends DeviceApiAbstract {

	@Autowired
	private DeviceEventService deviceEventService;
	@Autowired
	private IbmIntegrationService ibmIntService;
	@Autowired
	private KronosApplicationService appService;
	@Autowired
	private KronosUserService kronosUserService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private ApiUtil apiUtil;
	@Autowired
	private LastLocationService lastLocationService;
	@Autowired
	private TestResultService testResultService;
	@Autowired
	private ConfigBackupService configBackupService;

	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public DeviceModel findByHid(@PathVariable String hid) {
		validateCanReadDevice(hid);
		return buildDeviceModel(getKronosCache().findDeviceByHid(hid));
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public PagingResultModel<DeviceModel> findAllBy(@RequestParam(name = "userHid", required = false) String userHid,
			@RequestParam(name = "uid", required = false) String uid,
			@RequestParam(name = "type", required = false) String deviceTypeName,
			@RequestParam(name = "gatewayHid", required = false) String gatewayHid,
			@RequestParam(name = "nodeHids", required = false) String[] nodeHids,
			@RequestParam(name = "createdBefore", required = false) String createdBefore,
			@RequestParam(name = "createdAfter", required = false) String createdAfter,
			@RequestParam(name = "updatedBefore", required = false) String updatedBefore,
			@RequestParam(name = "updatedAfter", required = false) String updatedAfter,
			@RequestParam(name = "enabled", required = false) String enabled,
			@RequestParam(name = "softwareNames", required = false) String[] softwareNames,
			@RequestParam(name = "softwareVersions", required = false) String[] softwareVersions,
			@RequestParam(name = "_page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "_size", required = false, defaultValue = "100") int size) {

		// validation
		Assert.isTrue(page >= 0, "page must be positive");
		Assert.isTrue(size >= 0 && size <= KronosConstants.PageResult.MAX_SIZE,
				"size must be between 0 and " + KronosConstants.PageResult.MAX_SIZE);

		PagingResultModel<DeviceModel> result = new PagingResultModel<>();
		result.setPage(page);
		PageRequest pageRequest = PageRequest.of(page, size);
		DeviceSearchParams params = new DeviceSearchParams();

		AccessKey accessKey = validateCanReadApplication(ProductSystemNames.KRONOS);
		params.addApplicationIds(accessKey.getApplicationId());

		if (StringUtils.isNotEmpty(uid)) {
			params.addUids(uid);
		}
		if (StringUtils.isNotEmpty(userHid)) {
			User user = getCoreCacheService().findUserByHid(userHid);
			if (user == null)
				return result;
			params.addUserIds(user.getId());
		}
		if (StringUtils.isNotEmpty(deviceTypeName)) {
			DeviceType type = getKronosCache().findDeviceTypeByName(accessKey.getApplicationId(), deviceTypeName);
			if (type == null)
				return result;
			params.addDeviceTypeIds(type.getId());
		}
		if (StringUtils.isNotEmpty(gatewayHid)) {
			Gateway gateway = getKronosCache().findGatewayByHid(gatewayHid);
			if (gateway == null)
				return result;
			params.addGatewayIds(gateway.getId());
		}
		if (nodeHids != null) {
			Arrays.asList(nodeHids).stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet()).stream()
					.map(nodeHid -> getKronosCache().findNodeByHid(nodeHid)).filter(node -> node != null)
					.map(Node::getId).forEach(params::addNodeIds);
			if (params.getNodeIds() == null || params.getNodeIds().size() == 0) {
				return result;
			}
		}
		if (StringUtils.isNotEmpty(enabled))
			params.setEnabled(enabled.equalsIgnoreCase(Boolean.TRUE.toString()));

		if (StringUtils.isNotEmpty(createdBefore)) {
			try {
				params.setCreatedBefore(Instant.parse(createdBefore));
			} catch (Exception e) {
				throw new AcsLogicalException("field createdBefore has invalid datetime format");
			}
		}

		if (StringUtils.isNotEmpty(createdAfter)) {
			try {
				params.setCreatedAfter(Instant.parse(createdAfter));
			} catch (Exception e) {
				throw new AcsLogicalException("field createdAfter has invalid datetime format");
			}
		}

		if (StringUtils.isNotEmpty(updatedBefore)) {
			try {
				params.setUpdatedBefore(Instant.parse(updatedBefore));
			} catch (Exception e) {
				throw new AcsLogicalException("field updatedBefore has invalid datetime format");
			}
		}

		if (StringUtils.isNotEmpty(updatedAfter)) {
			try {
				params.setUpdatedAfter(Instant.parse(updatedAfter));
			} catch (Exception e) {
				throw new AcsLogicalException("field updatedAfter has invalid datetime format");
			}
		}

		if (softwareNames != null) {
			Arrays.stream(softwareNames).filter(StringUtils::isNoneBlank).forEach(params::addSoftwareNames);
		}

		if (softwareVersions != null) {
			Arrays.stream(softwareVersions).filter(StringUtils::isNotBlank).forEach(params::addSoftwareVersions);
		}

		Page<Device> devices = getDeviceService().getDeviceRepository().doFindDevices(pageRequest, params);
		List<DeviceModel> data = new ArrayList<>();
		devices.forEach(device -> {
			data.add(buildDeviceModel(device));
		});
		result.setData(data);
		result.setTotalPages(devices.getTotalPages());
		result.setTotalSize(devices.getTotalElements());
		result.setSize(devices.getNumberOfElements());

		return result;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public ExternalHidModel createOrUpdate(
			@ApiParam(value = "device registration model", required = true) @RequestBody(required = false) DeviceRegistrationModel body,
			HttpServletRequest request) {
		String method = "createOrUpdate";

		AccessKey accessKey = getValidatedAccessKey(ProductSystemNames.KRONOS);
		Application application = accessKey.getRefApplication();

		DeviceRegistrationModel model = JsonUtils.fromJson(getApiPayload(), DeviceRegistrationModel.class);
		model.trim();

		// default type Unknown if not specified
		if (StringUtils.isEmpty(model.getType())) {
			model.setType(KronosConstants.Device.DEFAULT_TYPE);
		}

		Assert.hasText(model.getUid(), "uid is required");

		// lookup user
		User user = null;
		if (StringUtils.isNotEmpty(model.getUserHid())) {
			user = getCoreCacheService().findUserByHid(model.getUserHid());
			if (user == null) {
				throw new AcsLogicalException("invalid user");
			}
			if (user.getStatus() != UserStatus.Active) {
				throw new AcsLogicalException("user not active");
			}
		}

		// lookup gateway
		Gateway gateway = null;
		if (StringUtils.isNotEmpty(model.getGatewayHid())) {
			gateway = getKronosCache().findGatewayByHid(model.getGatewayHid());
			checkEnabled(gateway, "gateway");
			if (!application.getId().equals(gateway.getApplicationId())) {
				throw new AcsLogicalException("applicationId mismatched!");
			}
		}

		// lookup node
		Node node = null;
		if (StringUtils.isNotEmpty(model.getNodeHid())) {
			node = getKronosCache().findNodeByHid(model.getNodeHid());
			if (node == null) {
				throw new AcsLogicalException("invalid node");
			}
			if (!application.getId().equals(node.getApplicationId())) {
				throw new AcsLogicalException("applicationId mismatched!");
			}
		}

		if (!accessKey.canWrite(application) && !accessKey.canWrite(gateway)) {
			throw new NotAuthorizedException();
		}

		Device device = getDeviceService().populate(getDeviceService().getDeviceRepository()
				.findByApplicationIdAndUid(accessKey.getApplicationId(), model.getUid()));
		if (device != null) {
			boolean updated = false;

			auditLog(method, application.getId(), device.getId(), accessKey.getId(), request);
			AuditLogBuilder builder = AuditLogBuilder.create().type(KronosAuditLog.Device.UpdateDevice)
					.applicationId(accessKey.getApplicationId()).productName(ProductSystemNames.KRONOS)
					.objectId(device.getId()).by(accessKey.getId());

			// check and update user
			if (user != null && !StringUtils.equals(device.getUserId(), user.getId())) {
				logInfo(method, "updated userId for uid: %s, %s --> %s", model.getUid(), device.getUserId(),
						user.getId());
				device.setUserId(user.getId());
				updated = true;
				builder.parameter("userId", user.getId());
			}

			if (!StringUtils.equalsIgnoreCase(device.getRefDeviceType().getName(), model.getType())) {
				logInfo(method, "updated deviceTypeId for uid: %s, %s --> %s", model.getUid(),
						device.getRefDeviceType().getName(), model.getType());
				DeviceType type = checkCreateNewDeviceType(model, accessKey);
				device.setDeviceTypeId(type.getId());
				device.setRefDeviceType(type);
				updated = true;
				builder.parameter("deviceTypeId", type.getId());
			}

			// check and update gateway
			if (gateway != null && !StringUtils.equals(device.getGatewayId(), gateway.getId())) {
				logInfo(method, "updated gatewayId for uid: %s, %s --> %s", model.getUid(), device.getGatewayId(),
						gateway.getId());
				device.setGatewayId(gateway.getId());
				updated = true;
				builder.parameter("gatewayId", gateway.getId());
			}

			// check and update node
			if (node != null && !StringUtils.equals(device.getNodeId(), node.getId())) {
				logInfo(method, "updated nodeId for uid: %s, %s --> %s", model.getUid(), device.getNodeId(),
						node.getId());
				device.setNodeId(node.getId());
				updated = true;
				builder.parameter("nodeId", node.getId());
			}

			// check and update info
			Map<String, String> info = model.getInfo();
			if (info != null && info.size() > 0) {
				for (String name : info.keySet()) {
					String value = StringUtils.trimToEmpty(info.get(name));
					Map<String, String> existingInfo = device.getInfo();
					String oldValue = StringUtils.trimToEmpty(existingInfo.get(name));
					if (!value.equals(oldValue)) {
						existingInfo.put(name, value);
						updated = true;
					}
				}
			}

			// check and update properties
			Map<String, String> newProperties = model.getProperties();
			if (newProperties != null && newProperties.size() > 0) {
				for (String name : newProperties.keySet()) {
					String value = StringUtils.trimToEmpty(newProperties.get(name));
					Map<String, String> existingProperties = device.getProperties();
					String oldValue = StringUtils.trimToEmpty(existingProperties.get(name));
					if (!value.equals(oldValue)) {
						existingProperties.put(name, value);
						updated = true;
					}
				}
			}

			if (model.isEnabled() != device.isEnabled()) {
				device.setEnabled(model.isEnabled());
				updated = true;
			}

			boolean softwareReleaseUpdated = false;

			if (StringUtils.isNotEmpty(model.getSoftwareName())
					&& !StringUtils.equals(device.getSoftwareName(), model.getSoftwareName())) {
				device.setSoftwareName(model.getSoftwareName());
				updated = true;
				softwareReleaseUpdated = true;
			}

			if (StringUtils.isNotEmpty(model.getSoftwareVersion())
					&& !StringUtils.equals(device.getSoftwareVersion(), model.getSoftwareVersion())) {
				device.setSoftwareVersion(model.getSoftwareVersion());
				updated = true;
				softwareReleaseUpdated = true;
			}

			if (softwareReleaseUpdated) {
				String rheaSoftwareReleaseId = getSoftwareReleaseService().findRheaSoftwareReleaseId(
						device.getDeviceTypeId(), device.getSoftwareName(), device.getSoftwareVersion());
				device.setSoftwareReleaseId(rheaSoftwareReleaseId);
			}

			String message = "device is already registered";
			if (updated) {
				getDeviceService().update(device, accessKey.getId());
				getAuditLogService().save(builder);
				message += ", device information has been updated";
			}

			processCloudPlatformIntegration(device);

			return (ExternalHidModel) new ExternalHidModel().withExternalId(device.getExternalId())
					.withHid(device.getHid()).withMessage(message);
		} else {
			Assert.notNull(gateway, "gateway is null");
			// check and create new device type if needed
			DeviceType type = checkCreateNewDeviceType(model, accessKey);

			device = new Device();
			device.setApplicationId(accessKey.getApplicationId());
			device.setEnabled(true);
			device.setDeviceTypeId(type.getId());
			device.setRefDeviceType(type);
			device.setName(model.getName());
			device.setUid(model.getUid());
			if (user != null) {
				device.setUserId(user.getId());
			}
			device.setGatewayId(gateway.getId());
			if (node != null) {
				device.setNodeId(node.getId());
			}

			device.setInfo(model.getInfo());
			device.setProperties(model.getProperties());
			device.setSoftwareName(model.getSoftwareName());
			device.setSoftwareVersion(model.getSoftwareVersion());

			String rheaSoftwareReleaseId = getSoftwareReleaseService().findRheaSoftwareReleaseId(type.getId(),
					model.getSoftwareName(), model.getSoftwareVersion());
			device.setSoftwareReleaseId(rheaSoftwareReleaseId);

			device = getDeviceService().create(device, accessKey.getId());

			logInfo(method, "created new device, type: %s, uid: %s", model.getType(), model.getUid());
			auditLog(method, application.getId(), device.getId(), accessKey.getId(), request);

			processCloudPlatformIntegration(device);

			return (ExternalHidModel) new ExternalHidModel().withExternalId(device.getExternalId())
					.withHid(device.getHid()).withMessage("device was registered successfully");
		}
	}

	@ApiOperation(value = "update existing device")
	@RequestMapping(path = "/{hid}", method = RequestMethod.PUT)
	public HidModel update(@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "device model", required = true) @RequestBody(required = false) DeviceRegistrationModel body,
			HttpServletRequest request) {
		String method = "update";

		AccessKey accessKey = validateCanUpdateDevice(hid);

		DeviceRegistrationModel model = JsonUtils.fromJson(getApiPayload(), DeviceRegistrationModel.class);
		Assert.notNull(model, "DeviceRegistrationModel is null");
		model.trim();

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is not found");

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		// device type
		if (StringUtils.isNotEmpty(model.getType())) {
			DeviceType deviceType = getKronosCache().findDeviceTypeByName(accessKey.getApplicationId(),
					model.getType());
			Assert.notNull(deviceType, "device type is not found");
			device.setDeviceTypeId(deviceType.getId());
		}

		// uid
		if (StringUtils.isNotEmpty(model.getUid())) {
			Device d = getDeviceService().getDeviceRepository().findByApplicationIdAndUid(accessKey.getApplicationId(),
					model.getUid());
			Assert.isTrue(d == null || d.getId().equals(device.getId()), "duplicate uid");
			device.setUid(model.getUid());
		}

		// name
		if (StringUtils.isNotEmpty(model.getName())) {
			device.setName(model.getName());
		}

		// userId
		if (StringUtils.isNotEmpty(model.getUserHid())) {
			User user = getCoreCacheService().findUserByHid(model.getUserHid());
			Assert.notNull(user, "user is not found");
			device.setUserId(user.getId());
		}

		// gatewayId
		if (StringUtils.isNotEmpty(model.getGatewayHid())) {
			Gateway gateway = getKronosCache().findGatewayByHid(model.getGatewayHid());
			Assert.notNull(gateway, "gateway is not found");
			Assert.isTrue(accessKey.getApplicationId().equals(gateway.getApplicationId()), "gateway does not match");
			device.setGatewayId(gateway.getId());
		}

		// nodeId
		if (StringUtils.isNotEmpty(model.getNodeHid())) {
			Node node = nodeService.getNodeRepository().doFindByHid(model.getNodeHid());
			Assert.notNull(node, "node is not found");
			Assert.isTrue(accessKey.getApplicationId().equals(node.getApplicationId()), "node does not match");
			device.setNodeId(node.getId());
		}

		// enabled
		device.setEnabled(model.isEnabled());

		// info
		if (model.getInfo() != null && model.getInfo().size() > 0) {
			device.setInfo(model.getInfo());
		}

		// properties
		if (model.getProperties() != null && model.getProperties().size() > 0) {
			device.setProperties(model.getProperties());
		}

		// tags
		if (model.getTags() != null && model.getTags().size() > 0) {
			device.setTags(model.getTags());
		}

		boolean softwareReleaseUpdated = false;

		// softwareName
		if (StringUtils.isNotEmpty(model.getSoftwareName())) {
			device.setSoftwareName(model.getSoftwareName());
			softwareReleaseUpdated = true;
		}

		// softwareVersion
		if (StringUtils.isNotEmpty(model.getSoftwareVersion())) {
			device.setSoftwareVersion(model.getSoftwareVersion());
			softwareReleaseUpdated = true;
		}

		if (softwareReleaseUpdated) {
			device.setSoftwareReleaseId(getSoftwareReleaseService().findRheaSoftwareReleaseId(device.getDeviceTypeId(),
					device.getSoftwareName(), device.getSoftwareVersion()));
		}

		device = getDeviceService().update(device, accessKey.getId());
		return new HidModel().withHid(device.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "list historical device events")
	@RequestMapping(path = "/{hid}/events", method = RequestMethod.GET)
	public PagingResultModel<DeviceEventModel> listDeviceEvents(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "createdDateFrom") @RequestParam(name = "createdDateFrom", required = false) String createdDateFrom,
			@ApiParam(value = "createdDateTo") @RequestParam(name = "createdDateTo", required = false) String createdDateTo,
			@ApiParam(value = "sortField") @RequestParam(name = "sortField", required = false) String sortField,
			@ApiParam(value = "sortDirection") @RequestParam(name = "sortDirection", required = false) String sortDirection,
			@ApiParam(value = "device event statuses") @RequestParam(name = "statuses", required = false) String[] statuses,
			@ApiParam(value = "device action type system names") @RequestParam(name = "systemNames", required = false) String[] systemNames,
			@ApiParam(value = "page index") @RequestParam(name = "_page", required = false) Integer page,
			@ApiParam(value = "items per page") @RequestParam(name = "_size", required = false) Integer size) {

		validateCanReadDevice(hid);

		size = apiUtil.validateSize(size);
		page = apiUtil.validatePage(page);

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is not found");

		Instant from = apiUtil.parseDateParam(createdDateFrom);
		Instant to = apiUtil.parseDateParam(createdDateTo);
		if (from != null && to != null && from.isAfter(to)) {
			throw new AcsLogicalException("createdDateFrom is after createdDateTo");
		}

		PageRequest pageRequest = apiUtil.buildPageRequest(page, size, sortField, sortDirection);

		DeviceEventSearchParams params = new DeviceEventSearchParams();
		params.addDeviceIds(device.getId());
		params.addStatuses(statuses);
		params.setCreatedDateFrom(from);
		params.setCreatedDateTo(to);
		if (systemNames != null) {
			Arrays.stream(systemNames).filter(name -> name != null).forEach(name -> {
				DeviceActionType deviceActionType = getKronosCache()
						.findDeviceActionTypeBySystemName(device.getApplicationId(), name);
				if (deviceActionType != null) {
					params.addDeviceActionTypeIds(deviceActionType.getId());
				}
			});
		}

		Page<DeviceEvent> deviceEventPage = deviceEventService.getDeviceEventRepository().findDeviceEvents(pageRequest,
				params);
		List<DeviceEventModel> data = new ArrayList<>();
		for (DeviceEvent deviceEvent : deviceEventPage) {
			DeviceActionType deviceActionType = null;
			if (deviceEvent.getDeviceActionTypeId() != null) {
				deviceActionType = getKronosCache().findDeviceActionTypeById(deviceEvent.getDeviceActionTypeId());
			}
			data.add(buildDeviceEventModel(deviceEvent, deviceActionType));
		}
		PagingResultModel<DeviceEventModel> result = new PagingResultModel<>();
		result.setPage(pageRequest.getPageNumber());
		result.setSize(pageRequest.getPageSize());
		result.setTotalPages(deviceEventPage.getTotalPages());
		result.setTotalSize(deviceEventPage.getTotalElements());
		result.setData(data);
		return result;
	}

	@ApiOperation(value = "list device audit logs")
	@RequestMapping(path = "/{hid}/logs", method = RequestMethod.GET)
	public PagingResultModel<AuditLogModel> listDeviceAuditLogs(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "createdDateFrom") @RequestParam(name = "createdDateFrom", required = false) String createdDateFrom,
			@ApiParam(value = "createdDateTo") @RequestParam(name = "createdDateTo", required = false) String createdDateTo,
			@ApiParam(value = "user hids") @RequestParam(name = "userHids", required = false) String[] userHids,
			@ApiParam(value = "types") @RequestParam(name = "types", required = false) String[] types,
			@ApiParam(value = "sortField") @RequestParam(name = "sortField", required = false) String sortField,
			@ApiParam(value = "sortDirection") @RequestParam(name = "sortDirection", required = false) String sortDirection,
			@ApiParam(value = "page index") @RequestParam(name = "_page", required = false) Integer page,
			@ApiParam(value = "items per page") @RequestParam(name = "_size", required = false) Integer size) {

		AccessKey accessKey = validateCanReadDevice(hid);

		size = apiUtil.validateSize(size);
		page = apiUtil.validatePage(page);

		Instant from = apiUtil.parseDateParam(createdDateFrom);
		Instant to = apiUtil.parseDateParam(createdDateTo);
		if (from != null && to != null && from.isAfter(to)) {
			throw new AcsLogicalException("createdDateFrom is after createdDateTo");
		}

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is not found");

		Set<String> userIds = apiUtil.getUserIdsByHids(userHids);

		PageRequest pageRequest = apiUtil.buildPageRequest(page, size, sortField, sortDirection);

		Page<AuditLog> list = getAuditLogService().getAuditLogRepository().findAuditLogs(pageRequest, null,
				new String[] { accessKey.getApplicationId() }, types, new String[] { device.getId() }, null, from, to,
				userIds.toArray(new String[userIds.size()]));
		List<AuditLogModel> data = new ArrayList<>();
		list.forEach(item -> data.add(buildAuditLogModel(item, device.getHid())));

		PagingResultModel<AuditLogModel> result = new PagingResultModel<>();
		result.setTotalPages(list.getTotalPages());
		result.setTotalSize(list.getTotalElements());
		result.setPage(pageRequest.getPageNumber());
		result.setSize(pageRequest.getPageSize());
		result.setData(data);
		return result;
	}

	@ApiOperation(value = "list device test results")
	@RequestMapping(path = "/{hid}/test-results", method = RequestMethod.GET)
	public PagingResultModel<TestResultModel> listTestResults(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "status") @RequestParam(name = "status", required = false) Set<String> statuses,
			@ApiParam(value = "test procedure hid") @RequestParam(name = "testProcedureHid", required = false) String testProcedureHid,
			@ApiParam(value = "createdDateFrom") @RequestParam(name = "createdDateFrom", required = false) String createdDateFrom,
			@ApiParam(value = "createdDateTo") @RequestParam(name = "createdDateTo", required = false) String createdDateTo,
			@ApiParam(value = "sort field") @RequestParam(name = "sortField", required = false) String sortField,
			@ApiParam(value = "sort direction") @RequestParam(name = "sortDirection", required = false) String sortDirection,
			@ApiParam(value = "page index") @RequestParam(name = "_page", required = false) Integer page,
			@ApiParam(value = "items per page") @RequestParam(name = "_size", required = false) Integer size) {

		validateCanReadDevice(hid);

		PageRequest pageRequest = apiUtil.buildPageRequest(apiUtil.validatePage(page), apiUtil.validateSize(size),
				sortField, sortDirection);

		Instant from = apiUtil.parseDateParam(createdDateFrom);
		Instant to = apiUtil.parseDateParam(createdDateTo);
		if (from != null && to != null && from.isAfter(to)) {
			throw new AcsLogicalException("createdDateFrom is after createdDateTo");
		}

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is not found");

		TestResultSearchParams params = new TestResultSearchParams().addObjectId(device.getId()).addCreatedBefore(to)
				.addCreatedAfter(from);
		if (StringUtils.isNotBlank(testProcedureHid)) {
			TestProcedure testProcedure = getKronosCache().findTestProcedureByHid(testProcedureHid);
			Assert.notNull(testProcedure, "testProcedure not found");
			params.addTestProcedureIds(testProcedure.getId());
		}
		if (statuses != null) {
			statuses.forEach(params::addStatuses);
		}
		Page<TestResult> testResults = testResultService.getTestResultRepository().findTestResult(pageRequest, params);
		List<TestResultModel> data = testResults.getContent().stream().map(this::buildTestResultModel)
				.collect(Collectors.toCollection(ArrayList::new));

		PagingResultModel<TestResultModel> result = new PagingResultModel<>();
		result.setPage(pageRequest.getPageNumber());
		result.setSize(pageRequest.getPageSize());
		result.setTotalPages(testResults.getTotalPages());
		result.setTotalSize(testResults.getTotalElements());
		result.setData(data);
		return result;
	}

	@ApiOperation(value = "get last location")
	@RequestMapping(path = "/{hid}/location", method = RequestMethod.GET)
	public LastLocationModel getLastLocation(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid) {

		validateCanReadDevice(hid);
		Device device = getKronosCache().findDeviceByHid(hid);
		LastLocation lastLocation = lastLocationService.getLastLocationRepository()
				.findByObjectTypeAndObjectId(LocationObjectType.DEVICE, device.getId());
		Assert.notNull(lastLocation, "lastLocation not found");

		return buildLastLocationModel(lastLocation, device.getHid());
	}

	@ApiOperation(value = "set last location")
	@RequestMapping(path = "/{hid}/location", method = RequestMethod.PUT)
	public HidModel setLastLocation(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "last location model", required = true) @RequestBody(required = false) LastLocationRegistrationModel body,
			HttpServletRequest request) {
		String method = "setLastLocation";

		AccessKey accessKey = validateCanUpdateDevice(hid);
		Device device = getKronosCache().findDeviceByHid(hid);

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		LastLocationRegistrationModel model = JsonUtils.fromJson(getApiPayload(), LastLocationRegistrationModel.class);

		LastLocation lastLocation = apiUtil.updateLastLocation(model, LocationObjectType.DEVICE, device.getId());
		return new HidModel().withHid(lastLocation.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "backup device configuration")
	@RequestMapping(path = "/{hid}/config-backups", method = RequestMethod.POST)
	public HidModel backupConfiguration(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "config backup name", required = true) @RequestBody(required = false) CreateConfigBackupModel body,
			HttpServletRequest request) {
		String method = "backupConfiguration";

		CreateConfigBackupModel model = JsonUtils.fromJson(getApiPayload(), CreateConfigBackupModel.class);
		Assert.notNull(model, "configBackup model is null");
		Assert.hasText(model.getName(), "configBackup name is empty");

		AccessKey accessKey = validateCanUpdateDevice(hid);
		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is not found");

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		ConfigBackup configBackup = getDeviceService().backupConfiguration(device, model.getName(), accessKey.getId());
		return new HidModel().withHid(configBackup.getHid()).withMessage("OK");
	}

	@ApiOperation(value = "restore device configuration")
	@RequestMapping(path = "/{hid}/config-backups/{configBackupHid}/restore", method = RequestMethod.POST)
	public StatusModel restoreConfiguration(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "config backup hid", required = true) @PathVariable(value = "configBackupHid") String configBackupHid,
			HttpServletRequest request) {
		String method = "restoreConfiguration";

		AccessKey accessKey = validateCanUpdateDevice(hid);
		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is not found");

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		ConfigBackup configBackup = configBackupService.getConfigBackupRepository().doFindByHid(configBackupHid);
		Assert.notNull(configBackup, "configBackup is not found");

		getDeviceService().restoreConfiguration(device, configBackup, accessKey.getId());
		return StatusModel.OK;
	}

	@ApiOperation(value = "list device configuration backups")
	@RequestMapping(path = "/{hid}/config-backups", method = RequestMethod.GET)
	public PagingResultModel<ConfigBackupModel> listConfigurationBackups(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid,
			@ApiParam(value = "createdDateFrom") @RequestParam(name = "createdDateFrom", required = false) String createdDateFrom,
			@ApiParam(value = "createdDateTo") @RequestParam(name = "createdDateTo", required = false) String createdDateTo,
			@ApiParam(value = "sortField") @RequestParam(name = "sortField", required = false) String sortField,
			@ApiParam(value = "sortDirection") @RequestParam(name = "sortDirection", required = false) String sortDirection,
			@ApiParam(value = "page index") @RequestParam(name = "_page", required = false) Integer page,
			@ApiParam(value = "items per page") @RequestParam(name = "_size", required = false) Integer size) {

		size = apiUtil.validateSize(size);
		page = apiUtil.validatePage(page);

		Instant from = apiUtil.parseDateParam(createdDateFrom);
		Instant to = apiUtil.parseDateParam(createdDateTo);
		if (from != null && to != null && from.isAfter(to)) {
			throw new AcsLogicalException("createdDateFrom is after createdDateTo");
		}

		validateCanReadDevice(hid);

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device is not found");
		PageRequest pageRequest = apiUtil.buildPageRequest(page, size, sortField, sortDirection);
		ConfigBackupSearchParams params = new ConfigBackupSearchParams().addObjectIds(device.getId())
				.addCreatedAfter(from).addCreatedBefore(to);
		Page<ConfigBackup> backups = configBackupService.getConfigBackupRepository().findConfigBackups(pageRequest,
				params);
		List<ConfigBackupModel> data = backups.getContent().stream().map(this::buildConfigBackupModel)
				.collect(Collectors.toCollection(ArrayList::new));

		PagingResultModel<ConfigBackupModel> result = new PagingResultModel<>();
		result.setPage(pageRequest.getPageNumber());
		result.setSize(pageRequest.getPageSize());
		result.setTotalPages(backups.getTotalPages());
		result.setTotalSize(backups.getTotalElements());
		result.setData(data);
		return result;
	}

	@ApiOperation(value = "delete device")
	@RequestMapping(path = "/{hid}", method = RequestMethod.DELETE)
	public StatusModel delete(@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid,
			HttpServletRequest request) {
		String method = "delete";

		AccessKey accessKey = validateCanDeleteDevice(hid);
		Device device = getKronosCache().findDeviceByHid(hid);

		auditLog(method, device.getApplicationId(), device.getId(), accessKey.getId(), request);

		getDeviceService().delete(device, accessKey.getId());
		return StatusModel.OK;
	}

	private DeviceType checkCreateNewDeviceType(DeviceRegistrationModel model, AccessKey accessKey) {
		String method = "checkCreateNewDeviceType";
		DeviceType type = getKronosCache().findDeviceTypeByName(accessKey.getApplicationId(), model.getType());
		if (type == null) {
			try {
				type = new DeviceType();
				type.setName(model.getType());
				type.setDescription(model.getType());
				type.setApplicationId(accessKey.getApplicationId());
				type.setEnabled(true);
				type.setDeviceCategory(AcnDeviceCategory.DEVICE);
				type = getDeviceTypeService().create(type, accessKey.getId());
				getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.DeviceType.CreateDeviceType)
						.applicationId(accessKey.getApplicationId()).productName(ProductSystemNames.KRONOS)
						.objectId(type.getId()).by(accessKey.getId()).parameter("name", model.getType()));
				logInfo(method, "created new device type: %s", model.getType());
			} catch (Throwable t) {
				logError(method, "error creating new device type, could be duplicate!");
				type = getKronosCache().findDeviceTypeByName(accessKey.getApplicationId(), model.getType());
			}
			Assert.notNull(type, "unable to create new device type");
		}
		return type;
	}

	private Device processCloudPlatformIntegration(Device device) {
		String method = "cloudPlatformIntegration";

		switch (findDeviceIoTProvider(device)) {
		case IBM:
			IbmDevice ibmDevice = ibmIntService.checkAndCreateDevice(device);
			if (ibmDevice != null) {
				logInfo(method, "IBM integration OK!");
			} else {
				logError(method, "IBM integration ERROR!");
			}
			break;
		case AWS:
			break;
		case ArrowConnect:
			break;
		default:
			break;
		}
		return device;
	}

	private DeviceEventModel buildDeviceEventModel(DeviceEvent deviceEvent, DeviceActionType deviceActionType) {
		Assert.notNull(deviceEvent, "deviceEvent is null");
		DeviceEventModel result = buildModel(new DeviceEventModel(), deviceEvent);
		result.setCriteria(deviceEvent.getCriteria());
		result.setStatus(deviceEvent.getStatus().name());
		result.setCreatedDate(deviceEvent.getCreatedDate());
		if (deviceActionType != null) {
			result.setDeviceActionTypeName(deviceActionType.getName());
		}
		return result;
	}

	private IoTProvider findDeviceIoTProvider(Device device) {
		String method = "findDeviceIoTProvider";
		IoTProvider iotProvider = null;
		if (StringUtils.isNotEmpty(device.getUserId())) {
			// first search for KronosUser
			KronosUser kronosUser = kronosUserService.getKronosUserRepository()
					.findByUserIdAndApplicationId(device.getUserId(), device.getApplicationId());
			if (kronosUser != null) {
				// set IoTProvider to the one configured at user level
				iotProvider = kronosUser.getIotProvider();
				logInfo(method, "iotProvider at user level: %s", iotProvider);
			}
		}
		if (iotProvider == null) {
			// if IoTProvider is not configured at user level or set to null
			KronosApplication kronosApp = appService.getKronosApplicationRepository()
					.findByApplicationId(device.getApplicationId());
			Assert.notNull(kronosApp, "kronosApplication not found: " + device.getApplicationId());
			iotProvider = kronosApp.getIotProvider();
			logInfo(method, "iotProvider at application level: %s", iotProvider);
			Assert.notNull(iotProvider, "iotProvider is not found");
		}
		return iotProvider;
	}

	@ApiOperation(value = "get available firmware for device by hid")
	@RequestMapping(path = "/{hid}/firmware/available", method = RequestMethod.GET)
	public List<AvailableFirmwareModel> availableFirmware(
			@ApiParam(value = "device hid", required = true) @PathVariable(value = "hid") String hid,
			HttpServletRequest request) {
		validateCanReadDevice(hid);
		return findAvailableSoftwarereleases(hid, AcnDeviceCategory.DEVICE);
	}
}