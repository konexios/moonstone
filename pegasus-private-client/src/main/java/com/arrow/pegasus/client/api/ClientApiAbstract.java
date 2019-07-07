package com.arrow.pegasus.client.api;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import com.arrow.acs.AcsRuntimeException;
import com.arrow.acs.client.api.ApiAbstract;
import com.arrow.acs.client.api.ApiConfig;

public abstract class ClientApiAbstract extends ApiAbstract {
	protected static final String WEB_SERVICE_ROOT_URL = "/api/v1/local/pegasus";

	@Value("${pegasus-local-api.url}")
	private String url;

	@PostConstruct
	public void init() {
		if (StringUtils.isEmpty(url)) {
			throw new AcsRuntimeException("configuration not found: pegasus-local-api.url");
		}
		setApiConfig(new ApiConfig().withBaseUrl(url).withApiKey("none").withSecretkey("none"));
	}
}