package moonstone.selene.device.peripheral.devices.grovepi;

import moonstone.selene.device.peripheral.PeripheralStates;
import moonstone.selene.engine.state.State;

public class SoundSensorStates extends PeripheralStates {
	private static final long serialVersionUID = -3800555811786535069L;

	private State volume = new State();

	public State getVolume() {
		return volume;
	}

	public void setVolume(State volume) {
		this.volume = volume;
	}
}
