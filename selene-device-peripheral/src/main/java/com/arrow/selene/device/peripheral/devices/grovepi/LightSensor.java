package com.arrow.selene.device.peripheral.devices.grovepi;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.arrow.selene.device.peripheral.ReportingPeripheralDevice;
import com.arrow.selene.device.peripheral.devices.LightSensorStates;
import com.arrow.selene.device.sensor.IntegerSensorData;
import com.arrow.selene.engine.state.State;

public class LightSensor extends GrovePiDevice<LightSensorStates>
		implements ReportingPeripheralDevice<IntegerSensorData> {
	@Override
	public List<IntegerSensorData> getData(Map<String, String> states) {
		int value = analogRead();
		states.putAll(getStates().importStates(Collections
				.singletonMap("light", new State().withValue(Integer.toString(value)).withTimestamp(Instant.now()))));
		return Collections.singletonList(new IntegerSensorData("light", value));
	}

	@Override
	public LightSensorStates createStates() {
		return new LightSensorStates();
	}
}
