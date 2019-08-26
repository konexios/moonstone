package moonstone.selene.device.peripheral.devices;

import java.util.Map;
import java.util.Objects;

import moonstone.selene.device.peripheral.AbstractPeripheralDevice;
import moonstone.selene.device.peripheral.ControlledPeripheralDevice;
import moonstone.selene.engine.state.State;
import upm_grove.GroveLed;

public class Led extends AbstractPeripheralDevice<LedStates> implements ControlledPeripheralDevice {
	private static final String ON_VALUE = "on";
	private GroveLed device;

	@Override
	public void init(Map<String, String> values) {
		device = new GroveLed(Integer.parseInt(values.get(PIN_FIELD_NAME)));
	}

	@Override
	public LedStates createStates() {
		return new LedStates();
	}

	@Override
	public boolean changeState(Map<String, State> states) {
		String led = populate(states).getLed().getValue();
		if (led != null) {
			if (Objects.equals(led, ON_VALUE)) {
				device.on();
			} else {
				device.off();
			}
		}
		return true;
	}
}
