package com.arrow.kronos.web.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceEvent;
import com.arrow.kronos.data.DeviceEventStatus;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.Gateway;
import com.arrow.kronos.data.TelemetryStat;
import com.arrow.kronos.repo.DeviceEventSearchParams;
import com.arrow.kronos.repo.DeviceSearchParams;
import com.arrow.kronos.repo.GatewaySearchParams;
import com.arrow.kronos.service.DeviceEventService;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.GatewayService;
import com.arrow.kronos.service.LastTelemetryItemService;
import com.arrow.kronos.web.model.HomeModels;
import com.arrow.kronos.web.model.HomeModels.MyDevice;
import com.arrow.kronos.web.model.HomeModels.MyDeviceEvent;
import com.arrow.kronos.web.model.HomeModels.MyGateway;
import com.arrow.kronos.web.model.HomeWsModels;
import com.arrow.kronos.web.model.SearchResultModels;
import com.arrow.pegasus.data.heartbeat.HeartbeatObjectType;
import com.arrow.pegasus.data.heartbeat.LastHeartbeat;
import com.arrow.pegasus.repo.LastHeartbeatSearchParams;
import com.arrow.pegasus.service.LastHeartbeatService;

@Controller
@MessageMapping("/api/kronos/ws/home")
public class HomeWsApi extends WsApiAbstract {

	private static final int DEFAULT_REFRESH_INTERVAL = 30000;
	private static final int MIN_REFRESH_INTERVAL = 5000;
	private static final String REFRESH_INTERVAL_KEY = "kronos.ws.home.refresh.interval";

	private static final String HOME_PAGE_DESTINATION = "/queue/home/page";

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private LastTelemetryItemService lastTelemetryItemService;

	@Autowired
	private GatewayService gatewayService;

	@Autowired
	private DeviceEventService deviceEventService;

	@Autowired
	private LastHeartbeatService lastHeartbeatService;

	@MessageMapping("/page")
	public void homePage(HomeWsModels.HomePageOptions options, SimpMessageHeaderAccessor headerAccessor) {
		String method = "homePage";

		Assert.notNull(options, "options is null");

		String userSessionId = headerAccessor.getSessionId();
		String subscriptionId = headerAccessor.getFirstNativeHeader("subscriptionId");
		logDebug(method, " --> subscriptionId %s", subscriptionId);
		Assert.notNull(subscriptionId, "subscriptionId is null");

		int refreshInterval = options.getRefreshInterval() <= 0 ? DEFAULT_REFRESH_INTERVAL
		        : options.getRefreshInterval();
		refreshInterval = refreshInterval < MIN_REFRESH_INTERVAL ? MIN_REFRESH_INTERVAL : refreshInterval;
		logDebug(method, " --> refreshInterval %d", refreshInterval);

		logDebug(method, " --> refresh interval %s", headerAccessor.getSessionAttributes().get(REFRESH_INTERVAL_KEY));
		if (headerAccessor.getSessionAttributes().containsKey(REFRESH_INTERVAL_KEY)) {
			headerAccessor.getSessionAttributes().put(REFRESH_INTERVAL_KEY, refreshInterval);
			return;
		}
		headerAccessor.getSessionAttributes().put(REFRESH_INTERVAL_KEY, refreshInterval);

		logDebug(method, " --> before while...");
		logDebug(method, " --> isSubscribed %s, header containsKey %s", isSubscribed(subscriptionId),
		        headerAccessor.getSessionAttributes().containsKey(REFRESH_INTERVAL_KEY));
		while (isSubscribed(subscriptionId) && headerAccessor.getSessionAttributes().containsKey(REFRESH_INTERVAL_KEY)
		        && (refreshInterval = (int) headerAccessor.getSessionAttributes().get(REFRESH_INTERVAL_KEY)) > 0) {

			logDebug(method, " --> inside while... refreshInterval %d", refreshInterval);

			// DEVICE PAGINATION
			// FILTERED BY NAME
			options.getPaginationDevice().setSortField("name");
			PageRequest devicePageRequest = new PageRequest(options.getPaginationDevice().getPageIndex(),
			        options.getPaginationDevice().getItemsPerPage(),
			        new Sort(Direction.valueOf(options.getPaginationDevice().getSortDirection()),
			                options.getPaginationDevice().getSortField()));

			Page<Device> devicePage = findAuthenticatedUsersDevices(headerAccessor, devicePageRequest);

			// find latest telemetry timestamps for device
			int i = 0;
			String[] deviceIds = new String[devicePage.getContent().size()];
			for (Device device : devicePage.getContent()) {
				deviceIds[i++] = device.getId();
			}

			Map<String, Long> timestamps = new HashMap<>(deviceIds.length);
			if (deviceIds.length > 0) {
				List<TelemetryStat> latestTelemetries = lastTelemetryItemService.getLastTelemetryItemRepository()
				        .findMaxLastTelemetryItemTimestamps(deviceIds);
				if (latestTelemetries != null) {
					for (TelemetryStat latestTelemetry : latestTelemetries) {
						try {
							timestamps.put(latestTelemetry.getName(), Long.valueOf(latestTelemetry.getValue()));
						} catch (NumberFormatException nfe) {
							// ignore
						}
					}
				}
			}

			List<HomeModels.MyDevice> deviceModels = new ArrayList<>();
			for (Device device : devicePage.getContent()) {
				DeviceType type = null;
				if (!StringUtils.isEmpty(device.getDeviceTypeId()))
					type = getKronosCache().findDeviceTypeById(device.getDeviceTypeId());

				deviceModels.add(new HomeModels.MyDevice(device, type != null ? type.getName() : null,
				        timestamps.get(device.getId())));
			}

			Page<MyDevice> resultDevice = new PageImpl<>(deviceModels, devicePageRequest,
			        devicePage.getTotalElements());

			// GATEWAY PAGINATION
			options.getPaginationGateways().setSortField("name");
			PageRequest gatewayPageRequest = new PageRequest(options.getPaginationGateways().getPageIndex(),
			        options.getPaginationGateways().getItemsPerPage(),
			        new Sort(Direction.valueOf(options.getPaginationGateways().getSortDirection()),
			                options.getPaginationGateways().getSortField()));

			GatewaySearchParams params = new GatewaySearchParams();
			params.addApplicationIds(getApplicationId(headerAccessor));
			params.addUserIds(getAuthenticatedUser(headerAccessor).getId());
			params.setEnabled(true);

			// lookup gateways
			Page<Gateway> gatewayPage = gatewayService.getGatewayRepository().findGateways(gatewayPageRequest, params);

			// lookup last heartbeats
			String[] gatewayIds = new String[gatewayPage.getContent().size()];
			int ii = 0;
			for (Gateway gateway : gatewayPage.getContent()) {
				gatewayIds[ii++] = gateway.getId();
			}
			LastHeartbeatSearchParams lastHeartbeatParams = new LastHeartbeatSearchParams();
			lastHeartbeatParams.addObjectTypes(HeartbeatObjectType.GATEWAY);
			lastHeartbeatParams.addObjectIds(gatewayIds);

			List<LastHeartbeat> lastHeartbeats = lastHeartbeatService.getLastHeartbeatRepository()
			        .findLastHeartbeats(lastHeartbeatParams);

			// map gateway id to last heartbeat
			Map<String, LastHeartbeat> gatewayLastHeartbeats = new HashMap<>();
			for (LastHeartbeat lastHeartbeat : lastHeartbeats) {
				gatewayLastHeartbeats.put(lastHeartbeat.getObjectId(), lastHeartbeat);
			}

			// convert to visual model
			List<HomeModels.MyGateway> gatewayModels = new ArrayList<>();
			for (Gateway gateway : gatewayPage.getContent()) {
				gatewayModels.add(new HomeModels.MyGateway(gateway, gateway.getType().name(),
				        gatewayLastHeartbeats.get(gateway.getId())));
			}

			Page<MyGateway> resultGateway = new PageImpl<>(gatewayModels, gatewayPageRequest,
			        gatewayPage.getTotalElements());

			// DEVICE EVENTS PAGINATION
			options.getPaginationDeviceEvents().setSortField("createdDate");
			PageRequest deviceEventsPageRequest = new PageRequest(options.getPaginationDeviceEvents().getPageIndex(),
			        options.getPaginationDeviceEvents().getItemsPerPage(),
			        new Sort(Direction.valueOf(options.getPaginationDeviceEvents().getSortDirection()),
			                options.getPaginationDeviceEvents().getSortField()));

			DeviceSearchParams paramsDeviceEvent = new DeviceSearchParams();
			paramsDeviceEvent.addApplicationIds(getApplicationId(headerAccessor));
			paramsDeviceEvent.addUserIds(getAuthenticatedUser(headerAccessor).getId());
			paramsDeviceEvent.setEnabled(true);

			List<Device> devicePageEvent = deviceService.getDeviceRepository().doFindAllDevices(paramsDeviceEvent);
			Set<String> deviceIdSet = new HashSet<>();
			for (Device device : devicePageEvent)
				deviceIdSet.add(device.getId());

			Page<MyDeviceEvent> resultDeviceEvent = new PageImpl<MyDeviceEvent>(new ArrayList<MyDeviceEvent>());
			if (deviceIdSet.size() > 0) {
				List<HomeModels.MyDeviceEvent> deviceEventModels = new ArrayList<>();
				// sorting & paging (only last 10 events)
				DeviceEventSearchParams deviceEventSearchParams = new DeviceEventSearchParams();
				deviceEventSearchParams.addDeviceIds(deviceIdSet.toArray(new String[deviceIdSet.size()]));
				deviceEventSearchParams.addStatuses(DeviceEventStatus.Open.name());

				// lookup
				Page<DeviceEvent> deviceEventPage = deviceEventService.getDeviceEventRepository()
				        .findDeviceEvents(deviceEventsPageRequest, deviceEventSearchParams);

				// convert to visual model
				for (DeviceEvent deviceEvent : deviceEventPage.getContent()) {
					DeviceActionType deviceActionType = null;
					if (!StringUtils.isEmpty(deviceEvent.getDeviceActionTypeId()))
						deviceActionType = getKronosCache()
						        .findDeviceActionTypeById(deviceEvent.getDeviceActionTypeId());
					Device device = getKronosCache().findDeviceById(deviceEvent.getDeviceId());
					deviceEventModels.add(new HomeModels.MyDeviceEvent(deviceEvent, deviceActionType, device));
				}
				resultDeviceEvent = new PageImpl<>(deviceEventModels, deviceEventsPageRequest,
				        deviceEventPage.getTotalElements());
			}

			HomeWsModels.HomePageModel data = new HomeWsModels.HomePageModel(
			        new SearchResultModels.MyDeviceSearchResultModel(resultDevice, options.getPaginationDevice()),
			        new SearchResultModels.MyGatewaySearchResultModel(resultGateway, options.getPaginationGateways()),
			        new SearchResultModels.MyDeviceEventSearchResultModel(resultDeviceEvent,
			                options.getPaginationDeviceEvents()));
			logDebug(method, " --> data %s %s", data, Thread.currentThread());
			getMessagingTemplate().convertAndSendToUser(userSessionId, HOME_PAGE_DESTINATION, data,
			        headerAccessor.getMessageHeaders());

			if (isSubscribed(subscriptionId)) {
				try {
					logDebug(method, " --> sleeping for %s", refreshInterval);
					Thread.sleep(refreshInterval);
				} catch (InterruptedException e) {
					logError(method, e);
				}
			}
		}
		headerAccessor.getSessionAttributes().remove(REFRESH_INTERVAL_KEY);
		logDebug(method, "end");
	}

	private Page<Device> findAuthenticatedUsersDevices(SimpMessageHeaderAccessor simpMessageHeaderAccessor,
	        PageRequest pageRequest) {

		DeviceSearchParams params = new DeviceSearchParams();
		params.addApplicationIds(getApplicationId(simpMessageHeaderAccessor));
		params.addUserIds(getAuthenticatedUser(simpMessageHeaderAccessor).getId());
		params.setEnabled(true);

		// lookup
		return deviceService.getDeviceRepository().doFindDevices(pageRequest, params);
	}
}
