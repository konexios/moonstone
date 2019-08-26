package com.arrow.kronos.processor;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlException;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.arrow.kronos.KronosEngineConstants;
import com.arrow.kronos.TelemetryWrapper;
import com.arrow.kronos.action.ActionHandler;
import com.arrow.kronos.action.ActionHandlerFactory;
import com.arrow.kronos.data.Device;
import com.arrow.kronos.data.DeviceActionType;
import com.arrow.kronos.data.DeviceEvent;
import com.arrow.kronos.data.DeviceEventStatus;
import com.arrow.kronos.data.TelemetryItem;
import com.arrow.kronos.service.DeviceActionWrapper;
import com.arrow.kronos.util.ActionHelper;

import moonstone.acs.AcsLogicalException;

@Component
public class ActionProcessor extends TelemetryProcessorAbstract {

	private JexlEngine jexlEngine = new JexlBuilder().strict(true).silent(false).create();

	public ActionProcessor() {
		super(KronosEngineConstants.ProcessorQueue.ACTION);
	}

	protected void doProcessTelemetry(TelemetryWrapper wrapper) {
		String method = "doProcessTelemetry";

		try {
			// Device device =
			// getDeviceMemCache().get(wrapper.getDeviceId()).orElse(null);
			Device device = getContext().getKronosCache().findDeviceById(wrapper.getDeviceId());
			Assert.notNull(device, "device not found: " + wrapper.getDeviceId());
			device.setRefDeviceType(getContext().getKronosCache().findDeviceTypeById(device.getDeviceTypeId()));
			Assert.notNull(device.getRefDeviceType(), "device type not found: " + device.getDeviceTypeId());
			logInfo(method, "deviceUid: %s, deviceName: %s, applicationId: %s", device.getUid(), device.getName(),
			        device.getApplicationId());
			for (DeviceActionWrapper action : ActionHelper.getDeviceActionWrappers(getContext(), wrapper, device)) {
				logInfo(method, "iterating over device actions: %s, enabled: %s, isNoTelemetry: %s, isNotBlank: %s",
				        action.getDescription(), action.isEnabled(), !action.isNoTelemetry(),
				        !StringUtils.isBlank(action.getCriteria()));
				if (action.isEnabled() && !action.isNoTelemetry() && !StringUtils.isBlank(action.getCriteria())) {
					try {
						DeviceActionType deviceActionType = getContext().getKronosCache()
						        .findDeviceActionTypeById(action.getDeviceActionTypeId());
						Assert.notNull(deviceActionType,
						        "deviceActionType not found: " + action.getDeviceActionTypeId());

						action.setRefDeviceActionType(deviceActionType);
						JexlExpression expr = jexlEngine.createExpression(action.getCriteria());
						JexlContext ctx = new MapContext();
						for (TelemetryItem item : wrapper.getItems()) {
							Object value = item.value();
							if (value != null) {
								ctx.set(item.getName(), value);
							}
						}
						Object result = expr.evaluate(ctx);

						logInfo(method, "result: %s", result);
						if (result != null && result.toString().equalsIgnoreCase(Boolean.TRUE.toString())) {
							DeviceEvent event = new DeviceEvent();
							event.setApplicationId(wrapper.getApplicationId());
							event.setDeviceId(wrapper.getDeviceId());
							event.setDeviceActionTypeId(action.getDeviceActionTypeId());
							event.setRefDeviceActionType(action.getRefDeviceActionType());
							event.setCriteria(action.getCriteria());
							event.setExpires(action.getExpiration());
							event.setStatus(DeviceEventStatus.Open);
							boolean created = getContext().getDeviceEventService().createOrUpdate(event, action);
							logInfo(method, "createOrUpdate action: %s, created: %s",
							        action.getRefDeviceActionType().getName(), created);

							// send to action handler
							if (created) {
								ActionHandler handler = ActionHandlerFactory.create(action.getRefDeviceActionType());
								getContext().getSpringContext().getAutowireCapableBeanFactory().autowireBean(handler);
								// put to DeviceEvent information parameters
								// from current DeviceAction
								event.addInformation(action.getParameters());
								// put to DeviceEvent description from current
								// DeviceAction
								event.addInformation("description", action.getDescription());
								handler.handle(event, wrapper, device, action);
								// update event with handler information
								getContext().getDeviceEventService().update(event, "admin");
								logInfo(method, "sent event to action handler: %s", handler.getClass().getName());
							}
						}
					} catch (JexlException e) {
						logError(method, "deviceId: %s, uid: %s, criteria: %s, JEXL ERROR: %s", device.getId(),
						        device.getUid(), action.getCriteria(), e.getMessage());
					} catch (Throwable t) {
						logError(method, "deviceId: %s, uid: %s, criteria: %s, UNKNOWN ERROR: %s", device.getId(),
						        device.getUid(), action.getCriteria(), t);
					}
				} else {
					logDebug(method, "action is disabled or empty criteria: %s", action);
				}
			}
		} catch (Exception e) {
			throw new AcsLogicalException("Unable to process action", e);
		}
	}
}
