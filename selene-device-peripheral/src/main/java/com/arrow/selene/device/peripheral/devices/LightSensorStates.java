package com.arrow.selene.device.peripheral.devices;

import com.arrow.selene.device.peripheral.PeripheralStates;
import com.arrow.selene.engine.state.State;

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
