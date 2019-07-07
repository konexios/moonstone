package com.arrow.pegasus.data.security;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class AuthLdap implements Serializable {
	private static final long serialVersionUID = -9205875764417321046L;

	@NotBlank
	private String applicationId;
	private String domain;
	private String url;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
