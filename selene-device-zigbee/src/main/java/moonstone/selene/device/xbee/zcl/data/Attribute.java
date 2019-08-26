package moonstone.selene.device.xbee.zcl.data;

import moonstone.selene.device.sensor.SensorData;

public interface Attribute<T extends SensorData<?>> {
	int getId();
	T toData(String name, byte... value);
}
