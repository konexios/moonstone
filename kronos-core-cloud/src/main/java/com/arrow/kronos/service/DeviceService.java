package com.arrow.kronos.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.DeviceActionTypeConstants.PostBackURL;
import com.arrow.kronos.DeviceActionTypeConstants.PostBackURL.ContentType;
import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.ConfigBackup;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceConfigBackup;
import com.arrow.kronos.data.DeviceState;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.repo.DeviceRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AccessPrivilege;
import com.arrow.pegasus.data.AccessPrivilege.AccessLevel;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.event.EventBuilder;
import com.arrow.pegasus.data.event.EventParameter;
import com.arrow.pegasus.data.location.LocationObjectType;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.LastLocationService;
import com.arrow.rhea.data.SoftwareRelease;

import moonstone.acn.AcnEventNames;
import moonstone.acn.client.model.DeviceRegistrationModel;
import moonstone.acs.AcsLogicalException;
import moonstone.acs.JsonUtils;

@Service
public class DeviceService extends KronosServiceAbstract {

	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private SoftwareReleaseTransService softwareReleaseTransService;
	@Autowired
	private DeviceStateService deviceStateService;
	@Autowired
	private DeviceStateTransService deviceStateTransService;
	@Autowired
	private DeviceEventService deviceEventService;
	@Autowired
	private IbmDeviceService ibmDeviceService;
	@Autowired
	private LastTelemetryItemService lastTelemetryItemService;
	@Autowired
	private TelemetryService telemetryService;
	@Autowired
	private TestResultService testResultService;
	@Autowired
	private SpringDataEsTelemetryItemService esTelemetryItemService;
	@Autowired
	private LastLocationService lastLocationService;
	@Autowired
	private GatewayCommandService gatewayCommandService;
	@Autowired
	private ConfigBackupService configBackupService;

	public DeviceRepository getDeviceRepository() {
		return deviceRepository;
	}

	public Device create(Device device, String who) {
		Assert.notNull(device, "device is null");
		Assert.hasText(device.getApplicationId(), "applicationId is empty");

		String method = "create";

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		Assert.notNull(application, "application not found: " + device.getApplicationId());
		checkEnabled(application, "application");

		// existing uid check
		Device existing = deviceRepository.findByApplicationIdAndUid(device.getApplicationId(), device.getUid());
		Assert.isNull(existing,
		        "Device already exists! applicationId: " + device.getApplicationId() + ", uid: " + device.getUid());

		// persist
		device = deviceRepository.doInsert(device, who);
		logInfo(method, "created device: %s ---> %s", device.getUid(), device.getId());

		// create ownerKey
		AccessKey ownerKey = getClientAccessKeyApi().create(application.getCompanyId(), application.getId(),
		        Collections.singletonList(new AccessPrivilege(device.getPri(), AccessLevel.OWNER)), who);
		logInfo(method, "created ownerKey: %s", ownerKey.getId());

		if (device.getRefDeviceType() == null)
			device.setRefDeviceType(getKronosCache().findDeviceTypeById(device.getDeviceTypeId()));

		Assert.notNull(device.getRefDeviceType(), "device.refDeviceType not populated");

		DeviceState deviceState = new DeviceState();
		deviceState.setApplicationId(device.getApplicationId());
		deviceState.setDeviceId(device.getId());
		deviceState = deviceStateService.create(deviceState, who);
		logInfo(method, "created deviceState: %s", deviceState.getId());

		// audit log
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.CreateDevice)
		        .applicationId(device.getApplicationId()).productName(ProductSystemNames.KRONOS)
		        .objectId(device.getId()).by(who).parameter("userId", device.getUserId())
		        .parameter("gatewayId", device.getGatewayId()).parameter("type", device.getRefDeviceType().getName())
		        .parameter("uid", device.getUid()));

		return device;
	}

	public Device update(Device device, String who) {
		Assert.notNull(device, "device is null");

		// persist
		device = deviceRepository.doSave(device, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.UpdateDevice)
		        .applicationId(device.getApplicationId()).productName(ProductSystemNames.KRONOS)
		        .objectId(device.getId()).by(who).parameter("userId", device.getUserId())
		        .parameter("gatewayId", device.getGatewayId()).parameter("deviceTypeId", device.getDeviceTypeId())
		        .parameter("uid", device.getUid()));

		// clear cache
		getKronosCache().clearDevice(device);

		return device;
	}

	public Device createDeviceAction(Device device, DeviceAction deviceAction, String who) {
		Assert.notNull(device, "device is null");

		validateDeviceAction(deviceAction);

		// push the action into the list
		device.getActions().add(deviceAction);

		// persist
		device = deviceRepository.doSave(device, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.CreateDeviceAction)
		        .applicationId(device.getApplicationId()).productName(ProductSystemNames.KRONOS)
		        .objectId(device.getId()).by(who)
		        .parameter("deviceActionIndex", String.valueOf(device.getActions().size() - 1))
		        .parameter("deviceActionTypeId", deviceAction.getDeviceActionTypeId()));

		// clear cache
		getKronosCache().clearDevice(device);

		return device;
	}

	public Device updateDeviceAction(Device device, int deviceActionIndex, DeviceAction deviceAction, String who) {
		Assert.notNull(device, "device is null");
		Assert.notNull(deviceAction, "deviceAction is null");
		Assert.notNull(deviceAction.getDeviceActionTypeId(), "deviceActionTypeId is null");
		Assert.isTrue(deviceActionIndex >= 0 && deviceActionIndex < device.getActions().size(),
		        "deviceActionIndex is out of bounds");

		validateDeviceAction(deviceAction);

		// update the action in the list
		device.getActions().set(deviceActionIndex, deviceAction);

		// persist
		device = deviceRepository.doSave(device, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.UpdateDeviceAction)
		        .applicationId(device.getApplicationId()).productName(ProductSystemNames.KRONOS)
		        .objectId(device.getId()).by(who).parameter("deviceActionIndex", String.valueOf(deviceActionIndex))
		        .parameter("deviceActionTypeId", deviceAction.getDeviceActionTypeId()));

		// clear cache
		getKronosCache().clearDevice(device);

		return device;
	}

	public Device deleteDeviceActions(Device device, List<Integer> actionIndices, String who) {
		Assert.notNull(device, "device is null");

		List<DeviceAction> actions = device.getActions();
		// DeviceAction objects to be deleted
		List<DeviceAction> deletedActions = new ArrayList<>(actionIndices.size());

		for (int i = 0; i < actions.size(); i++) {
			if (actionIndices.contains(i)) {
				deletedActions.add(actions.get(i));
			}
		}

		actions.removeAll(deletedActions);

		// persist
		device = deviceRepository.doSave(device, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.DeleteDeviceActions)
		        .applicationId(device.getApplicationId()).productName(ProductSystemNames.KRONOS)
		        .objectId(device.getId()).by(who)
		        .parameter("deletedActionsCount", String.valueOf(deletedActions.size())));

		// clear cache
		getKronosCache().clearDevice(device);

		return device;
	}

	public Device enable(String deviceId, String who) {
		return updateEnabled(deviceId, who, true);
	}

	public Device disable(String deviceId, String who) {
		return updateEnabled(deviceId, who, false);
	}

	private Device updateEnabled(String deviceId, String who, boolean enabled) {
		Assert.hasLength(deviceId, "deviceId is empty");
		Assert.hasLength(who, "who is empty");

		String auditLogType = KronosAuditLog.Device.EnableDevice;
		if (!enabled)
			auditLogType = KronosAuditLog.Device.DisableDevice;

		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device not found: " + deviceId);
		device = populate(device);

		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		Assert.notNull(application, "application not found: " + device.getApplicationId());

		// change enabled
		device.setEnabled(enabled);

		// save
		device = deviceRepository.doSave(device, who);

		// clear cache
		getKronosCache().clearDevice(device);

		// audit log
		getAuditLogService().save(AuditLogBuilder.create().type(auditLogType).applicationId(device.getApplicationId())
		        .productName(ProductSystemNames.KRONOS).objectId(device.getId()).by(who)
		        .parameter("userId", device.getUserId()).parameter("gatewayId", device.getGatewayId())
		        .parameter("deviceTypeId", device.getDeviceTypeId()).parameter("uid", device.getUid()));

		return device;
	}

	public Device populate(Device device) {
		if (device != null) {

			if (device.getRefApplication() == null && !StringUtils.isEmpty(device.getApplicationId()))
				device.setRefApplication(getCoreCacheService().findApplicationById(device.getApplicationId()));

			if (device.getRefGateway() == null && !StringUtils.isEmpty(device.getGatewayId()))
				device.setRefGateway(getKronosCache().findGatewayById(device.getGatewayId()));

			if (device.getRefDeviceType() == null && !StringUtils.isEmpty(device.getDeviceTypeId()))
				device.setRefDeviceType(getKronosCache().findDeviceTypeById(device.getDeviceTypeId()));
		}
		return device;
	}

	public Event requestConfigurationUpdate(String deviceId) {
		String method = "requestConfigurationUpdate";

		Assert.hasText(deviceId, "deviceId is empty");
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is not found");
		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		Assert.notNull(application, "application is not found");
		Gateway gateway = getKronosCache().findGatewayById(device.getGatewayId());
		Assert.notNull(gateway, "gateway is not found");

		EventBuilder eventBuilder = EventBuilder.create().applicationId(application.getId())
		        .name(AcnEventNames.ServerToGateway.DEVICE_CONFIGURATION_UPDATE)
		        .parameter(EventParameter.InString("deviceHid", device.getHid()));
		AccessKey gatewayOwnerKey = getClientAccessKeyApi().findOwnerKey(gateway.getPri());
		Event event = gatewayCommandService.sendEvent(eventBuilder.build(), gateway.getId(), gatewayOwnerKey);
		logInfo(method, "event has been sent to gatewayId=" + gateway.getId() + ", eventId=" + event.getId());
		return event;
	}

	public ConfigBackup backupConfiguration(Device device, String configBackupName, String who) {
		Assert.notNull(device, "device is null");
		Assert.hasText(device.getId(), "deviceId is empty");
		Assert.hasText(configBackupName, "configBackupName is empty");
		Assert.hasText(who, "who is empty");

		ConfigBackup configBackup = new ConfigBackup();
		configBackup.setType(ConfigBackup.Type.DEVICE);
		configBackup.setObjectId(device.getId());
		configBackup.setApplicationId(device.getApplicationId());
		configBackup.setName(configBackupName);
		configBackup.setDeviceConfig(populateDeviceConfigBackup(device));

		configBackup = configBackupService.create(configBackup, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.BackupDeviceConfiguration)
		        .applicationId(device.getApplicationId()).objectId(device.getId()).by(who)
		        .parameter("name", device.getName()).parameter("uid", device.getUid())
		        .parameter("configBackupId", configBackup.getId()));

		return configBackup;
	}

	public Device restoreConfiguration(Device device, ConfigBackup configBackup, String who) {
		Assert.notNull(device, "device is null");
		Assert.hasText(device.getId(), "deviceId is empty");
		Assert.notNull(configBackup, "configBackup is null");
		Assert.hasText(configBackup.getObjectId(), "objectId is empty");
		Assert.hasText(who, "who is empty");

		Assert.isTrue(ConfigBackup.Type.DEVICE.equals(configBackup.getType()), "configBackup type mismatched");
		Assert.isTrue(device.getId().equals(configBackup.getObjectId()), "device mismatched");

		device = update(populateDeviceConfigFromBackup(device, configBackup.getDeviceConfig()), who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.RestoreDeviceConfiguration)
		        .applicationId(device.getApplicationId()).objectId(device.getId()).by(who)
		        .parameter("name", device.getName()).parameter("uid", device.getUid())
		        .parameter("configBackupId", configBackup.getId()));

		return device;
	}

	public Event requestConfigurationRestore(String deviceId) {
		String method = "requestConfigurationRestore";

		Assert.hasText(deviceId, "deviceId is empty");
		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is not found");
		Application application = getCoreCacheService().findApplicationById(device.getApplicationId());
		Assert.notNull(application, "application is not found");
		Gateway gateway = getKronosCache().findGatewayById(device.getGatewayId());
		Assert.notNull(gateway, "gateway is not found");

		DeviceRegistrationModel payload = buildConfigurationRestorePayload(device, gateway);

		EventBuilder eventBuilder = EventBuilder.create().applicationId(application.getId())
		        .name(AcnEventNames.ServerToGateway.DEVICE_CONFIGURATION_RESTORE)
		        .parameter(EventParameter.InString("deviceHid", device.getHid()))
		        .parameter(EventParameter.InString("payload", JsonUtils.toJson(payload)));

		AccessKey gatewayOwnerKey = getClientAccessKeyApi().findOwnerKey(gateway.getPri());
		Event event = gatewayCommandService.sendEvent(eventBuilder.build(), gateway.getId(), gatewayOwnerKey);
		logInfo(method, "event has been sent to gatewayId=" + gateway.getId() + ", eventId=" + event.getId());
		return event;
	}

	private DeviceRegistrationModel buildConfigurationRestorePayload(Device device, Gateway gateway) {
		DeviceRegistrationModel model = new DeviceRegistrationModel();
		model.setEnabled(device.isEnabled());
		model.setGatewayHid(gateway.getHid());
		model.setInfo(device.getInfo());
		model.setName(device.getName());
		if (StringUtils.isNotBlank(device.getNodeId())) {
			Node node = getKronosCache().findNodeById(device.getNodeId());
			Assert.notNull(node, "node is not found");
			model.setNodeHid(node.getHid());
		}
		model.setProperties(device.getProperties());
		model.setTags(device.getTags());
		DeviceType deviceType = getKronosCache().findDeviceTypeById(device.getDeviceTypeId());
		Assert.notNull(deviceType, "deviceType is not found");
		model.setType(deviceType.getName());
		model.setUid(device.getUid());
		if (StringUtils.isNotBlank(device.getUserId())) {
			User user = getCoreCacheService().findUserById(device.getUserId());
			Assert.notNull(user, "user is not found");
			model.setUserHid(user.getHid());
		}
		return model;
	}

	private DeviceConfigBackup populateDeviceConfigBackup(Device device) {
		Assert.notNull(device, "device is null");

		DeviceConfigBackup deviceConfig = new DeviceConfigBackup();
		deviceConfig.setActions(device.getActions());
		deviceConfig.setDeviceTypeId(device.getDeviceTypeId());
		deviceConfig.setEnabled(device.isEnabled());
		deviceConfig.setExternalId(device.getExternalId());
		deviceConfig.setGatewayId(device.getGatewayId());
		deviceConfig.setIndexTelemetry(device.getIndexTelemetry());
		deviceConfig.setInfo(device.getInfo());
		deviceConfig.setName(device.getName());
		deviceConfig.setNodeId(device.getNodeId());
		deviceConfig.setPersistTelemetry(device.getPersistTelemetry());
		deviceConfig.setProperties(device.getProperties());
		deviceConfig.setSoftwareReleaseId(device.getSoftwareReleaseId());
		deviceConfig.setTags(device.getTags());
		deviceConfig.setUid(device.getUid());
		deviceConfig.setUserId(device.getUserId());
		deviceConfig.setSoftwareName(device.getSoftwareName());
		deviceConfig.setSoftwareVersion(device.getSoftwareVersion());
		return deviceConfig;
	}

	private Device populateDeviceConfigFromBackup(Device device, DeviceConfigBackup backup) {
		Assert.notNull(device, "device is null");
		Assert.notNull(backup, "deviceConfigBackup is null");

		device.setActions(backup.getActions());
		device.setEnabled(backup.isEnabled());
		device.setExternalId(backup.getExternalId());
		device.setIndexTelemetry(backup.getIndexTelemetry());
		device.setInfo(backup.getInfo());
		device.setPersistTelemetry(backup.getPersistTelemetry());
		device.setProperties(backup.getProperties());
		device.setTags(backup.getTags());
		// deviceTypeId
		Assert.hasText(backup.getDeviceTypeId(), "deviceTypeId is empty");
		DeviceType deviceType = getKronosCache().findDeviceTypeById(backup.getDeviceTypeId());
		Assert.notNull(deviceType, "deviceType is not found");
		device.setDeviceTypeId(backup.getDeviceTypeId());
		// gatewayId
		if (backup.getGatewayId() != null) {
			Gateway gateway = getKronosCache().findGatewayById(backup.getGatewayId());
			Assert.notNull(gateway, "gateway is not found");
		}
		device.setGatewayId(backup.getGatewayId());
		// name
		Assert.hasText(backup.getName(), "name is empty");
		device.setName(backup.getName());
		// nodeId
		if (backup.getNodeId() != null) {
			Node node = getKronosCache().findNodeById(backup.getNodeId());
			Assert.notNull(node, "node is not found");
		}
		device.setNodeId(backup.getNodeId());
		// softwareReleaseId
		if (backup.getSoftwareReleaseId() != null) {
			SoftwareRelease softwareRelease = getRheaClientCacheApi()
			        .findSoftwareReleaseById(backup.getSoftwareReleaseId());
			Assert.notNull(softwareRelease, "softwareRelease is not found");
		}
		device.setSoftwareReleaseId(backup.getSoftwareReleaseId());
		// uid
		Assert.hasText(backup.getUid(), "uid is null");
		device.setUid(backup.getUid());
		// userId
		if (backup.getUserId() != null) {
			User user = getCoreCacheService().findUserById(backup.getUserId());
			Assert.notNull(user, "user is not found");
		}
		device.setUserId(backup.getUserId());
		// softwareName
		device.setSoftwareName(backup.getSoftwareName());
		// softwareVersion
		device.setSoftwareVersion(backup.getSoftwareVersion());

		return device;
	}

	public void delete(Device device, String who) {
		String method = "delete";
		Assert.notNull(device, "device is null");
		Assert.hasLength(who, "who is empty");

		logDebug(method, "softwareReleaseTrans ...");
		softwareReleaseTransService.deleteBy(device, who);

		logDebug(method, "deviceStateService ...");
		deviceStateService.deleteBy(device, who);

		logDebug(method, "deviceStateTransService ...");
		deviceStateTransService.deleteBy(device, who);

		logDebug(method, "deviceEventService ...");
		deviceEventService.deleteBy(device, who);

		logDebug(method, "ibmDeviceService ...");
		ibmDeviceService.deleteBy(device);

		logDebug(method, "lastTelemetryItemService ...");
		lastTelemetryItemService.deleteBy(device);

		// this will delete telemetry items as well
		logDebug(method, "telemetryServiceService ...");
		telemetryService.deleteBy(device);

		logDebug(method, "testResultService ...");
		testResultService.deleteBy(device, who);

		logDebug(method, "esTelemetryItemService ...");
		esTelemetryItemService.deleteBy(device);

		logDebug(method, "lastLocationService ...");
		lastLocationService.deleteBy(LocationObjectType.DEVICE, device.getId());

		logDebug(method, "configBAckupService ...");
		configBackupService.deleteBy(device, who);

		logDebug(method, "removePriFromAccessKeys ...");
		getClientAccessKeyApi().removePriFromAccessKeys(device.getPri(), who);

		// delete from collection
		logDebug(method, "deviceRepository ...");
		deviceRepository.delete(device);

		logDebug(method, "Device id=" + device.getId() + " has been deleted");

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.DeleteDevice)
		        .applicationId(device.getApplicationId()).productName(ProductSystemNames.KRONOS)
		        .objectId(device.getId()).by(who).parameter("userId", device.getUserId())
		        .parameter("gatewayId", device.getGatewayId()).parameter("deviceTypeId", device.getDeviceTypeId())
		        .parameter("uid", device.getUid()));

		// clear cache
		getKronosCache().clearDevice(device);
	}

	public void deleteBy(Gateway gateway, String who) {
		Assert.notNull(gateway, "gateway is null");
		Assert.hasLength(who, "who is empty");

		List<Device> devices = deviceRepository.findAllByGatewayId(gateway.getId());
		for (Device device : devices) {
			this.delete(device, who);
		}
	}

	public Device move(Device device, Application toApplication, DeviceType toDeviceType,
	        Map<String, DeviceActionType> mappedDeviceActionTypes, Map<String, TestProcedure> mappedTestProcedures,
	        String who) {
		String method = "move";
		Assert.notNull(device, "device is null");
		Assert.notNull(toApplication, "toApplication is null");
		Assert.notNull(toDeviceType, "toDeviceType is null");
		Assert.notNull(mappedDeviceActionTypes, "mappedDeviceActionTypes is null");
		Assert.hasLength(who, "who is empty");

		deviceStateService.moveBy(device, toApplication, who);
		deviceStateTransService.moveBy(device, toApplication, who);
		deviceEventService.moveBy(device, toApplication, mappedDeviceActionTypes, who);
		testResultService.moveBy(device, toApplication, mappedTestProcedures, who);
		// remove device from existing access keys
		getClientAccessKeyApi().removePriFromAccessKeys(device.getPri(), who);

		// update device
		String fromApplicationId = device.getId();
		String fromDeviceTypeId = device.getDeviceTypeId();
		device.setApplicationId(toApplication.getId());
		device.setDeviceTypeId(toDeviceType.getId());
		String oldUserId = device.getUserId();
		device.setUserId(null);
		device.setNodeId(null);
		// update device action type ids in device actions
		for (DeviceAction action : device.getActions()) {
			if (action.getDeviceActionTypeId() != null) {
				DeviceActionType toDeviceActionType = mappedDeviceActionTypes.get(action.getDeviceActionTypeId());
				Assert.notNull(toDeviceActionType,
				        "failed to map device action type of device to the one in target application");
				action.setDeviceActionTypeId(toDeviceActionType.getId());
			}
		}

		device = this.update(device, who);
		logInfo(method, "Device id=%s has been moved from application id=%s to application id=%s", device.getId(),
		        fromApplicationId, toApplication.getId());
		// create owner access key
		AccessKey ownerKey = getClientAccessKeyApi().create(toApplication.getCompanyId(), toApplication.getId(),
		        Collections.singletonList(new AccessPrivilege(device.getPri(), AccessLevel.OWNER)), who);
		logInfo(method, "created ownerKey: %s", ownerKey.getId());

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.MoveDeviceOut)
		        .applicationId(fromApplicationId).productName(ProductSystemNames.KRONOS).objectId(device.getId())
		        .by(who).parameter("gatewayId", device.getGatewayId()).parameter("name", device.getName())
		        .parameter("uid", device.getUid()).parameter("fromDeviceTypeId", fromDeviceTypeId)
		        .parameter("toDeviceTypeId", toDeviceType.getId()).parameter("oldUserId", oldUserId)
		        .parameter("toApplicationId", toApplication.getId()));
		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Device.MoveDeviceIn)
		        .applicationId(device.getApplicationId()).productName(ProductSystemNames.KRONOS)
		        .objectId(device.getId()).by(who).parameter("gatewayId", device.getGatewayId())
		        .parameter("name", device.getName()).parameter("uid", device.getUid())
		        .parameter("fromDeviceTypeId", fromDeviceTypeId).parameter("toDeviceTypeId", toDeviceType.getId())
		        .parameter("oldUserId", oldUserId).parameter("fromApplicationId", fromApplicationId));

		return device;
	}

	public void moveBy(Gateway gateway, Application toApplication, Map<String, DeviceType> mappedDeviceTypes,
	        Map<String, DeviceActionType> mappedDeviceActionTypes, Map<String, TestProcedure> mappedTestProcedures,
	        String who) {
		Assert.notNull(gateway, "gateway is null");
		Assert.notNull(toApplication, "toApplication is null");
		Assert.notNull(mappedDeviceTypes, "mappedDeviceTypes is null");
		Assert.notNull(mappedDeviceActionTypes, "mappedDeviceActionTypes is null");
		Assert.hasLength(who, "who is empty");

		List<Device> devices = deviceRepository.findAllByGatewayId(gateway.getId());
		for (Device device : devices) {
			DeviceType toDeviceType = mappedDeviceTypes.get(device.getDeviceTypeId());
			Assert.notNull(toDeviceType, "failed to map device type of device to the one in target application");
			this.move(device, toApplication, toDeviceType, mappedDeviceActionTypes, mappedTestProcedures, who);
		}
	}

	private DeviceAction validateDeviceAction(DeviceAction deviceAction) {
		Assert.notNull(deviceAction, "deviceAction is null");
		Assert.notNull(deviceAction.getDeviceActionTypeId(), "deviceActionTypeId is null");

		DeviceActionType deviceActionType = getKronosCache()
		        .findDeviceActionTypeById(deviceAction.getDeviceActionTypeId());
		Assert.notNull(deviceActionType, "deviceActionType is not found");

		if (deviceActionType.getSystemName().equals(PostBackURL.SYSTEM_NAME)) {
			String contentTypeValue = deviceAction.getParameters().get(PostBackURL.PARAMETER_CONTENT_TYPE);
			if (StringUtils.isNotBlank(contentTypeValue)) {
				ContentType contentType = ContentType.fromValue(contentTypeValue);
				switch (contentType) {
				case APPLICATION_XML:
					try {
						String requestBody = deviceAction.getParameters().get(PostBackURL.PARAMETER_REQUEST_BODY);
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						dBuilder.parse(new ByteArrayInputStream(requestBody.getBytes()));
					} catch (Exception e) {
						throw new AcsLogicalException("Invalid xml: " + e.getMessage());
					}
					break;
				default:
				}
			}
		}
		return deviceAction;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return deviceRepository.deleteByApplicationId(applicationId);
	}
}
