package moonstone.selene.device.peripheral.devices;

import moonstone.selene.device.peripheral.PeripheralStates;
import moonstone.selene.engine.state.State;

public class ButtonStates extends PeripheralStates {
	private static final long serialVersionUID = -8891939675657062992L;

	private State pressed = new State();

	public State getPressed() {
		return pressed;
	}

	public void setPressed(State pressed) {
		this.pressed = pressed;
	}
}
