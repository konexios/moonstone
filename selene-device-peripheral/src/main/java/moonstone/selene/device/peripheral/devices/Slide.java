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
import upm_grove.GroveSlide;

public class Slide extends AbstractPeripheralDevice<SlideStates> implements ReportingPeripheralDevice<FloatSensorData> {
	private GroveSlide device;

	@Override
	public void init(Map<String, String> values) {
		device = new GroveSlide(Long.parseLong(values.get(PIN_FIELD_NAME)));
	}

	@Override
	public List<FloatSensorData> getData(Map<String, String> states) {
		List<FloatSensorData> data = new ArrayList<>(3);
		if (device != null) {
			Map<String, State> newStates = new HashMap<>();
			Instant timestamp = Instant.now();
			data.add(checkAndCreate(newStates, "rawValue", device.raw_value(), this.states.getRawValue(), timestamp));
			data.add(
					checkAndCreate(newStates, "refVoltage", device.ref_voltage(), this.states.getRefVoltage(),
							timestamp));
			data.add(checkAndCreate(newStates, "voltage", device.voltage_value(), this.states.getVoltage(), timestamp));
			states.putAll(getStates().importStates(newStates));
		}
		return data;
	}

	private static FloatSensorData checkAndCreate(Map<String, State> states, String name, float newValue, State state,
	                                              Instant timestamp) {
		String stateValue = Float.toString(newValue);
		if (!Objects.equals(state.getValue(), stateValue)) {
			states.put(name, new State().withValue(stateValue).withTimestamp(timestamp));
		}
		return new FloatSensorData(name, newValue, EngineConstants.FORMAT_DECIMAL_2);
	}

	@Override
	public SlideStates createStates() {
		return new SlideStates();
	}
}
