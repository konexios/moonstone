package com.arrow.selene.device.peripheral.devices;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.arrow.selene.device.peripheral.AbstractPeripheralDevice;
import com.arrow.selene.device.peripheral.ReportingPeripheralDevice;
import com.arrow.selene.device.sensor.IntegerSensorData;
import com.arrow.selene.engine.state.State;

import upm_grove.GroveTemp;

public class TemperatureSensor extends AbstractPeripheralDevice<TemperatureSensorStates>
        implements ReportingPeripheralDevice<IntegerSensorData> {
	private GroveTemp device;

	@Override
	public void init(Map<String, String> values) {
		device = new GroveTemp(Long.parseLong(values.get(PIN_FIELD_NAME)));
	}

	@Override
	public List<IntegerSensorData> getData(Map<String, String> states) {
		int value = device.value();
		states.putAll(getStates().importStates(Collections.singletonMap("temperature",
		        new State().withValue(Integer.toString(value)).withTimestamp(Instant.now()))));
		return device == null ? Collections.emptyList()
		        : Collections.singletonList(new IntegerSensorData("temperature", value));
	}

	@Override
	public TemperatureSensorStates createStates() {
		return new TemperatureSensorStates();
	}
}
