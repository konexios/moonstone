package com.arrow.kronos.web.websocket;

import java.time.Instant;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.arrow.acs.AcsLogicalException;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.EsTelemetryItem;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.repo.SpringDataEsTelemetryItemRepository;
import com.arrow.kronos.service.GatewayService;
import com.arrow.kronos.web.TelemetryListener;
import com.arrow.kronos.web.model.DeviceWsModels;
import com.arrow.kronos.web.model.TelemetryItemModels.TelemetryItemChartModel;

@Controller
public class DeviceWsApi extends WsApiAbstract {

	@Autowired
	private GatewayService gatewayService;

	@Autowired
	private SpringDataEsTelemetryItemRepository esTelemetryItemRepository;

	@Value("${com.arrow.kronos.deviceWsApi.fromBufferSeconds:120}")
	private int fromBufferSeconds;
	@Value("${com.arrow.kronos.deviceWsApi.toBufferSeconds:120}")
	private int toBufferSeconds;

	@Autowired
	private TelemetryListener telemetryListener;

	@MessageMapping("/api/kronos/ws/device/{deviceId}/telemetry/{telemetryName}")
	public void getTelemetryData(@DestinationVariable String deviceId, @DestinationVariable String telemetryName,
	        DeviceWsModels.DeviceTelemetryOptions options, StompHeaderAccessor headerAccessor) {
		String method = "getTelemetryData";

		Assert.notNull(deviceId, "deviceId is null");
		Assert.notNull(telemetryName, "telemetryName is null");
		Assert.notNull(options, "options is null");
		int refreshInterval = options.getRefreshInterval();

		String userSessionId = headerAccessor.getSessionId();
		String subscriptionId = headerAccessor.getFirstNativeHeader("subscriptionId");
		Assert.notNull(subscriptionId, "subscriptionId is null");
		logDebug(method, "subscriptionId: %s, telemetryName: %s", subscriptionId, telemetryName);

		// sanity check
		if (StringUtils.isEmpty(subscriptionId) || StringUtils.isEmpty(telemetryName)) {
			return;
		}

		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");
		if (!hasAuthority("KRONOS_VIEW_ALL_DEVICES", headerAccessor)) {
			Assert.isTrue(getAuthenticatedUser(headerAccessor).getId().equals(device.getUserId()),
			        "user must own the device");
		}
		Assert.isTrue(getApplicationId(headerAccessor).equals(device.getApplicationId()),
		        "user and device must have the same application id");

		String destination = String.format("/queue/device/%s/telemetry/%s", deviceId, telemetryName);
		String refreshIntervalKey = String.format("kronos.ws.subscription.%s.refresh.interval", subscriptionId);
		logDebug(method, "refreshIntervalKey: %s", headerAccessor.getSessionAttributes().get(refreshIntervalKey));
		if (headerAccessor.getSessionAttributes().containsKey(refreshIntervalKey)) {
			headerAccessor.getSessionAttributes().put(refreshIntervalKey, refreshInterval);
			return;
		}
		headerAccessor.getSessionAttributes().put(refreshIntervalKey, refreshInterval);
		long to = 0, from = 0, now = 0;
		while (isSubscribed(subscriptionId) && headerAccessor.getSessionAttributes().containsKey(refreshIntervalKey)
		        && (refreshInterval = (int) headerAccessor.getSessionAttributes().get(refreshIntervalKey)) > 0) {

			now = Instant.now().toEpochMilli();
			from = now - refreshInterval - fromBufferSeconds * 1000;
			to = now + toBufferSeconds * 1000;

			logDebug(method, " --> from: " + from + " to: " + to);
			try {
				List<EsTelemetryItem> telemetryItemHits = esTelemetryItemRepository
				        .findAllTelemetryItems(deviceId, new String[] { telemetryName }, from, to, SortOrder.ASC);
				TelemetryItemChartModel[] data = TelemetryItemChartModel.getTelemetryItemChartModels(telemetryItemHits);

				if (data.length > 0) {
					logDebug(method, " ----> timestmap: " + data[data.length - 1].getT());
				}

				if (!isSubscribed(subscriptionId)) {
					break;
				}
				getMessagingTemplate().convertAndSendToUser(userSessionId, destination, data,
				        headerAccessor.getMessageHeaders());
			} catch (AcsLogicalException e) {
				logError(method, e);
			}

			try {
				logDebug(method, "sleeping for %s ms", refreshInterval);
				Thread.sleep(refreshInterval);
			} catch (InterruptedException e) {
				logError(method, e);
			}
		}
		headerAccessor.getSessionAttributes().remove(refreshIntervalKey);
	}

	@SubscribeMapping("/topic/device/{deviceId}/telemetry/{telemetryName}")
	public void telemetry(@DestinationVariable String deviceId, @DestinationVariable String telemetryName,
	        StompHeaderAccessor headerAccessor) {
		subscribe(deviceId, headerAccessor);
	}

	@SubscribeMapping("/topic/device/{deviceId}")
	public void device(@DestinationVariable String deviceId, StompHeaderAccessor headerAccessor) {
		subscribe(deviceId, headerAccessor);
	}

	private void subscribe(String deviceId, StompHeaderAccessor headerAccessor) {
		String method = "subscribe";

		logDebug(method, "%s", telemetryListener);

		Device device = getKronosCache().findDeviceById(deviceId);
		Assert.notNull(device, "device is null");

		List<Gateway> gateways = gatewayService.getGatewayRepository()
		        .doFindByApplicationIdAndGatewayIds(device.getApplicationId(), device.getGatewayId());
		if (gateways.size() > 0 && gateways.get(0) != null && gateways.get(0).getHid() != null) {
			telemetryListener.subscribe(gateways.get(0).getHid(), device.getHid(), headerAccessor);
		} else {
			logDebug(method, "gatewayHid is not found for device %s", deviceId);
		}
	}
}
