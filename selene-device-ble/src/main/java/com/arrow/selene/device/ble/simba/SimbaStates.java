package com.arrow.selene.device.ble.simba;

import java.util.HashMap;
import java.util.Map;

import com.arrow.selene.device.ble.BleStates;
import com.arrow.selene.engine.state.State;

public class SimbaStates extends BleStates {
	private static final long serialVersionUID = 2161009267507245434L;

	private State led = new State();

	public State getLed() {
		return led;
	}

	public void setLed(State led) {
		this.led = led;
	}

	public static Map<String, State> extractStates(boolean led) {
		Map<String, State> states = new HashMap<>();
		states.put("led", new State().withValue(Boolean.toString(led)));
		return states;
	}

	@Override
	public String toString() {
		return "SimbaStates{" + "led=" + led + '}';
	}
}
