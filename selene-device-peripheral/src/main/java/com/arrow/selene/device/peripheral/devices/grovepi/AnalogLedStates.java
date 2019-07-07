package com.arrow.selene.device.peripheral.devices.grovepi;

import com.arrow.selene.device.peripheral.PeripheralStates;
import com.arrow.selene.engine.state.State;

public class AnalogLedStates extends PeripheralStates {
	private static final long serialVersionUID = -8432648658679402219L;

	private State led = new State();
	private State value = new State();

	public State getLed() {
		return led;
	}

	public void setLed(State led) {
		this.led = led;
	}

	public State getValue() {
		return value;
	}

	public void setValue(State value) {
		this.value = value;
	}
}
