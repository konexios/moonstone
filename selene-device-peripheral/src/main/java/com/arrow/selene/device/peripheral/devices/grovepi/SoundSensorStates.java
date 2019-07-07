package com.arrow.selene.device.peripheral.devices.grovepi;

import com.arrow.selene.device.peripheral.PeripheralStates;
import com.arrow.selene.engine.state.State;

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
