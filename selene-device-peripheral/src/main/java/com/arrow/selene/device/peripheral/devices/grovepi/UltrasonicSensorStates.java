package com.arrow.selene.device.peripheral.devices.grovepi;

import com.arrow.selene.device.peripheral.PeripheralStates;
import com.arrow.selene.engine.state.State;

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
