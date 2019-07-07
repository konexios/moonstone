package com.arrow.pegasus.client.api;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.arrow.pegasus.data.SocialEvent;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class ClientSocialEventApi extends ClientApiAbstract {

	private static final String SOCIAL_EVENTS_ROOT_URL = WEB_SERVICE_ROOT_URL + "/social-events";

	public SocialEvent findById(String id) {
		Assert.hasText(id, "id is empty");
		String method = "findById";
		try {
			URI uri = buildUri(SOCIAL_EVENTS_ROOT_URL + "/ids/" + id);
			SocialEvent socialEvent = execute(new HttpGet(uri), SocialEvent.class);
			if (socialEvent != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", socialEvent.getId(), socialEvent.getHid(),
				        socialEvent.getName());
			return socialEvent;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SocialEvent findByHid(String hid) {
		Assert.hasText(hid, "hid is empty");
		String method = "findByHid";
		try {
			URI uri = buildUri(SOCIAL_EVENTS_ROOT_URL + "/hids/" + hid);
			SocialEvent socialEvent = execute(new HttpGet(uri), SocialEvent.class);
			if (socialEvent != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", socialEvent.getId(), socialEvent.getHid(),
				        socialEvent.getName());
			return socialEvent;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public SocialEvent findByName(String name) {
		Assert.hasText(name, "name is empty");
		String method = "findByName";
		try {
			URI uri = buildUri(SOCIAL_EVENTS_ROOT_URL + "/names/" + name);
			SocialEvent socialEvent = execute(new HttpGet(uri), SocialEvent.class);
			if (socialEvent != null && isDebugEnabled())
				logDebug(method, "id: %s, hid: %s, name: %s", socialEvent.getId(), socialEvent.getHid(),
				        socialEvent.getName());
			return socialEvent;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public List<SocialEvent> findByDate(Instant date) {
		return findBy(date, null, null, null);
	}

	public List<SocialEvent> findBy(Instant date, String zoneId, String sortField, String sortDirection) {
		try {
			URIBuilder uriBuilder = new URIBuilder(buildUri(SOCIAL_EVENTS_ROOT_URL));
			if (date != null) {
				uriBuilder.addParameter("date", date.toString());
			}
			if (StringUtils.hasText(zoneId)) {
				uriBuilder.addParameter("zoneId", zoneId);
			}
			if (StringUtils.hasText(sortField)) {
				uriBuilder.addParameter("sortField", sortField);
			}
			if (StringUtils.hasText(sortDirection)) {
				uriBuilder.addParameter("sortDirection", sortDirection);
			}
			URI uri = uriBuilder.build();
			return execute(new HttpGet(uri), new TypeReference<List<SocialEvent>>() {
			});
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}
