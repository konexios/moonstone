package moonstone.selene.device.peripheral;

import java.util.Map;

import moonstone.selene.engine.state.State;

public interface ControlledPeripheralDevice extends PeripheralDevice {
	boolean changeState(Map<String, State> states);
}
