package com.arrow.selene.device.peripheral.devices.grovepi;

import java.util.Map;
import java.util.Objects;

import com.arrow.selene.device.peripheral.ControlledPeripheralDevice;
import com.arrow.selene.engine.state.State;

public class Led extends GrovePiDevice<AnalogLedStates> implements ControlledPeripheralDevice {
	private static final String ON_VALUE = "on";

	@Override
	public boolean changeState(Map<String, State> statesMap) {
		AnalogLedStates ledStates = populate(statesMap);
		String led = ledStates.getLed().getValue();
		String value = ledStates.getValue().getValue();
		if (led != null) {
			if (Objects.equals(led, ON_VALUE)) {
				on();
			} else {
				off();
			}
		} else if (value != null) {
			value(Integer.parseInt(value));
		}
		return true;
	}

	@Override
	public AnalogLedStates createStates() {
		return new AnalogLedStates();
	}

	private void on() {
		digitalWrite(1);
	}

	private void off() {
		digitalWrite(0);
	}

	private void value(int value) {
		analogWrite(value);
	}
}
