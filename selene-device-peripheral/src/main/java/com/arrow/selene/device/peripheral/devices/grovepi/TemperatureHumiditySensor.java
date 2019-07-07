package com.arrow.selene.device.peripheral.devices.grovepi;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.arrow.selene.device.peripheral.ReportingPeripheralDevice;
import com.arrow.selene.device.sensor.FloatSensorData;
import com.arrow.selene.engine.EngineConstants;
import com.arrow.selene.engine.state.State;

public class TemperatureHumiditySensor extends GrovePiDevice<TemperatureHumiditySensorStates>
		implements ReportingPeripheralDevice<FloatSensorData> {
	@Override
	public List<FloatSensorData> getData(Map<String, String> states) {
		i2c.write(new byte[]{Commands.DHT_READ, pin, 0, Commands.UNUSED});
		byte[] bytes = new byte[32];
		i2c.read(bytes);
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		Instant timestamp = Instant.now();
		List<FloatSensorData> data = new ArrayList<>(2);
		Map<String, State> newStates = new HashMap<>();
		data.add(checkAndCreate(newStates, "temperature", buf.getFloat(1), this.states.getTemperature(), timestamp));
		data.add(checkAndCreate(newStates, "humidity", buf.getFloat(5), this.states.getHumidity(), timestamp));
		states.putAll(getStates().importStates(newStates));
		return data;
	}

	@Override
	public TemperatureHumiditySensorStates createStates() {
		return new TemperatureHumiditySensorStates();
	}

	private static FloatSensorData checkAndCreate(Map<String, State> states, String name, float newValue, State state,
	                                              Instant timestamp) {
		String stateValue = Float.toString(newValue);
		if (!Objects.equals(state.getValue(), stateValue)) {
			states.put(name, new State().withValue(stateValue).withTimestamp(timestamp));
		}
		return new FloatSensorData(name, newValue, EngineConstants.FORMAT_DECIMAL_2);
	}
}
