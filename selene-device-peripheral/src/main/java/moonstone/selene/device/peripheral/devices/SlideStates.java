package moonstone.selene.device.peripheral.devices;

import moonstone.selene.device.peripheral.PeripheralStates;
import moonstone.selene.engine.state.State;

public class SlideStates extends PeripheralStates {
	private static final long serialVersionUID = 1411026515173413255L;

	private State rawValue = new State();
	private State refVoltage = new State();
	private State voltage = new State();

	public State getRawValue() {
		return rawValue;
	}

	public void setRawValue(State rawValue) {
		this.rawValue = rawValue;
	}

	public State getRefVoltage() {
		return refVoltage;
	}

	public void setRefVoltage(State refVoltage) {
		this.refVoltage = refVoltage;
	}

	public State getVoltage() {
		return voltage;
	}

	public void setVoltage(State voltage) {
		this.voltage = voltage;
	}
}
