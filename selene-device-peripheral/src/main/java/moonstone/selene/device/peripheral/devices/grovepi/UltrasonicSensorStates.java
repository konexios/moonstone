package moonstone.selene.device.peripheral.devices.grovepi;

import moonstone.selene.device.peripheral.PeripheralStates;
import moonstone.selene.engine.state.State;

public class UltrasonicSensorStates extends PeripheralStates {
	private static final long serialVersionUID = 7331499394738778313L;

	private State range = new State();

	public State getRange() {
		return range;
	}

	public void setRange(State range) {
		this.range = range;
	}
}
