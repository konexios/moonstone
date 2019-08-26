package moonstone.selene.device.peripheral.devices;

import moonstone.selene.device.peripheral.PeripheralStates;
import moonstone.selene.engine.state.State;

public class LightSensorStates extends PeripheralStates {
	private static final long serialVersionUID = 521812564356809991L;

	private State light = new State();

	public State getLight() {
		return light;
	}

	public void setLight(State light) {
		this.light = light;
	}
}
