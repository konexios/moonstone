package moonstone.selene.device.peripheral.devices.grovepi;

import java.util.Map;
import java.util.Objects;

import moonstone.selene.device.peripheral.ControlledPeripheralDevice;
import moonstone.selene.engine.state.State;

public class Buzzer extends GrovePiDevice<BuzzerStates> implements ControlledPeripheralDevice {
	private static final String ON_VALUE = "on";

	@Override
	public boolean changeState(Map<String, State> states) {
		BuzzerStates buzzerStates = populate(states);
		String buzzer = buzzerStates.getBuzzer().getValue();
		String value = buzzerStates.getValue().getValue();
		if (buzzer != null) {
			if (Objects.equals(buzzer, ON_VALUE)) {
				on();
			} else {
				off();
			}
		} else if (value != null) {
			value(Integer.parseInt(value));
		}
		return true;
	}

	@Override
	public BuzzerStates createStates() {
		return new BuzzerStates();
	}

	private void on() {
		digitalWrite(1);
	}

	private void off() {
		digitalWrite(0);
	}

	private void value(int value) {
		analogWrite(value);
	}
}
