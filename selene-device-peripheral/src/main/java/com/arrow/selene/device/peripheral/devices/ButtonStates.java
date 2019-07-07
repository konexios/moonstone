package com.arrow.selene.device.peripheral.devices;

import com.arrow.selene.device.peripheral.PeripheralStates;
import com.arrow.selene.engine.state.State;

public class ButtonStates extends PeripheralStates {
	private static final long serialVersionUID = -8891939675657062992L;

	private State pressed = new State();

	public State getPressed() {
		return pressed;
	}

	public void setPressed(State pressed) {
		this.pressed = pressed;
	}
}
