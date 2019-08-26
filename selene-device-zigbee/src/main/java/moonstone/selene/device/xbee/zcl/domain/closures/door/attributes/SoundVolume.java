package moonstone.selene.device.xbee.zcl.domain.closures.door.attributes;

import moonstone.selene.device.sensor.StringSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum SoundVolume implements Attribute<StringSensorData> {
	SILENT_MODE,
	LOW_VOLUME,
	HIGH_VOLUME;

	public static SoundVolume getByValue(byte... value) {
		return values()[value[0]];
	}

	@Override
	public int getId() {
		return DoorLockClusterAttributes.SOUND_VOLUME_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
