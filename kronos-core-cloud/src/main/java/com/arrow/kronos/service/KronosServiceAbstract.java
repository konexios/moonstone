package com.arrow.kronos.service;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.arrow.pegasus.client.api.ClientAccessKeyApi;
import com.arrow.pegasus.service.BaseServiceAbstract;
import com.arrow.pegasus.service.PlatformConfigService;
import com.arrow.rhea.client.api.ClientCacheApi;

public abstract class KronosServiceAbstract extends BaseServiceAbstract {

	@Autowired
	private KronosCache kronosCache;
	@Autowired
	private ClientAccessKeyApi clientAccessKeyApi;
	@Autowired
	private ClientCacheApi rheaClientCacheApi;
	@Autowired
	private PlatformConfigService platformConfigService;

	protected KronosCache getKronosCache() {
		return kronosCache;
	}

	protected PlatformConfigService getPlatformConfigService() {
		return platformConfigService;
	}

	public ClientAccessKeyApi getClientAccessKeyApi() {
		return clientAccessKeyApi;
	}

	public ClientCacheApi getRheaClientCacheApi() {
		return rheaClientCacheApi;
	}

	@SuppressWarnings("unchecked")
	protected <T> T doRequestCache(String id, Function<String, String> keyFunction,
	        Function<String, T> lookupFunction) {
		String key = keyFunction.apply(id);
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		T result = (T) attrs.getAttribute(key, RequestAttributes.SCOPE_REQUEST);
		if (result == null) {
			result = lookupFunction.apply(id);
			attrs.setAttribute(key, result, RequestAttributes.SCOPE_REQUEST);
		}
		return result;
	}
}