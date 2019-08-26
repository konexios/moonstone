package moonstone.selene.device.peripheral.devices;

import moonstone.selene.device.peripheral.PeripheralStates;
import moonstone.selene.engine.state.State;

public class RelayStates extends PeripheralStates {
	private static final long serialVersionUID = 7352391493878911572L;

	private State relay = new State();

	public State getRelay() {
		return relay;
	}

	public void setRelay(State relay) {
		this.relay = relay;
	}
}
