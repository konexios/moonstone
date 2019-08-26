package moonstone.selene.device.peripheral.devices.grovepi;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import moonstone.selene.device.peripheral.ReportingPeripheralDevice;
import moonstone.selene.device.sensor.FloatSensorData;
import moonstone.selene.engine.EngineConstants;
import moonstone.selene.engine.state.State;

public class RotaryAngleSensor extends GrovePiDevice<RotaryAngleSensorStates>
		implements ReportingPeripheralDevice<FloatSensorData> {
	private static final float SCALE = 300.f / 1023.f;

	@Override
	public List<FloatSensorData> getData(Map<String, String> states) {
		float value = analogRead() * SCALE;
		states.putAll(getStates().importStates(Collections.singletonMap("absoluteAngle",
				new State().withValue(Float.toString(value)).withTimestamp(Instant.now()))));
		return Collections.singletonList(new FloatSensorData("absoluteAngle", value, EngineConstants
				.FORMAT_DECIMAL_2));
	}

	@Override
	public RotaryAngleSensorStates createStates() {
		return new RotaryAngleSensorStates();
	}
}
