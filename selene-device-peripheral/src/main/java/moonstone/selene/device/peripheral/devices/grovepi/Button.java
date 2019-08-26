package moonstone.selene.device.peripheral.devices.grovepi;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import moonstone.selene.device.peripheral.ReportingPeripheralDevice;
import moonstone.selene.device.peripheral.devices.ButtonStates;
import moonstone.selene.device.sensor.BooleanSensorData;
import moonstone.selene.engine.state.State;

public class Button extends GrovePiDevice<ButtonStates> implements ReportingPeripheralDevice<BooleanSensorData> {
	@Override
	public List<BooleanSensorData> getData(Map<String, String> states) {
		int value = digitalRead();
		states.putAll(getStates().importStates(Collections
				.singletonMap("pressed", new State().withValue(Integer.toString(value)).withTimestamp(Instant.now())
				)));
		return Collections.singletonList(new BooleanSensorData("pressed", value == 1));
	}

	@Override
	public ButtonStates createStates() {
		return new ButtonStates();
	}
}
