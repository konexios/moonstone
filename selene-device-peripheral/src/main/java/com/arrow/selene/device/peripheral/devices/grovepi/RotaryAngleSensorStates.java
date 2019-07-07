package com.arrow.selene.device.peripheral.devices.grovepi;

import com.arrow.selene.device.peripheral.PeripheralStates;
import com.arrow.selene.engine.state.State;

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
