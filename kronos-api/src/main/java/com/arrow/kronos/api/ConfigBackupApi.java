package com.arrow.kronos.api;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arrow.kronos.data.BaseDeviceConfigBackup;
import com.arrow.kronos.data.ConfigBackup;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceConfigBackup;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.GatewayConfigBackup;
import com.arrow.kronos.data.Node;
import com.arrow.kronos.service.ConfigBackupService;
import com.arrow.kronos.util.ApiUtil;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.profile.User;
import com.arrow.rhea.data.SoftwareRelease;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import moonstone.acn.client.model.BaseDeviceConfigBackupModel;
import moonstone.acn.client.model.ConfigBackupModel;
import moonstone.acn.client.model.DeviceConfigBackupModel;
import moonstone.acn.client.model.GatewayConfigBackupModel;
import moonstone.acn.client.model.GatewayModel;
import moonstone.acs.AcsLogicalException;
import moonstone.acs.client.model.ConfigurationPropertyModel;

@RestController
@RequestMapping("/api/v1/kronos/config-backups")
public class ConfigBackupApi extends BaseApiAbstract {

	@Autowired
	private ConfigBackupService configBackupService;
	@Autowired
	private ApiUtil apiUtil;

	@ApiOperation(value = "find configuration backup")
	@RequestMapping(path = "/{hid}", method = RequestMethod.GET)
	public ConfigBackupModel findByHid(
			@ApiParam(value = "config backup hid", required = true) @PathVariable(value = "hid") String hid) {

		ConfigBackup configBackup = configBackupService.getConfigBackupRepository().doFindByHid(hid);

		ConfigBackupModel result = buildConfigBackupModel(configBackup);
		switch (configBackup.getType()) {
		case DEVICE:
			Device device = getKronosCache().findDeviceById(configBackup.getObjectId());
			Assert.notNull(device, "device is not found");
			validateCanReadDevice(device.getHid());
			result.setDeviceConfig(buildDeviceConfigBackupModel(configBackup.getDeviceConfig()));
			result.setObjectHid(device.getHid());
			break;
		case GATEWAY:
			Gateway gateway = getKronosCache().findGatewayById(configBackup.getObjectId());
			Assert.notNull(gateway, "gateway is not found");
			validateCanReadGateway(gateway.getHid());
			result.setGatewayConfig(buildGatewayConfigBackupModel(configBackup.getGatewayConfig()));
			result.setObjectHid(gateway.getHid());
			break;
		default:
			throw new AcsLogicalException("Unsupported ConfigBackup type");
		}
		return result.withHid(configBackup.getHid()).withName(configBackup.getName())
				.withType(ConfigBackupModel.Type.valueOf(configBackup.getType().name()))
				.withCreatedDate(configBackup.getCreatedDate());
	}

	private DeviceConfigBackupModel buildDeviceConfigBackupModel(DeviceConfigBackup deviceConfig) {
		if (deviceConfig == null) {
			return null;
		}
		DeviceConfigBackupModel result = populateBaseModel(new DeviceConfigBackupModel(), deviceConfig);
		if (StringUtils.isNotBlank(deviceConfig.getGatewayId())) {
			Gateway gateway = getKronosCache().findGatewayById(deviceConfig.getGatewayId());
			Assert.notNull(gateway, "gateway is not found");
			result.setGatewayHid(gateway.getHid());
		}
		result.withActions(apiUtil.buildDeviceActionModelList(deviceConfig.getActions()));
		return result;
	}

	private GatewayConfigBackupModel buildGatewayConfigBackupModel(GatewayConfigBackup gatewayConfig) {
		if (gatewayConfig == null) {
			return null;
		}
		GatewayConfigBackupModel result = populateBaseModel(new GatewayConfigBackupModel(), gatewayConfig)
				.withType(GatewayModel.GatewayType.valueOf(gatewayConfig.getType().name()))
				.withOsName(gatewayConfig.getOsName()).withSdkVersion(gatewayConfig.getSdkVersion());
		if (gatewayConfig.getConfigurations() != null) {
			result.withConfigurations(gatewayConfig.getConfigurations().stream()
					.map(this::buildConfigurationPropertyModel).collect(Collectors.toCollection(ArrayList::new)));
		}
		return result;
	}

	private <T extends BaseDeviceConfigBackupModel> T populateBaseModel(T model, BaseDeviceConfigBackup backup) {
		Assert.notNull(model, "model is null");
		Assert.notNull(backup, "backup is null");

		if (StringUtils.isNotBlank(backup.getDeviceTypeId())) {
			DeviceType deviceType = getKronosCache().findDeviceTypeById(backup.getDeviceTypeId());
			Assert.notNull(deviceType, "deviceType is not found");
			model.withDeviceTypeHid(deviceType.getHid());
		}
		if (StringUtils.isNotBlank(backup.getNodeId())) {
			Node node = getKronosCache().findNodeById(backup.getNodeId());
			Assert.notNull(node, "node is not found");
			model.withNodeHid(node.getHid());
		}
		if (StringUtils.isNotBlank(backup.getSoftwareReleaseId())) {
			SoftwareRelease softwareRelease = configBackupService.getRheaClientCacheApi()
					.findSoftwareReleaseById(backup.getSoftwareReleaseId());
			Assert.notNull(softwareRelease, "softwareRelease is not found");
			model.withSoftwareRelaseHid(softwareRelease.getHid());
		}
		if (StringUtils.isNoneBlank(backup.getUserId())) {
			User user = getCoreCacheService().findUserById(backup.getUserId());
			Assert.notNull(user, "user is not found");
			model.withUserHid(user.getHid());
		}
		model.withEnabled(backup.isEnabled()).withExternalId(backup.getExternalId())
				.withIndexTelemetry(backup.getIndexTelemetry()).withInfo(backup.getInfo()).withName(backup.getName())
				.withPersistTelemetry(backup.getPersistTelemetry()).withProperties(backup.getProperties())
				.withTags(backup.getTags()).withUid(backup.getUid()).withSoftwareName(backup.getSoftwareName())
				.withSoftwareVersion(backup.getSoftwareVersion());
		return model;
	}

	private ConfigurationPropertyModel buildConfigurationPropertyModel(ConfigurationProperty property) {
		if (property == null) {
			return null;
		}
		ConfigurationPropertyModel result = new ConfigurationPropertyModel().withJsonClass(property.getJsonClass())
				.withName(property.getName()).withValue(property.getValue());
		if (property.getCategory() != null) {
			result.withCategory(
					ConfigurationPropertyModel.ConfigurationPropertyCategory.valueOf(property.getCategory().name()));
		}
		if (property.getDataType() != null) {
			result.withDataType(
					ConfigurationPropertyModel.ConfigurationPropertyDataType.valueOf(property.getDataType().name()));
		}
		return result;
	}
}
