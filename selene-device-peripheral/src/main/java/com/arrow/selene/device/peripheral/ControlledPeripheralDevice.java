package com.arrow.selene.device.peripheral;

import java.util.Map;

import com.arrow.selene.engine.state.State;

public interface ControlledPeripheralDevice extends PeripheralDevice {
	boolean changeState(Map<String, State> states);
}
