package com.arrow.pegasus.client.api;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

import com.arrow.pegasus.client.model.CreateUpdateModel;
import com.arrow.pegasus.data.profile.Subscription;
import com.fasterxml.jackson.core.type.TypeReference;

import moonstone.acs.JsonUtils;

@Component
public class ClientSubscriptionApi extends ClientApiAbstract {
	private static final String SUBSCRIPTIONS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/subscriptions";

	public List<Subscription> findAll() {
		try {
			URI uri = buildUri(SUBSCRIPTIONS_ROOT_URL);
			List<Subscription> result = execute(new HttpGet(uri), new TypeReference<List<Subscription>>() {
			});
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Subscription create(Subscription subscription, String who) {
		String method = "create";
		try {
			URI uri = buildUri(SUBSCRIPTIONS_ROOT_URL);
			CreateUpdateModel<Subscription> model = new CreateUpdateModel<Subscription>().withModel(subscription)
			        .withWho(who);
			Subscription result = execute(new HttpPost(uri), JsonUtils.toJson(model), Subscription.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Subscription update(Subscription subscription, String who) {
		String method = "update";
		try {
			URI uri = buildUri(SUBSCRIPTIONS_ROOT_URL);
			CreateUpdateModel<Subscription> model = new CreateUpdateModel<Subscription>().withModel(subscription)
			        .withWho(who);
			Subscription result = execute(new HttpPut(uri), JsonUtils.toJson(model), Subscription.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Subscription findById(String id) {
		String method = "findById";
		try {
			URI uri = buildUri(SUBSCRIPTIONS_ROOT_URL + "/ids/" + id);
			Subscription result = execute(new HttpGet(uri), Subscription.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Subscription findByHid(String hid) {
		String method = "findByHid";
		try {
			URI uri = buildUri(SUBSCRIPTIONS_ROOT_URL + "/hids/" + hid);
			Subscription result = execute(new HttpGet(uri), Subscription.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", result.getId(), result.getHid(), result.getName());
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public long getApplicationCount(String subscriptionId, Instant createdBefore, Boolean enabled) {
		try {
			URIBuilder uriBuilder = new URIBuilder(
			        buildUri(SUBSCRIPTIONS_ROOT_URL + "/" + subscriptionId + "/applications/count"));
			if (createdBefore != null) {
				uriBuilder.addParameter("createdBefore", createdBefore.toString());
			}
			if (enabled != null) {
				uriBuilder.addParameter("enabled", enabled.toString());
			}
			return execute(new HttpGet(uriBuilder.build()), Long.class);
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	public Long delete(String id) {
		String method = "delete";
		try {
			URI uri = buildUri(SUBSCRIPTIONS_ROOT_URL + "/" + id);
			Long result = execute(new HttpDelete(uri), Long.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: %d", result);
			return result;
		} catch (Throwable t) {
			throw handleException(t);
		}
	}
}
