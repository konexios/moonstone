package com.arrow.kronos.api;

import java.util.Map;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import org.springframework.util.Assert;

import com.arrow.kronos.data.Device;

public class DeviceTelemetryEndpoint extends TelemetryEndpointAbstract<DeviceTelemetryEndpoint> {

	@Override
	public DeviceTelemetryEndpoint self() {
		return this;
	}

	@Override
	public void onOpen(Session session, EndpointConfig endpointConfig) {
		String method = "onOpen";

		Map<String, String> pathParams = session.getPathParameters();
		Assert.notNull(pathParams, "pathParams is null");

		String hid = pathParams.get("hid");
		Assert.hasText(hid, "empty device hid");
		getLogger().logInfo(method, "hid: %s", hid);

		String telemetryName = pathParams.get("telemetryName");
		getLogger().logInfo(method, "telemetryName: %s", telemetryName);

		Device device = getKronosCache().findDeviceByHid(hid);
		Assert.notNull(device, "device not found");
		getLogger().logInfo(method, "deviceId: %s", device.getId());

		validateCanReadDevice(session, device);

		SessionWorker sessionWorker = new SessionWorker(session, telemetryName);
		sessionWorker.createSubscription(device);
	}
}
