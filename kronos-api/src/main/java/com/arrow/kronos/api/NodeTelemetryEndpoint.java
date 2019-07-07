package com.arrow.kronos.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.Node;

public class NodeTelemetryEndpoint extends TelemetryEndpointAbstract<NodeTelemetryEndpoint> {

	private final static long ACTIVE_DEVICES_MONITOR_INTERVAL_SECS = 60;

	private ScheduledThreadPoolExecutor activeDevicesMonitor;

	@Override
	public NodeTelemetryEndpoint self() {
		return this;
	}

	@Override
	public void init() {
		super.init();
		startActiveDevicesMonitorThread();
	}

	@Override
	public void preDestroy() {
		super.preDestroy();
		if (activeDevicesMonitor != null) {
			activeDevicesMonitor.shutdownNow();
		}
	}

	private void startActiveDevicesMonitorThread() {
		activeDevicesMonitor = new ScheduledThreadPoolExecutor(1);
		activeDevicesMonitor.scheduleWithFixedDelay(() -> {
			String method = "activeDevicesMonitorThread";
			getLogger().logDebug(method, "...");
			try {
				for (SessionWorker worker : sessions.values()) {
					if (StringUtils.isNotEmpty(worker.getNodeId())) {
						List<Device> devices = getDeviceService().getDeviceRepository()
						        .findAllByNodeIdAndEnabled(worker.getNodeId(), true);
						worker.updateSubscription(devices);
					}
				}
			} catch (Exception e) {
				getLogger().logError(method, e);
			}
		}, 0, ACTIVE_DEVICES_MONITOR_INTERVAL_SECS, TimeUnit.SECONDS);
	}

	@Override
	public void onOpen(Session session, EndpointConfig endpointConfig) {
		String method = "onOpen";
		getLogger().logDebug(method, "...");
		getLogger().logDebug(method, "%s", session.getPathParameters());

		Map<String, String> pathParams = session.getPathParameters();
		Assert.notNull(pathParams, "pathParams is null");

		String hid = pathParams.get("hid");
		Assert.hasText(hid, "node hid is empty");
		getLogger().logInfo(method, "hid: %s", hid);

		String telemetryName = pathParams.get("telemetryName");
		Assert.hasText(telemetryName, "telemetry name is empty");
		getLogger().logDebug(method, "hid: %s, telemetryName: %s", hid, telemetryName);

		Node node = getKronosCache().findNodeByHid(hid);
		Assert.notNull(node, "node is not found");

		validateCanReadNode(session, node);

		List<Device> devices = getDeviceService().getDeviceRepository().findAllByNodeIdAndEnabled(node.getId(), true);
		SessionWorker sessionWorker = new SessionWorker(session, telemetryName, node.getId());
		sessionWorker.createSubscription(devices);
	}
}
