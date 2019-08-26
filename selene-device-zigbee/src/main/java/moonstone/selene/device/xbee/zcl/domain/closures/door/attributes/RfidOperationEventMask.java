package moonstone.selene.device.xbee.zcl.domain.closures.door.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum RfidOperationEventMask implements Attribute<SetSensorData> {
	UNKNOWN_OR_MANUFACTURER_SPECIFIC_RFID_OPERATION_EVENT,
	LOCK_SOURCE_RFID,
	UNLOCK_SOURCE_RFID,
	LOCK_SOURCE_RFID_ERROR_INVALID_RFID_ID,
	LOCK_SOURCE_RFID_ERROR_INVALID_SCHEDULE,
	UNLOCK_SOURCE_RFID_ERROR_INVALID_RFID_ID,
	UNLOCK_SOURCE_RFID_ERROR_INVALID_SCHEDULE;

	public static Set<RfidOperationEventMask> getByValue(byte... value) {
		Set<RfidOperationEventMask> result = EnumSet.noneOf(RfidOperationEventMask.class);
		for (RfidOperationEventMask item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.RFID_OPERATION_EVENT_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
