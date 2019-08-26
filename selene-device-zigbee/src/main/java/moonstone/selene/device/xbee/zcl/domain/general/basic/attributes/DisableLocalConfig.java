package moonstone.selene.device.xbee.zcl.domain.general.basic.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum DisableLocalConfig implements Attribute<SetSensorData> {
	RESET_ENABLED((byte) 0, false),
	RESET_DISABLED((byte) 0, true),
	DEVICE_CONFIG_ENABLED((byte) 1, false),
	DEVICE_CONFIG_DISABLED((byte) 1, true);

	private final byte bit;
	private final boolean value;

	DisableLocalConfig(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<DisableLocalConfig> getByValue(byte... value) {
		Set<DisableLocalConfig> result = EnumSet.noneOf(DisableLocalConfig.class);
		for (DisableLocalConfig item : values()) {
			if (((value[0] >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return BasicClusterAttributes.DISABLE_LOCAL_CONFIG_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
