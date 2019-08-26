package moonstone.selene.device.peripheral.devices;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import moonstone.selene.device.peripheral.AbstractPeripheralDevice;
import moonstone.selene.device.peripheral.ReportingPeripheralDevice;
import moonstone.selene.device.sensor.FloatSensorData;
import moonstone.selene.engine.EngineConstants;
import moonstone.selene.engine.state.State;
import upm_grove.GroveRotary;

public class AngleSensor extends AbstractPeripheralDevice<AngleSensorStates>
        implements ReportingPeripheralDevice<FloatSensorData> {
	private GroveRotary device;

	@Override
	public void init(Map<String, String> values) {
		device = new GroveRotary(Long.parseLong(values.get(PIN_FIELD_NAME)));
	}

	@Override
	public List<FloatSensorData> getData(Map<String, String> states) {
		List<FloatSensorData> data = new ArrayList<>(2);
		if (device != null) {
			Map<String, State> newStates = new HashMap<>();
			Instant timestamp = Instant.now();
			data.add(checkAndCreate(newStates, "absoluteAngle", device.abs_value(), this.states.getAbsoluteAngle(),
			        timestamp));
			data.add(checkAndCreate(newStates, "relativeAngle", device.rel_value(), this.states.getRelativeAngle(),
			        timestamp));
			states.putAll(getStates().importStates(newStates));
		}
		return data;
	}

	@Override
	public AngleSensorStates createStates() {
		return new AngleSensorStates();
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
