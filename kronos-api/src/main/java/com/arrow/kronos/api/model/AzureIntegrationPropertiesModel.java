package com.arrow.kronos.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AzureIntegrationPropertiesModel implements Serializable {

	private static final long serialVersionUID = 332870097010078902L;

	private String accessPolicyName;
	private String hostName;
	private String accessKeyName;
	private String accessKey;
	private List<String> gatewayHids = new ArrayList<>();

	public String getAccessPolicyName() {
		return accessPolicyName;
	}

	public void setAccessPolicyName(String accessPolicyName) {
		this.accessPolicyName = accessPolicyName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getAccessKeyName() {
		return accessKeyName;
	}

	public void setAccessKeyName(String accessKeyName) {
		this.accessKeyName = accessKeyName;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public List<String> getGatewayHids() {
		return gatewayHids;
	}

	public void setGatewayHids(List<String> gatewayHids) {
		this.gatewayHids = gatewayHids;
	}
}
