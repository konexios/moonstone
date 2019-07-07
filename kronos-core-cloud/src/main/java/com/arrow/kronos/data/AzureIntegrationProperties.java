package com.arrow.kronos.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AzureIntegrationProperties implements Serializable {

	private static final long serialVersionUID = -7635416155278580667L;

	private String accessPolicyName;
	@NotBlank
	private String hostName;
	@NotBlank
	private String accessKeyName;
	@NotBlank
	private String accessKey;
	private List<String> gatewayIds = new ArrayList<>();

	@Transient
	@JsonIgnore
	private SocialEventRegistration refSocialEventRegistration;

	public String getAccessPolicyName() {
		return accessPolicyName;
	}

	public void setAccessPolicyName(String accessPolicyName) {
		this.accessPolicyName = accessPolicyName;
	}

	public List<String> getGatewayIds() {
		return gatewayIds;
	}

	public void setGatewayIds(List<String> gatewayIds) {
		this.gatewayIds = gatewayIds;
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

	public SocialEventRegistration getRefSocialEventRegistration() {
		return refSocialEventRegistration;
	}

	public void setRefSocialEventRegistration(SocialEventRegistration refSocialEventRegistration) {
		this.refSocialEventRegistration = refSocialEventRegistration;
	}
}
