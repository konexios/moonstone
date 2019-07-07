package com.arrow.selene.device.ble.sensor;

import java.util.List;
import java.util.Map;

import com.arrow.selene.device.ble.gatt.Gatt;
import com.arrow.selene.device.sensor.SensorData;

public interface BleSensor<Prop extends SensorProperties, Data extends SensorData<?>> {
	String getName();

	void enable();

	void disable();

	void setPeriod(int period);

	boolean isEnabled();

	List<Data> parseData(byte[] data);

	List<Data> parseData(byte[] data, int offset);

	Prop getProperties();

	void setProperties(Map<String, String> properties);

	void configure(Gatt gatt);

	boolean isPassive();

	byte[] readValue();

	List<String> getControlledTelemetry();

	void setTelemetry(String value);

	String[] getUUIDs();
}
