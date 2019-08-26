package moonstone.selene.device.peripheral.devices;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import moonstone.selene.device.peripheral.AbstractPeripheralDevice;
import moonstone.selene.device.peripheral.ReportingPeripheralDevice;
import moonstone.selene.device.sensor.IntegerSensorData;
import moonstone.selene.engine.state.State;
import upm_grove.GroveButton;

public class Button extends AbstractPeripheralDevice<ButtonStates>
        implements ReportingPeripheralDevice<IntegerSensorData> {
	private GroveButton device;

	@Override
	public List<IntegerSensorData> getData(Map<String, String> states) {
		int value = device.value();
		states.putAll(getStates().importStates(Collections.singletonMap("pressed",
		        new State().withValue(Integer.toString(value)).withTimestamp(Instant.now()))));

		return device == null ? Collections.emptyList()
		        : Collections.singletonList(new IntegerSensorData("pressed", value));
	}

	@Override
	public void init(Map<String, String> values) {
		device = new GroveButton(Long.parseLong(values.get(PIN_FIELD_NAME)));
	}

	@Override
	public ButtonStates createStates() {
		return new ButtonStates();
	}
}
