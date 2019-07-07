package com.arrow.pegasus.client.api;

import java.net.URI;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

import com.arrow.pegasus.data.security.Auth;

@Component
public class ClientAuthApi extends ClientApiAbstract {
	private static final String AUTHS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/auths";

	public Auth findBySamlIdp(String remoteEntityId) {
		String method = "findBySamlIdp";
		try {
			URI uri = new URIBuilder(buildUri(AUTHS_ROOT_URL)).addParameter("samlIdp", remoteEntityId).build();
			Auth result = execute(new HttpGet(uri), Auth.class);
			logDebug(method, "id: %s, hid: %s", result.getId(), result.getHid());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}
}
