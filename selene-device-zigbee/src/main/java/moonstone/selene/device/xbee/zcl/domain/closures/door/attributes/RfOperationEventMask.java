package moonstone.selene.device.xbee.zcl.domain.closures.door.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum RfOperationEventMask implements Attribute<SetSensorData> {
	UNKNOWN_OR_MANUFACTURER_SPECIFIC_RF_OPERATION_EVENT,
	LOCK_SOURCE_RF,
	UNLOCK_SOURCE_RF,
	LOCK_SOURCE_RF_ERROR_INVALID_CODE,
	LOCK_SOURCE_RF_ERROR_INVALID_SCHEDULE,
	UNLOCK_SOURCE_RF_ERROR_INVALID_CODE,
	UNLOCK_SOURCE_RF_ERROR_INVALID_SCHEDULE;

	public static Set<RfOperationEventMask> getByValue(byte... value) {
		Set<RfOperationEventMask> result = EnumSet.noneOf(RfOperationEventMask.class);
		for (RfOperationEventMask item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.RF_OPERATION_EVENT_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
