package com.arrow.kronos.repo;

import java.util.Set;

public class AzureDeviceSearchParams extends KronosDocumentSearchParams {

	private static final long serialVersionUID = -8993038816974278651L;

	private Set<String> azureAccountIds;
	private Set<String> gatewayIds;

	public Set<String> getAzureAccountIds() {
		return azureAccountIds;
	}

	public AzureDeviceSearchParams addAzureAccountIds(String... azureAccountIds) {
		this.azureAccountIds = addValues(this.azureAccountIds, azureAccountIds);
		return this;
	}

	public Set<String> getGatewayIds() {
		return gatewayIds;
	}

	public AzureDeviceSearchParams addGatewayIds(String... gatewayIds) {
		this.gatewayIds = addValues(this.gatewayIds, gatewayIds);
		return this;
	}
}
