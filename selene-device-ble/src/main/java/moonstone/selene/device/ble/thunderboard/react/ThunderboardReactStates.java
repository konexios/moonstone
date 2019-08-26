package moonstone.selene.device.ble.thunderboard.react;

import java.util.HashMap;
import java.util.Map;

import moonstone.selene.device.ble.BleStates;
import moonstone.selene.engine.state.State;

public class ThunderboardReactStates extends BleStates {
	private static final long serialVersionUID = 8385284728577421042L;

	private static final int LEDB_BITMASK = 0b001;
	private static final int LEDG_BITMASK = 0b100;

	private State led1status = new State();
	private State led2status = new State();

	public static Map<String, State> extractStates(int leds) {
		Map<String, State> states = new HashMap<>();
		states.put("led1status", new State().withValue(Boolean.toString((leds & LEDB_BITMASK) > 0)));
		states.put("led2status", new State().withValue(Boolean.toString((leds & LEDG_BITMASK) > 0)));
		return states;
	}

	public State getLed1status() {
		return led1status;
	}

	public void setLed1status(State led1status) {
		this.led1status = led1status;
	}

	public State getLed2status() {
		return led2status;
	}

	public void setLed2status(State led2status) {
		this.led2status = led2status;
	}

	@Override
	public String toString() {
		return "ThunderboardReactStates{" + "led1status=" + led1status + ", led2status=" + led2status + '}';
	}
}
