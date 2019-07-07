package com.arrow.selene.device.peripheral.devices.grovepi;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.arrow.selene.device.peripheral.ReportingPeripheralDevice;
import com.arrow.selene.device.sensor.IntegerSensorData;
import com.arrow.selene.engine.state.State;

public class UltrasonicSensor extends GrovePiDevice<UltrasonicSensorStates>
		implements ReportingPeripheralDevice<IntegerSensorData> {
	@Override
	public List<IntegerSensorData> getData(Map<String, String> states) {
		i2c.write(new byte[]{Commands.ULTRASONIC_READ, pin, 0, Commands.UNUSED});
		byte[] bytes = new byte[32];
		i2c.read(bytes);
		int value = Short.toUnsignedInt(ByteBuffer.wrap(bytes).getShort(1));
		states.putAll(getStates().importStates(Collections
				.singletonMap("range", new State().withValue(Float.toString(value)).withTimestamp(Instant.now()))));
		return Collections.singletonList(new IntegerSensorData("range", value));
	}

	@Override
	public UltrasonicSensorStates createStates() {
		return new UltrasonicSensorStates();
	}
}
