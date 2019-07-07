package com.arrow.kronos.web.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
import com.arrow.kronos.web.model.EULAModel;
import com.arrow.kronos.web.model.HomeModels;
import com.arrow.kronos.web.model.HomeModels.MyDevice;
import com.arrow.kronos.web.model.HomeModels.MyDeviceEvent;
import com.arrow.kronos.web.model.HomeModels.MyGateway;
import com.arrow.kronos.web.model.SearchFilterModels;
import com.arrow.kronos.web.model.SearchResultModels;
import com.arrow.pegasus.client.api.ClientProductApi;
import com.arrow.pegasus.client.api.ClientUserApi;
import com.arrow.pegasus.data.ConfigurationProperty;
import com.arrow.pegasus.data.heartbeat.HeartbeatObjectType;
import com.arrow.pegasus.data.heartbeat.LastHeartbeat;
import com.arrow.pegasus.data.profile.Product;
import com.arrow.pegasus.data.profile.ProductEULA;
import com.arrow.pegasus.data.profile.User;
import com.arrow.pegasus.data.profile.UserEULA;
import com.arrow.pegasus.repo.LastHeartbeatSearchParams;
import com.arrow.pegasus.service.LastHeartbeatService;
import com.arrow.pegasus.webapi.data.ResponseWrapper;

@RestController
@RequestMapping("/api/kronos/home")
public class HomeController extends BaseControllerAbstract {

	@Autowired
	private DeviceService deviceService;
	@Autowired
	private GatewayService gatewayService;
	@Autowired
	private LastTelemetryItemService lastTelemetryItemService;
	@Autowired
	private DeviceEventService deviceEventService;
	@Autowired
	private LastHeartbeatService lastHeartbeatService;
	@Autowired
	private ClientUserApi clientUserApi;
	@Autowired
	private ClientProductApi clientProductApi;

	@RequestMapping(value = "/checkeula", method = RequestMethod.GET)
	public ResponseWrapper checkEULA(HttpSession session) {

		long start = System.currentTimeMillis();
		String method = "checkEULA";
		logInfo(method, "...");

		EULAModel model = new EULAModel();
		boolean needsToAgree = false;

		Product product = clientProductApi.findById(getApplication(session).getProductId());
		ConfigurationProperty eulaConfigurationProperty = null;
		for (ConfigurationProperty cp : product.getConfigurations())
			if (cp.getName().equalsIgnoreCase("productEULA")) {
				eulaConfigurationProperty = cp;
				break;
			}

		if (eulaConfigurationProperty != null) {
			ProductEULA productEULA = (ProductEULA) eulaConfigurationProperty.jsonValue();

			if (!StringUtils.isEmpty(productEULA.getUrl()) && productEULA.getDate() != null) {
				model.withUrl(productEULA.getUrl()).withDate(productEULA.getDate());

				User user = clientUserApi.findById(getAuthenticatedUser().getId());

				UserEULA userEULA = null;
				for (UserEULA ue : user.getEulas()) {
					if (ue.getProductId().equals(product.getId())) {
						userEULA = ue;
						break;
					}
				}

				needsToAgree = (userEULA == null || userEULA.getAgreedDate().isBefore(productEULA.getDate()));

				logInfo(method, "needsToAgree: " + needsToAgree);
			}
		}

		model.withNeedsToAgree(needsToAgree);

		long end = System.currentTimeMillis();

		return new ResponseWrapper(start, end, model);
	}

	@RequestMapping(value = "/updateeula", method = RequestMethod.POST)
	public ResponseWrapper updateEULA(HttpSession session) {

		long start = System.currentTimeMillis();
		String method = "updateEULA";
		logInfo(method, "...");

		Product product = clientProductApi.findById(getApplication(session).getProductId());
		User user = clientUserApi.findById(getAuthenticatedUser().getId());

		UserEULA userEULA = null;
		for (UserEULA ue : user.getEulas()) {
			if (ue.getProductId().equals(product.getId())) {
				userEULA = ue;
				break;
			}
		}

		if (userEULA == null) {
			userEULA = new UserEULA();
			userEULA.setProductId(product.getId());
		}
		userEULA.setAgreedDate(Instant.now());

		// remove old user eula
		for (int i = 0; i < user.getEulas().size(); i++) {
			UserEULA ue = user.getEulas().get(i);
			if (ue.getProductId().equals(product.getId())) {
				user.getEulas().remove(i);
				break;
			}
		}

		user.getEulas().add(userEULA);
		user = clientUserApi.update(user, getUserId());

		long end = System.currentTimeMillis();

		return new ResponseWrapper(start, end, user.getEulas());
	}

	private Page<Device> findAuthenticatedUsersDevices(HttpSession session, PageRequest pageRequest) {
		// sorting & paging

		DeviceSearchParams params = new DeviceSearchParams();
		params.addApplicationIds(getApplicationId(session));
		params.addUserIds(getAuthenticatedUser().getId());
		params.setEnabled(true);

		// lookup
		return deviceService.getDeviceRepository().doFindDevices(pageRequest, params);
	}

	@RequestMapping(value = "/devices", method = RequestMethod.POST)
	public SearchResultModels.MyDeviceSearchResultModel devices(
			@RequestBody SearchFilterModels.MyDevicesSearchFilterModel searchFilter, HttpSession session) {

		// FILTERED BY NAME
		searchFilter.setSortField("name");
		PageRequest pageRequest = PageRequest.of(searchFilter.getPageIndex(), searchFilter.getItemsPerPage(),
				Sort.by(Direction.valueOf(searchFilter.getSortDirection()), searchFilter.getSortField()));

		Page<Device> devices = findAuthenticatedUsersDevices(session, pageRequest);

		// find latest telemetry timestamps for device
		int i = 0;
		String[] deviceIds = new String[devices.getContent().size()];
		for (Device device : devices.getContent()) {
			deviceIds[i++] = device.getId();
		}

		Map<String, Long> timestamps = new HashMap<>(deviceIds.length);
		if (deviceIds.length > 0) {
			List<TelemetryStat> telemetryStat = lastTelemetryItemService.getLastTelemetryItemRepository()
					.findMaxLastTelemetryItemTimestamps(deviceIds);
			if (telemetryStat != null) {
				for (TelemetryStat latestTelemetry : telemetryStat) {
					try {
						timestamps.put(latestTelemetry.getName(), Long.valueOf(latestTelemetry.getValue()));
					} catch (NumberFormatException nfe) {
						// ignore
					}
				}
			}
		}

		// convert to visual model
		List<HomeModels.MyDevice> deviceModels = new ArrayList<>();
		for (Device device : devices.getContent()) {
			DeviceType type = null;
			if (!StringUtils.isEmpty(device.getDeviceTypeId()))
				type = getKronosCache().findDeviceTypeById(device.getDeviceTypeId());

			deviceModels.add(new HomeModels.MyDevice(device, type != null ? type.getName() : null,
					timestamps.get(device.getId())));
		}

		Page<MyDevice> result = new PageImpl<>(deviceModels, pageRequest, devices.getTotalElements());
		return new SearchResultModels.MyDeviceSearchResultModel(result, searchFilter);

	}

	@RequestMapping(value = "/gateways", method = RequestMethod.POST)
	public SearchResultModels.MyGatewaySearchResultModel gateways(
			@RequestBody SearchFilterModels.MyGatewaySearchFilterModel searchFilterModel, HttpSession session) {

		searchFilterModel.setSortField("name");
		PageRequest pageRequest = PageRequest.of(searchFilterModel.getPageIndex(), searchFilterModel.getItemsPerPage(),
				Sort.by(Direction.valueOf(searchFilterModel.getSortDirection()), searchFilterModel.getSortField()));

		GatewaySearchParams params = new GatewaySearchParams();
		params.addApplicationIds(getApplicationId(session));
		params.addUserIds(getAuthenticatedUser().getId());
		params.setEnabled(true);

		// lookup gateways
		Page<Gateway> gatewayPage = gatewayService.getGatewayRepository().findGateways(pageRequest, params);

		// lookup last heartbeats
		String[] gatewayIds = new String[gatewayPage.getContent().size()];
		int i = 0;
		for (Gateway gateway : gatewayPage.getContent()) {
			gatewayIds[i++] = gateway.getId();
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

		Page<MyGateway> result = new PageImpl<>(gatewayModels, pageRequest, gatewayPage.getTotalElements());

		return new SearchResultModels.MyGatewaySearchResultModel(result, searchFilterModel);
	}

	@RequestMapping(value = "/device/events", method = RequestMethod.POST)
	public SearchResultModels.MyDeviceEventSearchResultModel deviceEvents(
			@RequestBody SearchFilterModels.MyDeviceEventSearchFilterModel searchFilterModel, HttpSession session) {

		searchFilterModel.setSortField("createdDate");
		PageRequest pageRequest = PageRequest.of(searchFilterModel.getPageIndex(), searchFilterModel.getItemsPerPage(),
				Sort.by(Direction.valueOf(searchFilterModel.getSortDirection()), searchFilterModel.getSortField()));

		DeviceSearchParams params = new DeviceSearchParams();
		params.addApplicationIds(getApplicationId(session));
		params.addUserIds(getAuthenticatedUser().getId());
		params.setEnabled(true);

		List<Device> devicePage = deviceService.getDeviceRepository().doFindAllDevices(params);
		Set<String> deviceIdSet = new HashSet<>();
		for (Device device : devicePage)
			deviceIdSet.add(device.getId());

		List<HomeModels.MyDeviceEvent> deviceEventModels = new ArrayList<>();

		DeviceEventSearchParams deviceEventSearchParams = new DeviceEventSearchParams();
		deviceEventSearchParams.addDeviceIds(deviceIdSet.toArray(new String[deviceIdSet.size()]));
		deviceEventSearchParams.addStatuses(DeviceEventStatus.Open.name());

		// lookup
		Page<DeviceEvent> deviceEventPage = deviceEventService.getDeviceEventRepository().findDeviceEvents(pageRequest,
				deviceEventSearchParams);

		if (!deviceIdSet.isEmpty()) {
			// convert to visual model
			for (DeviceEvent deviceEvent : deviceEventPage.getContent()) {
				DeviceActionType deviceActionType = null;
				if (!StringUtils.isEmpty(deviceEvent.getDeviceActionTypeId()))
					deviceActionType = getKronosCache().findDeviceActionTypeById(deviceEvent.getDeviceActionTypeId());
				Device device = getKronosCache().findDeviceById(deviceEvent.getDeviceId());
				deviceEventModels.add(new HomeModels.MyDeviceEvent(deviceEvent, deviceActionType, device));
			}
		}

		Page<MyDeviceEvent> result = new PageImpl<>(deviceEventModels, pageRequest, deviceEventPage.getTotalElements());
		return new SearchResultModels.MyDeviceEventSearchResultModel(result, searchFilterModel);
	}
}
