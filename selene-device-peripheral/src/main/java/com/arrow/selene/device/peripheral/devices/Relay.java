package com.arrow.selene.device.peripheral.devices;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.arrow.selene.device.peripheral.AbstractPeripheralDevice;
import com.arrow.selene.device.peripheral.ControlledPeripheralDevice;
import com.arrow.selene.device.peripheral.ReportingPeripheralDevice;
import com.arrow.selene.device.sensor.BooleanSensorData;
import com.arrow.selene.engine.state.State;

import upm_grove.GroveRelay;

public class Relay extends AbstractPeripheralDevice<RelayStates>
		implements ControlledPeripheralDevice, ReportingPeripheralDevice<BooleanSensorData> {
	private static final String ON_VALUE = "on";
	private GroveRelay device;

	@Override
	public List<BooleanSensorData> getData(Map<String, String> states) {
		boolean value = device.isOn();
		states.putAll(getStates().importStates(Collections
				.singletonMap("relay", new State().withValue(Boolean.toString(value)).withTimestamp(Instant.now()))));
		return device == null ? Collections.emptyList() : Collections.singletonList(
				new BooleanSensorData("relay", value));
	}

	@Override
	public void init(Map<String, String> values) {
		device = new GroveRelay(Long.parseLong(values.get(PIN_FIELD_NAME)));
	}

	@Override
	public boolean changeState(Map<String, State> states) {
		String relay = populate(states).getRelay().getValue();
		if (relay != null) {
			if (Objects.equals(relay, ON_VALUE)) {
				device.on();
			} else {
				device.off();
			}
		}
		return true;
	}

	@Override
	public RelayStates createStates() {
		return new RelayStates();
	}
}
