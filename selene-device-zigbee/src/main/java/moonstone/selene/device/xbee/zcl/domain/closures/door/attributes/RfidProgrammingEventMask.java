package moonstone.selene.device.xbee.zcl.domain.closures.door.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum RfidProgrammingEventMask implements Attribute<SetSensorData> {
	UNKNOWN_OR_MANUFACTURER_SPECIFIC_RFID_PROGRAMMING_EVENT,
	RESERVED1,
	RESERVED2,
	RESERVED3,
	RESERVED4,
	ID_ADDED_SOURCE_RFID,
	ID_DELETED_SOURCE_RFID;

	public static Set<RfidProgrammingEventMask> getByValue(byte... value) {
		Set<RfidProgrammingEventMask> result = EnumSet.noneOf(RfidProgrammingEventMask.class);
		for (RfidProgrammingEventMask item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.RFID_PROGRAMMING_EVENT_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
