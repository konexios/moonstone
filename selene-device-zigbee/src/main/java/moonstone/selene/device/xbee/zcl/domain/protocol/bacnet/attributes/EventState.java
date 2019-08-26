package moonstone.selene.device.xbee.zcl.domain.protocol.bacnet.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum EventState implements Attribute<StringSensorData> {
	NORMAL,
	FAULT,
	OFFNORMAL,
	HIGH_LIMIT,
	LOW_LIMIT;

	public static EventState getByValue(byte... value) {
		return values()[value[0]];
	}

	@Override
	public int getId() {
		return AnalogInputExtAttributes.EVENT_STATE_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
