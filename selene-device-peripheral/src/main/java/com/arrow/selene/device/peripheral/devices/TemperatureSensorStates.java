package com.arrow.selene.device.peripheral.devices;

import com.arrow.selene.device.peripheral.PeripheralStates;
import com.arrow.selene.engine.state.State;

public class TemperatureSensorStates extends PeripheralStates {
	private static final long serialVersionUID = 1574361910524341637L;

	private State temperature = new State();

	public State getTemperature() {
		return temperature;
	}

	public void setTemperature(State temperature) {
		this.temperature = temperature;
	}
}
