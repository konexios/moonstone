package com.arrow.selene.device.peripheral.devices;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arrow.selene.device.peripheral.AbstractPeripheralDevice;
import com.arrow.selene.device.peripheral.ReportingPeripheralDevice;
import com.arrow.selene.device.sensor.BooleanSensorData;
import com.arrow.selene.device.sensor.IntegerSensorData;
import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.engine.state.State;

import upm_groveultrasonic.GroveUltraSonic;

public class RangerSensor extends AbstractPeripheralDevice<RangerSensorStates>
        implements ReportingPeripheralDevice<SensorData<?>> {
	private GroveUltraSonic device;

	@Override
	public void init(Map<String, String> values) {
		device = new GroveUltraSonic(Integer.parseInt(values.get(PIN_FIELD_NAME)));
	}

	@Override
	public List<SensorData<?>> getData(Map<String, String> states) {
		List<SensorData<?>> data = new ArrayList<>(2);
		if (device != null) {
			Map<String, State> newStates = new HashMap<>();
			int distance = device.getDistance();
			Instant timestamp = Instant.now();
			newStates.put("distance", new State().withValue(Integer.toString(distance)).withTimestamp(timestamp));
			data.add(new IntegerSensorData("distance", distance));
			boolean working = device.working();
			newStates.put("working", new State().withValue(Boolean.toString(working)).withTimestamp(timestamp));
			data.add(new BooleanSensorData("working", working));
			states.putAll(getStates().importStates(newStates));
		}
		return data;
	}

	@Override
	public RangerSensorStates createStates() {
		return new RangerSensorStates();
	}
}
