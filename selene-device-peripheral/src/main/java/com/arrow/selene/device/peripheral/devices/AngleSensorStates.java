package com.arrow.selene.device.peripheral.devices;

import com.arrow.selene.device.peripheral.PeripheralStates;
import com.arrow.selene.engine.state.State;

public class AngleSensorStates extends PeripheralStates {
	private static final long serialVersionUID = -181030505004591780L;

	private State absoluteAngle = new State();
	private State relativeAngle = new State();

	public State getAbsoluteAngle() {
		return absoluteAngle;
	}

	public void setAbsoluteAngle(State absoluteAngle) {
		this.absoluteAngle = absoluteAngle;
	}

	public State getRelativeAngle() {
		return relativeAngle;
	}

	public void setRelativeAngle(State relativeAngle) {
		this.relativeAngle = relativeAngle;
	}
}
