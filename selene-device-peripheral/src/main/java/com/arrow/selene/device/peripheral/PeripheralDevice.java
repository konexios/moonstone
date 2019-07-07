package com.arrow.selene.device.peripheral;

import java.util.Map;

public interface PeripheralDevice {
	void init(Map<String, String> values);

	void start();

	void stop();

	boolean isShuttingDown();
}
