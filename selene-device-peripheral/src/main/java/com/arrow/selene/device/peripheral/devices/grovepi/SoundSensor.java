package com.arrow.selene.device.peripheral.devices.grovepi;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.arrow.selene.device.peripheral.ReportingPeripheralDevice;
import com.arrow.selene.device.sensor.IntegerSensorData;
import com.arrow.selene.engine.state.State;

public class SoundSensor extends GrovePiDevice<SoundSensorStates>
		implements ReportingPeripheralDevice<IntegerSensorData> {
	@Override
	public List<IntegerSensorData> getData(Map<String, String> states) {
		int value = analogRead();
		states.putAll(getStates().importStates(Collections
				.singletonMap("volume", new State().withValue(Float.toString(value)).withTimestamp(Instant.now()))));
		return Collections.singletonList(new IntegerSensorData("volume", value));
	}

	@Override
	public SoundSensorStates createStates() {
		return new SoundSensorStates();
	}
}
