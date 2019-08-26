package moonstone.selene.device.peripheral.devices.grovepi;

import moonstone.selene.device.peripheral.PeripheralStates;
import moonstone.selene.engine.state.State;

public class BuzzerStates extends PeripheralStates {
	private static final long serialVersionUID = -3897425642430859663L;

	private State buzzer = new State();
	private State value = new State();

	public State getBuzzer() {
		return buzzer;
	}

	public void setBuzzer(State buzzer) {
		this.buzzer = buzzer;
	}

	public State getValue() {
		return value;
	}

	public void setValue(State value) {
		this.value = value;
	}
}
