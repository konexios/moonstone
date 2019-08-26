package moonstone.selene.device.peripheral.devices.grovepi;

import moonstone.selene.device.peripheral.PeripheralStates;
import moonstone.selene.engine.state.State;

public class TemperatureHumiditySensorStates extends PeripheralStates {
	private static final long serialVersionUID = -474024841630381285L;

	private State temperature = new State();
	private State humidity = new State();

	public State getTemperature() {
		return temperature;
	}

	public void setTemperature(State temperature) {
		this.temperature = temperature;
	}

	public State getHumidity() {
		return humidity;
	}

	public void setHumidity(State humidity) {
		this.humidity = humidity;
	}
}
