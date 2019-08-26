package moonstone.selene.device.peripheral.devices.grovepi;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import moonstone.selene.device.peripheral.ControlledPeripheralDevice;
import moonstone.selene.device.peripheral.ReportingPeripheralDevice;
import moonstone.selene.device.peripheral.devices.RelayStates;
import moonstone.selene.device.sensor.BooleanSensorData;
import moonstone.selene.engine.state.State;

public class Relay extends GrovePiDevice<RelayStates>
		implements ControlledPeripheralDevice, ReportingPeripheralDevice<BooleanSensorData> {
	private static final String ON_VALUE = "on";

	@Override
	public boolean changeState(Map<String, State> states) {
		String relay = populate(states).getRelay().getValue();
		if (relay != null) {
			if (Objects.equals(relay, ON_VALUE)) {
				on();
			} else {
				off();
			}
		}
		return true;
	}

	@Override
	public List<BooleanSensorData> getData(Map<String, String> states) {
		int value = digitalRead();
		states.putAll(getStates().importStates(Collections
				.singletonMap("relay", new State().withValue(Integer.toString(value)).withTimestamp(Instant.now()))));
		return Collections.singletonList(new BooleanSensorData("relay", value == 1));
	}

	@Override
	public RelayStates createStates() {
		return new RelayStates();
	}

	private void on() {
		digitalWrite(1);
	}

	private void off() {
		digitalWrite(0);
	}
}
