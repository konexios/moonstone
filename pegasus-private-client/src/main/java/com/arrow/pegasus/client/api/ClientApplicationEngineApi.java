package com.arrow.pegasus.client.api;

import java.net.URI;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;

import com.arrow.acs.JsonUtils;
import com.arrow.pegasus.client.model.ApplicationEngineChangeModel;
import com.arrow.pegasus.data.ApplicationEngine;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class ClientApplicationEngineApi extends ClientApiAbstract {
	private static final String APPLICATION_ENGINES_ROOT_URL = WEB_SERVICE_ROOT_URL + "/applicationEngines";

	public List<ApplicationEngine> findAllByProductId(String productId) {
		try {
			URI uri = buildUri(APPLICATION_ENGINES_ROOT_URL + "/products/" + productId);
			List<ApplicationEngine> result = execute(new HttpGet(uri), new TypeReference<List<ApplicationEngine>>() {
			});
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public ApplicationEngine findByName(String name) {
		String method = "findByName";
		try {
			URI uri = buildUri(APPLICATION_ENGINES_ROOT_URL + "/names/" + name);
			ApplicationEngine result = execute(new HttpGet(uri), ApplicationEngine.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %hid, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public ApplicationEngine findById(String id) {
		String method = "findById";
		try {
			URI uri = buildUri(APPLICATION_ENGINES_ROOT_URL + "/ids/" + id);
			ApplicationEngine result = execute(new HttpGet(uri), ApplicationEngine.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %hid, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public ApplicationEngine create(ApplicationEngine applicationEngine, String who) {
		String method = "create";
		try {
			URI uri = buildUri(APPLICATION_ENGINES_ROOT_URL);
			ApplicationEngineChangeModel model = new ApplicationEngineChangeModel()
			        .withApplicationEngine(applicationEngine).withWho(who);
			ApplicationEngine result = execute(new HttpPost(uri), JsonUtils.toJson(model), ApplicationEngine.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %hid, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}
}
