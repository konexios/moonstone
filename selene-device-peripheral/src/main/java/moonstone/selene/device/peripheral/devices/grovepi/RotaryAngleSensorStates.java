package moonstone.selene.device.peripheral.devices.grovepi;

import moonstone.selene.device.peripheral.PeripheralStates;
import moonstone.selene.engine.state.State;

public class RotaryAngleSensorStates extends PeripheralStates {
	private static final long serialVersionUID = 8229700848057366579L;

	private State absoluteAngle = new State();

	public State getAbsoluteAngle() {
		return absoluteAngle;
	}

	public void setAbsoluteAngle(State absoluteAngle) {
		this.absoluteAngle = absoluteAngle;
	}
}
