package com.arrow.selene.device.peripheral;

import java.util.List;
import java.util.Map;

import com.arrow.selene.device.sensor.SensorData;

public interface ReportingPeripheralDevice<T extends SensorData<?>> extends PeripheralDevice {
	List<T> getData(Map<String, String> states);
}
