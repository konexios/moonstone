package com.arrow.selene.device.ble.thunderboard.react.handlers;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.SerializationUtils;

import com.arrow.acs.Loggable;
import com.arrow.selene.device.ble.thunderboard.react.ThunderboardReactStates;
import com.arrow.selene.device.ble.thunderboard.react.sensor.LedsControl;
import com.arrow.selene.engine.state.State;
import com.arrow.selene.engine.state.StateChangeHandler;

public class LedsStateChangeHandler extends Loggable implements StateChangeHandler<ThunderboardReactStates> {
	private static final int LEDB_BITMASK = 0b001;
	private static final int LEDG_BITMASK = 0b100;

	private final LedsControl ledsControl;

	public LedsStateChangeHandler(LedsControl ledsControl) {
		this.ledsControl = ledsControl;
	}

	@Override
	public void handle(ThunderboardReactStates currentStates, Map<String, State> newStates) {
		String method = "handle";
		for (Entry<String, State> entry : newStates.entrySet()) {
			logInfo(method, "key: %s, value: %s", entry.getKey(), entry.getValue().getValue());
		}
		logInfo(method, "currentStates: %s", currentStates);
		ThunderboardReactStates requestStates = SerializationUtils.clone(currentStates);
		requestStates.importStates(newStates);
		logInfo(method, "requestStates: %s", requestStates);

		int value = 0;
		State ledB = requestStates.getLed1status();
		value |= Boolean.parseBoolean(ledB.getValue()) ? LEDB_BITMASK : 0;

		State ledG = requestStates.getLed2status();
		value |= Boolean.parseBoolean(ledG.getValue()) ? LEDG_BITMASK : 0;

		String strValue = String.format("%02x", value);
		logInfo(method, "---> strValue: %d", value);
		ledsControl.setTelemetry(strValue);
	}
}
