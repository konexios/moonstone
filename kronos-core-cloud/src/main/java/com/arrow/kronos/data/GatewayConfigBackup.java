package com.arrow.kronos.data;

import java.util.ArrayList;
import java.util.List;

import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.GatewayType;

public class GatewayConfigBackup extends BaseDeviceConfigBackup {
	private static final long serialVersionUID = 9154646326243657690L;

	private GatewayType type;
	private String osName;
	private String sdkVersion;
	private List<ConfigurationProperty> configurations = new ArrayList<>();

	public GatewayType getType() {
		return type;
	}

	public void setType(GatewayType type) {
		this.type = type;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public List<ConfigurationProperty> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<ConfigurationProperty> configurations) {
		this.configurations = configurations;
	}
}