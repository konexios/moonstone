package com.arrow.pegasus.client.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import com.arrow.pegasus.data.AccessPrivilege;

public class AccessKeyCreateModel implements Serializable {
	private static final long serialVersionUID = -7063927240633225555L;
	private String name;
	private String companyId;
	private String subscriptionId;
	private String applicationId;
	private String apiKey;
	private String secretKey;
	private Instant expiration;
	private List<AccessPrivilege> privileges;

	public AccessKeyCreateModel() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Instant getExpiration() {
		return expiration;
	}

	public void setExpiration(Instant expiration) {
		this.expiration = expiration;
	}

	public List<AccessPrivilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<AccessPrivilege> privileges) {
		this.privileges = privileges;
	}

}
