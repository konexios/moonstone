package com.arrow.kronos.data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.kronos.KronosConstants;
import com.arrow.pegasus.data.TsDocumentAbstract;

@Document(collection = "config_backup")
public class ConfigBackup extends TsDocumentAbstract {
	private static final long serialVersionUID = 8360142104507358485L;

	public enum Type {
		DEVICE, GATEWAY
	}

	@NotBlank
	private String name;
	@NotNull
	private Type type;
	@NotBlank
	private String objectId;
	private String applicationId;

	private DeviceConfigBackup deviceConfig;
	private GatewayConfigBackup gatewayConfig;

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public DeviceConfigBackup getDeviceConfig() {
		return deviceConfig;
	}

	public void setDeviceConfig(DeviceConfigBackup deviceConfig) {
		this.deviceConfig = deviceConfig;
	}

	public GatewayConfigBackup getGatewayConfig() {
		return gatewayConfig;
	}

	public void setGatewayConfig(GatewayConfigBackup gatewayConfig) {
		this.gatewayConfig = gatewayConfig;
	}

	@Override
	protected String getProductPri() {
		return KronosConstants.KRONOS_PRI;
	}

	@Override
	protected String getTypePri() {
		return KronosConstants.KronosPri.CONFIG_BACKUP;
	}
}
