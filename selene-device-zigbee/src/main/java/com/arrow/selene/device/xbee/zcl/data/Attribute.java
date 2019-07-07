package com.arrow.selene.device.xbee.zcl.data;

import com.arrow.selene.device.sensor.SensorData;

public interface Attribute<T extends SensorData<?>> {
	int getId();
	T toData(String name, byte... value);
}
