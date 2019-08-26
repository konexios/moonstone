package moonstone.selene.device.xbee.zcl.domain.hvac.thermostat.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum RemoteSensing implements Attribute<SetSensorData> {
	LOCAL_TEMPERATURE_SENSED_INTERNALLY((byte) 0, false),
	LOCAL_TEMPERATURE_SENSED_REMOTELY((byte) 0, true),
	OUTDOOR_TEMPERATURE_SENSED_INTERNALLY((byte) 1, false),
	OUTDOOR_TEMPERATURE_SENSED_REMOTELY((byte) 1, true),
	OCCUPANCY_SENSED_INTERNALLY((byte) 2, false),
	OCCUPANCY_SENSED_REMOTELY((byte) 2, true);

	private final byte bit;
	private final boolean value;

	RemoteSensing(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<RemoteSensing> getByValue(byte... value) {
		Set<RemoteSensing> result = EnumSet.noneOf(RemoteSensing.class);
		for (RemoteSensing item : values()) {
			if (((value[0] >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return HvacThermostatClusterAttributes.REMOTE_SENSING_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
