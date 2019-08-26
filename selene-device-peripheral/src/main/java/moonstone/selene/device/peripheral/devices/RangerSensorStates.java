package moonstone.selene.device.peripheral.devices;

import moonstone.selene.device.peripheral.PeripheralStates;
import moonstone.selene.engine.state.State;

public class RangerSensorStates extends PeripheralStates {
	private static final long serialVersionUID = -5208328102482480082L;

	private State distance = new State();
	private State working = new State();

	public State getDistance() {
		return distance;
	}

	public void setDistance(State distance) {
		this.distance = distance;
	}

	public State getWorking() {
		return working;
	}

	public void setWorking(State working) {
		this.working = working;
	}
}
