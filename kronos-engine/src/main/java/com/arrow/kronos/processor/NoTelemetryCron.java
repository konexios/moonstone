package com.arrow.kronos.processor;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.arrow.kronos.KronosEngineContext;
import com.arrow.kronos.TelemetryWrapper;
import com.arrow.kronos.action.ActionHandler;
import com.arrow.kronos.action.ActionHandlerFactory;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceAction;
import com.arrow.kronos.data.DeviceEvent;
import com.arrow.kronos.data.DeviceEventStatus;
import com.arrow.kronos.data.DeviceType;
import com.arrow.kronos.data.LastTelemetryCreated;
import com.arrow.kronos.service.DeviceService;
import com.arrow.kronos.service.DeviceTypeService;
import com.arrow.kronos.service.LastTelemetryItemService;

import moonstone.acs.Loggable;

@Component
public class NoTelemetryCron extends Loggable {

	@Autowired
	private KronosEngineContext context;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private DeviceTypeService deviceTypeService;

	@Autowired
	private LastTelemetryItemService lastTelemetryItemService;

	@Value("${NoTelemetryCron.enabled:true}")
	private boolean enabled;

	@Scheduled(cron = "${NoTelemetryCron.cron:*/15 * * * * *}")
	public void run() {
		if (enabled) {
			scheduleNoTelemetryCronActions();
		}
	}

	private void scheduleNoTelemetryCronActions() {
		String method = "scheduleNoTelemetryCronActions";
		logInfo(method, "start");

		Instant checkQueryTimeStart = Instant.now();
		Map<Device, List<DeviceAction>> map = getNoTelemetryActions();
		Instant checkQueryTimeEnd = Instant.now();

		logDebug(method, "no telemetry actions counting got "
		        + Duration.between(checkQueryTimeStart, checkQueryTimeEnd).toMillis());

		// map: key - Device, value - actions from device and device type with
		// noTelemetry = true

		List<String> deviceIds = new ArrayList<>();
		for (Entry<Device, List<DeviceAction>> entry : map.entrySet())
			deviceIds.add(entry.getKey().getId());

		if (!deviceIds.isEmpty()) {

			// bulk lookup max created dates
			List<LastTelemetryCreated> maxLastTelemetryItemCreatedDates = lastTelemetryItemService
			        .getLastTelemetryItemRepository()
			        .findMaxLastTelemetryItemCreatedDates(deviceIds.toArray(new String[deviceIds.size()]));

			Map<String, LastTelemetryCreated> createdDatesMap = new HashMap<>();
			for (LastTelemetryCreated lastTelemetryCreated : maxLastTelemetryItemCreatedDates)
				createdDatesMap.put(lastTelemetryCreated.getName(), lastTelemetryCreated);

			for (Entry<Device, List<DeviceAction>> entry : map.entrySet()) {

				Device device = entry.getKey();
				List<DeviceAction> actions = entry.getValue();

				logDebug(method, "check " + actions.size() + " actions for deviceId " + device.getId());

				Instant lastTelemetryCreatedDateTime = null;
				LastTelemetryCreated lastTelemetryCreated = createdDatesMap.get(device.getId());

				if (lastTelemetryCreated != null) {
					logInfo(method, "lastTelemetryCreated=" + lastTelemetryCreated.getValue());
					lastTelemetryCreatedDateTime = lastTelemetryCreated.getValue();
				} else {
					lastTelemetryCreatedDateTime = Instant.MIN;
				}
				logDebug(method,
				        "last telemetry time for deviceId " + device.getId() + " is "
				                + (lastTelemetryCreatedDateTime == Instant.MIN ? "Instant.MIN"
				                        : lastTelemetryCreatedDateTime.toEpochMilli()));

				// lets check all actions
				for (DeviceAction action : actions) {

					int i = 0;
					if (action.getNoTelemetryTime() < 1) {
						// additional check that action has correct
						// noTelemetryTime
						// (>= that 1 minute)
						logDebug(method, "skip " + i + " action for deviceId " + device.getId()
						        + " due to incorrect noTelemetryTime " + action.getNoTelemetryTime());
						continue;
					}

					long noTelemetryTimeSeconds = action.getNoTelemetryTime() * 60;
					Instant timeOfNoTelemetryPeriod = Instant.now().minusSeconds(noTelemetryTimeSeconds);
					if (lastTelemetryCreatedDateTime.isAfter(timeOfNoTelemetryPeriod)) {
						// it is to early to call this action according to last
						// telemetry time
						logDebug(method,
						        "skip #" + i + " action for deviceId " + device.getId()
						                + " due to lastTelemetryTime is not so old: "
						                + (lastTelemetryCreatedDateTime == Instant.MIN ? "Instant.MIN"
						                        : lastTelemetryCreatedDateTime.toEpochMilli())
						                + ". to run action, time should be less timeOfNoTelemetryPeriod: "
						                + timeOfNoTelemetryPeriod.toEpochMilli());
						continue;
					}

					action.setRefDeviceActionType(
					        context.getKronosCache().findDeviceActionTypeById(action.getDeviceActionTypeId()));

					performNoTelemetryAction(device, action);
				}

			}
		}

		logInfo(method, "complete");
	}

	private void performNoTelemetryAction(Device device, DeviceAction action) {

		String method = "performNoTelemetryAction";

		try {

			DeviceEvent event = new DeviceEvent();
			event.setApplicationId(device.getApplicationId());
			event.setDeviceId(device.getId());
			event.setDeviceActionTypeId(action.getDeviceActionTypeId());
			event.setRefDeviceActionType(action.getRefDeviceActionType());

			// since we have a noTelemetry flag and noTelemetryTime values, lets
			// replace text criteria on this level
			// probably it would better to not have flag and int field. and put
			// such criteria on action create. can consider in future if needed
			String noTelemetryCriteriaValue = "No Telemetry (" + action.getNoTelemetryTime() + ")";
			event.setCriteria(noTelemetryCriteriaValue);
			action.setCriteria(noTelemetryCriteriaValue);

			long expires = action.getExpiration();
			event.setExpires(expires);
			event.setStatus(DeviceEventStatus.Open);
			boolean created = context.getDeviceEventService().createOrUpdate(event, action);
			logInfo(method, "createOrUpdate action: %s, created: %s", action.getRefDeviceActionType().getName(),
			        created);

			// send to action handler
			if (created) {
				ActionHandler handler = ActionHandlerFactory.create(action.getRefDeviceActionType());
				context.getSpringContext().getAutowireCapableBeanFactory().autowireBean(handler);
				// put to DeviceEvent information parameters from current
				// DeviceAction
				event.addInformation(action.getParameters());

				// FIXME: make sure dummy telemetry is fine for actions
				TelemetryWrapper dummyWrapper = new TelemetryWrapper();
				dummyWrapper.setDeviceId(device.getId());
				dummyWrapper.setTimestamp(Instant.now().toEpochMilli());
				dummyWrapper.setCreatedDate(Instant.now());
				dummyWrapper.setApplicationId(device.getApplicationId());
				dummyWrapper.setId("none");
				dummyWrapper.setItems(Collections.emptyList());

				handler.handle(event, dummyWrapper, device, action);
				// update event with handler information
				context.getDeviceEventService().update(event, "admin");
			}
		} catch (Throwable e) {
			// catch any exception to not affect other actions in current cron
			// process
			logError(method, "failed to perform action for deviceId " + device.getId(), e);
		}
	}

	private Map<Device, List<DeviceAction>> getNoTelemetryActions() {

		String method = "getNoTelemetryActions";
		logDebug(method, "query devices");

		Map<Device, List<DeviceAction>> map = new HashMap<>();

		// all deviceTypes with noTelemetry=true actions
		List<String> deviceTypesIdsWitNoTelemetryActions = deviceTypeService.getDeviceTypeRepository()
		        .findAllDeviceTypesWithNoTelemetryActions();

		logDebug(method,
		        "found " + deviceTypesIdsWitNoTelemetryActions.size() + " device types with noTelemetry actions");

		List<Device> devicesWithNoTelemetryActions = deviceService.getDeviceRepository()
		        .doFindAllDevicesWithNoTelemetryActions(deviceTypesIdsWitNoTelemetryActions);

		logDebug(method, "found " + devicesWithNoTelemetryActions.size() + " devices with noTelemetry actions");

		for (Device d : devicesWithNoTelemetryActions) {
			List<DeviceAction> deviceActions = new ArrayList<DeviceAction>();
			if (d.getActions() != null) {
				deviceActions = d.getActions().stream().filter(a -> a.isNoTelemetry() && a.isEnabled())
				        .collect(Collectors.toList());
			}
			List<DeviceAction> deviceTypeActions = new ArrayList<DeviceAction>();
			if (d.getDeviceTypeId() != null) {
				DeviceType deviceType = context.getKronosCache().findDeviceTypeById(d.getDeviceTypeId());
				d.setRefDeviceType(deviceType);
				if (deviceType != null && deviceType.getActions() != null) {
					deviceTypeActions = deviceType.getActions().stream().filter(a -> a.isNoTelemetry() && a.isEnabled())
					        .collect(Collectors.toList());
				}
			}
			deviceActions.addAll(deviceTypeActions);
			if (deviceActions.size() != 0) {
				map.put(d, deviceActions);
			}
		}
		return map;
	}

}
