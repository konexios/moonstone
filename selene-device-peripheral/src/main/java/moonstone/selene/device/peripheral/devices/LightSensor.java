package moonstone.selene.device.peripheral.devices;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import moonstone.selene.device.peripheral.AbstractPeripheralDevice;
import moonstone.selene.device.peripheral.ReportingPeripheralDevice;
import moonstone.selene.device.sensor.IntegerSensorData;
import moonstone.selene.engine.state.State;
import upm_grove.GroveLight;

public class LightSensor extends AbstractPeripheralDevice<LightSensorStates>
        implements ReportingPeripheralDevice<IntegerSensorData> {
	private GroveLight device;

	@Override
	public void init(Map<String, String> values) {
		device = new GroveLight(Long.parseLong(values.get(PIN_FIELD_NAME)));
	}

	@Override
	public List<IntegerSensorData> getData(Map<String, String> states) {
		int value = device.value();
		states.putAll(getStates().importStates(Collections.singletonMap("light",
		        new State().withValue(Integer.toString(value)).withTimestamp(Instant.now()))));
		return device == null ? Collections.emptyList()
		        : Collections.singletonList(new IntegerSensorData("light", value));
	}

	@Override
	public LightSensorStates createStates() {
		return new LightSensorStates();
	}
}
