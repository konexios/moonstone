package moonstone.selene.device.peripheral.devices.jhd1313m1;

import moonstone.selene.device.peripheral.PeripheralStates;
import moonstone.selene.engine.state.State;

public class Jhd1313m1States extends PeripheralStates {
	private static final long serialVersionUID = -2366506018783901549L;

	// on/off
	private State backlight = new State();

	// red/green/blue
	private State color = new State();

	// on/off
	private State display = new State();

	// cursor + text
	private State row1 = new State();

	// cursor + text
	private State row2 = new State();

	public State getBacklight() {
		return backlight;
	}

	public void setBacklight(State backlight) {
		this.backlight = backlight;
	}

	public State getColor() {
		return color;
	}

	public void setColor(State color) {
		this.color = color;
	}

	public State getDisplay() {
		return display;
	}

	public void setDisplay(State display) {
		this.display = display;
	}

	public State getRow1() {
		return row1;
	}

	public void setRow1(State row1) {
		this.row1 = row1;
	}

	public State getRow2() {
		return row2;
	}

	public void setRow2(State row2) {
		this.row2 = row2;
	}
}
