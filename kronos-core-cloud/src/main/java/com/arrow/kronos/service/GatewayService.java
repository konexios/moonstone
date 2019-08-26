package com.arrow.kronos.service;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosAuditLog;
import com.arrow.kronos.data.ConfigBackup;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.GatewayConfigBackup;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.data.TestProcedure;
import com.arrow.kronos.repo.GatewayRepository;
import com.arrow.pegasus.ProductSystemNames;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.data.AccessPrivilege;
import com.arrow.pegasus.data.AccessPrivilege.AccessLevel;
import com.arrow.pegasus.data.AuditLogBuilder;
import com.arrow.pegasus.data.event.Event;
import com.arrow.pegasus.data.event.EventBuilder;
import com.arrow.pegasus.data.event.EventParameter;
import com.arrow.pegasus.data.heartbeat.HeartbeatObjectType;
import com.arrow.pegasus.data.location.LocationObjectType;
import com.arrow.pegasus.data.profile.Application;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.service.HeartbeatService;
import com.arrow.pegasus.service.LastHeartbeatService;
import com.arrow.pegasus.service.LastLocationService;
import com.arrow.rhea.data.SoftwareRelease;

import moonstone.acn.AcnEventNames;
import moonstone.acn.client.model.CreateGatewayModel;
import moonstone.acn.client.model.UpdateGatewayModel;
import moonstone.acs.JsonUtils;

@Service
public class GatewayService extends KronosServiceAbstract {

	@Autowired
	private GatewayRepository gatewayRepository;
	@Autowired
	private SoftwareReleaseTransService softwareReleaseTransService;
	@Autowired
	private GatewayCommandService gatewayCommandService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private AwsThingService awsThingService;
	@Autowired
	private AzureDeviceService azureDeviceService;
	@Autowired
	private IbmGatewayService ibmGatewayService;
	@Autowired
	private HeartbeatService heartbeatService;
	@Autowired
	private LastHeartbeatService lastHeartbeatService;
	@Autowired
	private TestResultService testResultService;
	@Autowired
	private LastLocationService lastLocationService;
	@Autowired
	private ConfigBackupService configBackupService;

	public GatewayRepository getGatewayRepository() {
		return gatewayRepository;
	}

	public Gateway create(Gateway gateway, String who) {
		Assert.notNull(gateway, "gateway is null");
		Assert.hasLength(who, "who is empty");

		String method = "create";

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		Assert.notNull(application, "application not found: " + gateway.getApplicationId());
		checkEnabled(application, "application");

		// existing uid check
		Gateway existing = gatewayRepository.findByApplicationIdAndUid(gateway.getApplicationId(), gateway.getUid());
		Assert.isNull(existing,
				"Gateway already exists! applicationId: " + gateway.getApplicationId() + ", uid: " + gateway.getUid());

		// persist
		gateway = gatewayRepository.doInsert(gateway, who);
		logInfo(method, "created gateway: %s ---> %s", gateway.getUid(), gateway.getId());

		// create ownerKey
		checkCreateOwnerKey(gateway, who);

		// audit log
		getAuditLogService().save(AuditLogBuilder.create().applicationId(application.getId())
				.type(KronosAuditLog.Gateway.CreateGateway).productName(ProductSystemNames.KRONOS)
				.objectId(gateway.getId()).by(who).parameter("type", gateway.getType().toString())
				.parameter("name", gateway.getName()).parameter("uid", gateway.getUid()));

		if (gateway.isEnabled()) {
			gatewayCommandService.createMqttSubscriptionQueue(gateway.getHid());
		}

		return gateway;
	}

	public Gateway update(Gateway gateway, String who) {
		Assert.notNull(gateway, "gateway is null");
		Assert.hasLength(who, "who is empty");

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		Assert.notNull(application, "application not found: " + gateway.getApplicationId());

		gateway = gatewayRepository.doSave(gateway, who);

		// create ownerKey
		checkCreateOwnerKey(gateway, who);

		// clear cache
		Gateway cachedGateway = getKronosCache().findGatewayById(gateway.getId());
		getKronosCache().clearGateway(cachedGateway);

		// audit log
		getAuditLogService().save(AuditLogBuilder.create().applicationId(application.getId())
				.type(KronosAuditLog.Gateway.UpdateGateway).productName(ProductSystemNames.KRONOS)
				.objectId(gateway.getId()).by(who).parameter("type", gateway.getType().toString())
				.parameter("name", gateway.getName()).parameter("uid", gateway.getUid()));

		return gateway;
	}

	public void checkCreateOwnerKey(Gateway gateway, String who) {
		String method = "checkCreateOwnerKey";

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		Assert.notNull(application, "application not found: " + gateway.getApplicationId());
		checkEnabled(application, "application");

		AccessKey ownerKey = null;
		try {
			ownerKey = getClientAccessKeyApi().findOwnerKey(gateway.getPri());
		} catch (Exception e) {
			logError(method, "owner key not found or error querying it: " + e.getMessage());
		}

		if (ownerKey != null) {
			logInfo(method, "Owner Key exists for gateway: %s", gateway.getName());
		} else {
			ownerKey = getClientAccessKeyApi().create(application.getCompanyId(), application.getId(),
					Collections.singletonList(new AccessPrivilege(gateway.getPri(), AccessLevel.OWNER)), who);
			logInfo(method, "created owner accessKey: %s", ownerKey.getId());
		}
	}

	public Gateway enable(String gatewayId, String who) {
		Gateway gateway = updateEnabled(gatewayId, who, true);
		gatewayCommandService.createMqttSubscriptionQueue(gateway.getHid());
		return gateway;
	}

	public Gateway disable(String gatewayId, String who) {
		Gateway gateway = updateEnabled(gatewayId, who, false);
		gatewayCommandService.deleteMqttSubscriptionQueue(gateway.getHid());
		return gateway;
	}

	private Gateway updateEnabled(String gatewayId, String who, boolean enabled) {
		Assert.hasLength(gatewayId, "gatewayId is empty");
		Assert.hasLength(who, "who is empty");

		String auditLogType = KronosAuditLog.Gateway.EnableGateway;
		if (!enabled)
			auditLogType = KronosAuditLog.Gateway.DisableGateway;

		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway not found: " + gatewayId);

		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		Assert.notNull(application, "application not found: " + gateway.getApplicationId());

		// change enabled
		gateway.setEnabled(enabled);

		// save
		gateway = gatewayRepository.doSave(gateway, who);

		// clear cache
		getKronosCache().clearGateway(gateway);

		// audit log
		getAuditLogService().save(AuditLogBuilder.create().applicationId(application.getId()).type(auditLogType)
				.productName(ProductSystemNames.KRONOS).objectId(gateway.getId()).by(who)
				.parameter("type", gateway.getType().toString()).parameter("name", gateway.getName())
				.parameter("uid", gateway.getUid()));

		return gateway;
	}

	public Gateway populate(Gateway gateway) {
		if (gateway != null) {

			if (gateway.getRefApplication() == null && !StringUtils.isEmpty(gateway.getApplicationId()))
				gateway.setRefApplication(getCoreCacheService().findApplicationById(gateway.getApplicationId()));

			if (gateway.getRefUser() == null && !StringUtils.isEmpty(gateway.getUserId()))
				gateway.setRefUser(getCoreCacheService().findUserById(gateway.getUserId()));

			if (gateway.getRefDeviceType() == null && !StringUtils.isEmpty(gateway.getDeviceTypeId()))
				gateway.setRefDeviceType(getKronosCache().findDeviceTypeById(gateway.getDeviceTypeId()));

			if (gateway.getRefNode() == null && !StringUtils.isEmpty(gateway.getNodeId()))
				gateway.setRefNode(getKronosCache().findNodeById(gateway.getNodeId()));
		}

		return gateway;
	}

	public Event requestConfigurationUpdate(String gatewayId) {
		String method = "requestConfigurationUpdate";

		Assert.hasText(gatewayId, "gatewayId is empty");
		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is not found");
		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		Assert.notNull(application, "application is not found");

		EventBuilder eventBuilder = EventBuilder.create().applicationId(application.getId())
				.name(AcnEventNames.ServerToGateway.GATEWAY_CONFIGURATION_UPDATE)
				.parameter(EventParameter.InString("gatewayHid", gateway.getHid()));
		AccessKey gatewayOwnerKey = getClientAccessKeyApi().findOwnerKey(gateway.getPri());
		Event event = gatewayCommandService.sendEvent(eventBuilder.build(), gateway.getId(), gatewayOwnerKey);
		logInfo(method, "event has been sent to gatewayId=" + gateway.getId() + ", eventId=" + event.getId());
		return event;
	}

	public Event requestConfigurationRestore(String gatewayId) {
		String method = "requestConfigurationRestore";

		Assert.hasText(gatewayId, "gatewayId is empty");
		Gateway gateway = getKronosCache().findGatewayById(gatewayId);
		Assert.notNull(gateway, "gateway is not found");
		Application application = getCoreCacheService().findApplicationById(gateway.getApplicationId());
		Assert.notNull(application, "application is not found");

		UpdateGatewayModel payload = buildConfigurationRestorePayload(gateway, application);

		EventBuilder eventBuilder = EventBuilder.create().applicationId(application.getId())
				.name(AcnEventNames.ServerToGateway.GATEWAY_CONFIGURATION_RESTORE)
				.parameter(EventParameter.InString("gatewayHid", gateway.getHid()))
				.parameter(EventParameter.InString("payload", JsonUtils.toJson(payload)));

		AccessKey gatewayOwnerKey = getClientAccessKeyApi().findOwnerKey(gateway.getPri());
		Event event = gatewayCommandService.sendEvent(eventBuilder.build(), gateway.getId(), gatewayOwnerKey);
		logInfo(method, "event has been sent to gatewayId=" + gateway.getId() + ", eventId=" + event.getId());
		return event;
	}

	public ConfigBackup backupConfiguration(Gateway gateway, String configBackupName, String who) {
		Assert.notNull(gateway, "gateway is null");
		Assert.hasText(gateway.getId(), "gatewayId is empty");
		Assert.hasText(configBackupName, "configBackupName is empty");
		Assert.hasText(who, "who is empty");

		ConfigBackup configBackup = new ConfigBackup();
		configBackup.setType(ConfigBackup.Type.GATEWAY);
		configBackup.setObjectId(gateway.getId());
		configBackup.setApplicationId(gateway.getApplicationId());
		configBackup.setName(configBackupName);
		configBackup.setGatewayConfig(populateGatewayConfigBackup(gateway));

		configBackup = configBackupService.create(configBackup, who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Gateway.BackupGatewayConfiguration)
				.applicationId(gateway.getApplicationId()).objectId(gateway.getId()).by(who)
				.parameter("name", gateway.getName()).parameter("uid", gateway.getUid())
				.parameter("configBackupId", configBackup.getId()));

		return configBackup;
	}

	public Gateway restoreConfiguration(Gateway gateway, ConfigBackup configBackup, String who) {
		Assert.notNull(gateway, "gateway is null");
		Assert.hasText(gateway.getId(), "gatewayId is empty");
		Assert.notNull(configBackup, "configBackup is null");
		Assert.hasText(configBackup.getObjectId(), "objectId is empty");
		Assert.hasText(who, "who is empty");

		Assert.isTrue(ConfigBackup.Type.GATEWAY.equals(configBackup.getType()), "configBackup type mismatched");
		Assert.isTrue(gateway.getId().equals(configBackup.getObjectId()), "gateway mismatched");

		gateway = update(populateGatewayConfigFromBackup(gateway, configBackup.getGatewayConfig()), who);

		getAuditLogService().save(AuditLogBuilder.create().type(KronosAuditLog.Gateway.RestoreGatewayConfiguration)
				.applicationId(gateway.getApplicationId()).objectId(gateway.getId()).by(who)
				.parameter("name", gateway.getName()).parameter("uid", gateway.getUid())
				.parameter("configBackupId", configBackup.getId()));

		return gateway;
	}

	private GatewayConfigBackup populateGatewayConfigBackup(Gateway gateway) {
		Assert.notNull(gateway, "gateway is null");

		GatewayConfigBackup gatewayConfig = new GatewayConfigBackup();
		gatewayConfig.setConfigurations(gateway.getConfigurations());
		gatewayConfig.setDeviceTypeId(gateway.getDeviceTypeId());
		gatewayConfig.setEnabled(gateway.isEnabled());
		gatewayConfig.setExternalId(gateway.getExternalId());
		gatewayConfig.setIndexTelemetry(gateway.getIndexTelemetry());
		gatewayConfig.setInfo(gateway.getInfo());
		gatewayConfig.setName(gateway.getName());
		gatewayConfig.setNodeId(gateway.getNodeId());
		gatewayConfig.setOsName(gateway.getOsName());
		gatewayConfig.setPersistTelemetry(gateway.getPersistTelemetry());
		gatewayConfig.setProperties(gateway.getProperties());
		gatewayConfig.setSdkVersion(gateway.getSdkVersion());
		gatewayConfig.setSoftwareName(gateway.getSoftwareName());
		gatewayConfig.setSoftwareReleaseId(gateway.getSoftwareReleaseId());
		gatewayConfig.setSoftwareVersion(gateway.getSoftwareVersion());
		gatewayConfig.setTags(gateway.getTags());
		gatewayConfig.setType(gateway.getType());
		gatewayConfig.setUid(gateway.getUid());
		gatewayConfig.setUserId(gateway.getUserId());
		return gatewayConfig;
	}

	private Gateway populateGatewayConfigFromBackup(Gateway gateway, GatewayConfigBackup backup) {
		Assert.notNull(gateway, "gateway is null");
		Assert.notNull(backup, "gatewayConfigBackup is null");

		gateway.setConfigurations(backup.getConfigurations());
		gateway.setEnabled(backup.isEnabled());
		gateway.setExternalId(backup.getExternalId());
		gateway.setIndexTelemetry(backup.getIndexTelemetry());
		gateway.setInfo(backup.getInfo());
		gateway.setPersistTelemetry(backup.getPersistTelemetry());
		gateway.setProperties(backup.getProperties());
		gateway.setSdkVersion(backup.getSdkVersion());
		gateway.setTags(backup.getTags());
		// deviceTypeId
		Assert.hasText(backup.getDeviceTypeId(), "deviceTypeId is empty");
		DeviceType deviceType = getKronosCache().findDeviceTypeById(backup.getDeviceTypeId());
		Assert.notNull(deviceType, "deviceType is not found");
		gateway.setDeviceTypeId(backup.getDeviceTypeId());
		// name
		Assert.hasText(backup.getName(), "name is empty");
		gateway.setName(backup.getName());
		// nodeId
		if (backup.getNodeId() != null) {
			Node node = getKronosCache().findNodeById(backup.getNodeId());
			Assert.notNull(node, "node is not found");
		}
		gateway.setNodeId(backup.getNodeId());
		// osName
		Assert.hasText(backup.getOsName(), "osName is empty");
		gateway.setOsName(backup.getOsName());
		// softwareName
		Assert.hasText(backup.getSoftwareName(), "softwareName is empty");
		gateway.setSoftwareName(backup.getSoftwareName());
		// softwareReleaseId
		if (backup.getSoftwareReleaseId() != null) {
			SoftwareRelease softwareRelease = getRheaClientCacheApi()
					.findSoftwareReleaseById(backup.getSoftwareReleaseId());
			Assert.notNull(softwareRelease, "softwareRelease is not found");
		}
		gateway.setSoftwareReleaseId(backup.getSoftwareReleaseId());
		// softwareVersion
		Assert.hasText(backup.getSoftwareVersion(), "softwareVersion is empty");
		gateway.setSoftwareVersion(backup.getSoftwareVersion());
		// type
		Assert.notNull(backup.getType(), "type is null");
		gateway.setType(backup.getType());
		// uid
		Assert.hasText(backup.getUid(), "uid is null");
		gateway.setUid(backup.getUid());
		// userId
		if (backup.getUserId() != null) {
			User user = getCoreCacheService().findUserById(backup.getUserId());
			Assert.notNull(user, "user is not found");
		}
		gateway.setUserId(backup.getUserId());

		return gateway;
	}

	private UpdateGatewayModel buildConfigurationRestorePayload(Gateway gateway, Application application) {
		UpdateGatewayModel model = new UpdateGatewayModel();
		model.setApplicationHid(application.getHid());
		model.setHid(gateway.getHid());
		model.setName(gateway.getName());
		model.setOsName(gateway.getOsName());
		model.setSdkVersion(gateway.getSdkVersion());
		model.setSoftwareName(gateway.getSoftwareName());
		model.setSoftwareVersion(gateway.getSoftwareVersion());
		model.setType(CreateGatewayModel.GatewayType.valueOf(gateway.getType().name()));
		model.setUid(gateway.getUid());
		if (StringUtils.isNotBlank(gateway.getUserId())) {
			User user = getCoreCacheService().findUserById(gateway.getUserId());
			Assert.notNull(user, "user is not found");
			model.setUserHid(user.getHid());
		}
		return model;
	}

	public void delete(Gateway gateway, String who) {
		String method = "delete";
		Assert.notNull(gateway, "gateway is null");
		Assert.hasLength(who, "who is empty");

		softwareReleaseTransService.deleteBy(gateway, who);
		deviceService.deleteBy(gateway, who);
		awsThingService.deleteBy(gateway);
		azureDeviceService.deleteBy(gateway);
		ibmGatewayService.deleteBy(gateway);
		heartbeatService.deleteBy(HeartbeatObjectType.GATEWAY, gateway.getId());
		lastHeartbeatService.deleteBy(HeartbeatObjectType.GATEWAY, gateway.getId());
		testResultService.deleteBy(gateway, who);
		lastLocationService.deleteBy(LocationObjectType.GATEWAY, gateway.getId());
		configBackupService.deleteBy(gateway, who);
		getClientAccessKeyApi().removePriFromAccessKeys(gateway.getPri(), who);

		// delete
		gatewayRepository.delete(gateway);
		logInfo(method, "Gateway id=" + gateway.getId() + " has been deleted");

		// clear cache
		getKronosCache().clearGateway(gateway);

		// audit log
		getAuditLogService().save(AuditLogBuilder.create().applicationId(gateway.getApplicationId())
				.type(KronosAuditLog.Gateway.DeleteGateway).productName(ProductSystemNames.KRONOS)
				.objectId(gateway.getId()).by(who).parameter("type", gateway.getType().toString())
				.parameter("name", gateway.getName()).parameter("uid", gateway.getUid()));

		gatewayCommandService.deleteMqttSubscriptionQueue(gateway.getHid());
	}

	public Gateway move(Gateway gateway, Application toApplication, Map<String, DeviceType> mappedDeviceTypes,
			Map<String, DeviceActionType> mappedDeviceActionTypes, Map<String, TestProcedure> mappedTestProcedures,
			String who) {
		String method = "move";
		Assert.notNull(gateway, "gateway is null");
		Assert.notNull(toApplication, "toApplication is null");
		Assert.hasLength(who, "who is empty");

		deviceService.moveBy(gateway, toApplication, mappedDeviceTypes, mappedDeviceActionTypes, mappedTestProcedures,
				who);
		testResultService.moveBy(gateway, toApplication, mappedTestProcedures, who);
		getClientAccessKeyApi().removePriFromAccessKeys(gateway.getPri(), who);

		// update this gateway
		if (gateway.getDeviceTypeId() != null) {
			DeviceType deviceType = mappedDeviceTypes.get(gateway.getDeviceTypeId());
			Assert.notNull(deviceType, "failed to map device type of gateway to the one in target application");
			gateway.setDeviceTypeId(deviceType.getId());
		}
		gateway.setUserId(null);
		gateway.setNodeId(null);
		String fromApplicationId = gateway.getApplicationId();
		gateway.setApplicationId(toApplication.getId());

		gateway = this.update(gateway, who);
		logInfo(method, "Gateway id=%s has been moved from application id=%s to application id=%s", gateway.getId(),
				fromApplicationId, toApplication.getId());

		// create ownerKey
		AccessKey ownerAccessKey = getClientAccessKeyApi().create(toApplication.getCompanyId(), toApplication.getId(),
				Collections.singletonList(new AccessPrivilege(gateway.getPri(), AccessLevel.OWNER)), who);
		logInfo(method, "created owner accessKey: %s", ownerAccessKey.getId());

		getAuditLogService().save(
				AuditLogBuilder.create().applicationId(fromApplicationId).type(KronosAuditLog.Gateway.MoveGatewayOut)
						.productName(ProductSystemNames.KRONOS).objectId(gateway.getId()).by(who)
						.parameter("type", gateway.getType().toString()).parameter("name", gateway.getName())
						.parameter("uid", gateway.getUid()).parameter("toApplicationId", toApplication.getId()));
		getAuditLogService().save(AuditLogBuilder.create().applicationId(gateway.getApplicationId())
				.type(KronosAuditLog.Gateway.MoveGatewayIn).productName(ProductSystemNames.KRONOS)
				.objectId(gateway.getId()).by(who).parameter("type", gateway.getType().toString())
				.parameter("name", gateway.getName()).parameter("uid", gateway.getUid())
				.parameter("fromApplicationId", fromApplicationId));

		return gateway;
	}

	public Long deleteByApplicationId(String applicationId, String who) {
		String method = "deleteByApplicationId";
		Assert.hasText(applicationId, "applicationId is empty");
		Assert.hasText(who, "who is empty");
		logInfo(method, "applicationId: %s, who: %s", applicationId, who);
		return gatewayRepository.deleteByApplicationId(applicationId);
	}
}
