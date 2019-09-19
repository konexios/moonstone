package moonstone.selene.device.ble.thunderboard.sense;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import moonstone.acs.JsonUtils;
import moonstone.selene.device.ble.BleStates;
import moonstone.selene.engine.state.State;

public class ThunderboardSenseStates extends BleStates {
	private static final long serialVersionUID = -6243417862686376738L;
	private static final int LED0_BITMASK = 0b0001;
	private static final int LED1_BITMASK = 0b0010;
	private static final int LED2_BITMASK = 0b0100;
	private static final int LED3_BITMASK = 0b1000;

	private State led0 = new State();
	private State led1 = new State();
	private State led2 = new State();
	private State led3 = new State();
	private State color = new State();

	public static Map<String, State> extractStates(int color, int leds) {
		Map<String, State> states = new HashMap<>();
		states.put("color", new State().withValue(String.valueOf(color)));
		states.put("led0", new State().withValue(Boolean.toString((leds & LED0_BITMASK) > 0)));
		states.put("led1", new State().withValue(Boolean.toString((leds & LED1_BITMASK) > 0)));
		states.put("led2", new State().withValue(Boolean.toString((leds & LED2_BITMASK) > 0)));
		states.put("led3", new State().withValue(Boolean.toString((leds & LED3_BITMASK) > 0)));
		return states;
	}

	public Map<String, String> importStates(Map<String, State> newStates) {
		Map<String, String> result = new HashMap<>();
		compareAndUpdateState(result, "led0", led0, newStates);
		compareAndUpdateState(result, "led1", led1, newStates);
		compareAndUpdateState(result, "led2", led2, newStates);
		compareAndUpdateState(result, "led3", led3, newStates);
		compareAndUpdateState(result, "color", color, newStates);
		return result;
	}

	public State getLed0() {
		return led0;
	}

	public void setLed0(State led0) {
		this.led0 = led0;
	}

	public State getLed1() {
		return led1;
	}

	public void setLed1(State led1) {
		this.led1 = led1;
	}

	public State getLed2() {
		return led2;
	}

	public void setLed2(State led2) {
		this.led2 = led2;
	}

	public State getLed3() {
		return led3;
	}

	public void setLed3(State led3) {
		this.led3 = led3;
	}

	public State getColor() {
		return color;
	}

	public void setColor(State color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}

	private void compareAndUpdateState(Map<String, String> result, String name, State currentState,
			Map<String, State> newStates) {
		State newState = newStates.get(name);
		if (currentState != null && newState != null && !Objects.equals(currentState.getValue(), newState.getValue())) {
			currentState.setValue(newState.getValue());
			result.put(name, currentState.getValue());
		}
	}
}
