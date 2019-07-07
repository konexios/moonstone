package com.arrow.pegasus.client.api;

import java.net.URI;

import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.acs.JsonUtils;
import com.arrow.pegasus.data.MiramontiTenant;

@Component
public class ClientMiramontiApi extends ClientApiAbstract {
	private static final String MIRAMONTI_ROOT_URL = WEB_SERVICE_ROOT_URL + "/miramonti";

	public MiramontiTenant createCompany(String number, String zoneId, String applicationEngineId) {
		String method = "createCompany";
		Assert.hasText(number, "number is empty");
		logInfo(method, "number: %s", number);
		try {
			URI uri = buildUri(String.format("%s/%s/%s/%s", MIRAMONTI_ROOT_URL, number, zoneId, applicationEngineId));
			MiramontiTenant result = execute(new HttpPost(uri), "", MiramontiTenant.class);
			if (result != null && isDebugEnabled())
				logDebug(method, "result: ", JsonUtils.toJson(result));
			return result;
		} catch (Throwable e) {
			throw handleException(e);
		}
	}
}
