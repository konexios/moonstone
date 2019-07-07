package com.arrow.selene.device.mqttrouter.handlers;

import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

import com.arrow.acs.Loggable;
import com.arrow.selene.device.mqttrouter.MqttRouterDeviceModule;
import com.arrow.selene.device.mqttrouter.MqttRouterDeviceStates;
import com.arrow.selene.engine.state.DeviceStates;
import com.arrow.selene.engine.state.State;
import com.arrow.selene.engine.state.StateChangeHandler;

/**
 * DeviceStateChangeHandler implements over StateChangeHandler.
 * handle method execute when state change request receive from cloudModuleAbstract.
 */
public class DeviceStateChangeHandler extends Loggable implements StateChangeHandler<MqttRouterDeviceStates> {

	private MqttRouterDeviceModule deviceRouter;

	public DeviceStateChangeHandler(MqttRouterDeviceModule deviceRouter) {
		this.deviceRouter = deviceRouter;
	}

	/**
	 * Receive request to perform new device state.
	 * 
	 * @param currentStates device current state.
	 * 
	 * @param newStates requested device new state.
	 */
	@Override
	public void handle(MqttRouterDeviceStates currentStates, Map<String, State> newStates) {
		String method = "handle";
		logInfo(method, "Map for new states %s", newStates);

		logInfo(method, "currentStates: %s", currentStates);
		DeviceStates requestStates = SerializationUtils.clone(currentStates);
		requestStates.importStates(newStates);
		logInfo(method, "requestStates: %s", requestStates);

		// Call to respective device to perform device state change request.
		deviceRouter.setDeviceStates(newStates);

	}
}
