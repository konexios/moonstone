package com.arrow.pegasus.api;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.arrow.pegasus.CoreConstant;
import com.arrow.pegasus.EndpointAbstract;
import com.arrow.pegasus.NotAuthorizedException;
import com.arrow.pegasus.data.AccessKey;
import com.arrow.pegasus.service.HeartbeatService;
import com.arrow.pegasus.service.LastHeartbeatService;
import com.arrow.pegasus.util.ApiConfigurationPropertyUtil;

public abstract class ApiAbstract extends EndpointAbstract {

	@Autowired
	private ApiConfigurationPropertyUtil apiConfig;
	@Autowired
	private HeartbeatService heartbeatService;
	@Autowired
	private LastHeartbeatService lastHeartbeatService;

	protected AccessKey getAccessKey() {
		String method = "getAccessKey";
		AccessKey accessKey = (AccessKey) RequestContextHolder.getRequestAttributes()
		        .getAttribute(CoreConstant.API_KEY_CONTEXT, RequestAttributes.SCOPE_REQUEST);
		Assert.isTrue(accessKey != null && accessKey.getExpiration().isAfter(Instant.now()), "accessKey is expired!");

		accessKey = getCoreCacheHelper().populateAccessKey(accessKey);
		getCoreCacheHelper().populateApplication(accessKey.getRefApplication());

		logDebug(method, "accessKey: %s", accessKey.getId());
		return accessKey;
	}

	protected AccessKey getValidatedAccessKey(String productSystemName) {
		AccessKey accessKey = getAccessKey();
		if (!validateProduct(accessKey.getApplicationId(), productSystemName)) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected AccessKey validateCanReadApplication(String productSystemName) {
		AccessKey accessKey = getValidatedAccessKey(productSystemName);
		if (!accessKey.canRead(accessKey.getRefApplication())) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected AccessKey validateCanWriteApplication(String productSystemName) {
		AccessKey accessKey = getValidatedAccessKey(productSystemName);
		if (!accessKey.canWrite(accessKey.getRefApplication())) {
			throw new NotAuthorizedException();
		}
		return accessKey;
	}

	protected String getApiPayload() {
		return (String) RequestContextHolder.getRequestAttributes().getAttribute(CoreConstant.API_PAYLOAD,
		        RequestAttributes.SCOPE_REQUEST);
	}

	protected ApiConfigurationPropertyUtil getApiConfig() {
		return apiConfig;
	}

	protected HeartbeatService getHeartbeatService() {
		return heartbeatService;
	}

	protected LastHeartbeatService getLastHeartbeatService() {
		return lastHeartbeatService;
	}
}