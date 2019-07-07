package com.arrow.kronos.data;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.Enabled;
import com.arrow.pegasus.data.GatewayType;

@Document(collection = "gateway")
public class Gateway extends BaseDeviceAbstract implements Enabled {
	private static final long serialVersionUID = 6805516186647172743L;

	private final static GatewayType DEFAULT_GATEWAY_TYPE = GatewayType.Local;

	@NotNull
	private GatewayType type = DEFAULT_GATEWAY_TYPE;
	@NotBlank
	private String osName;
	private String sdkVersion;
	private List<ConfigurationProperty> configurations = new ArrayList<>();

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public List<ConfigurationProperty> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(List<ConfigurationProperty> configurations) {
		this.configurations = configurations;
	}

	public GatewayType getType() {
		return type;
	}

	public void setType(GatewayType type) {
		this.type = type;
	}

	public String getSdkVersion() {
		return sdkVersion;
	}

	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	@Override
	protected String getProductPri() {
		return CoreConstant.PegasusPri.BASE;
	}

	@Override
	protected String getTypePri() {
		return CoreConstant.PegasusPri.GATEWAY;
	}
}
