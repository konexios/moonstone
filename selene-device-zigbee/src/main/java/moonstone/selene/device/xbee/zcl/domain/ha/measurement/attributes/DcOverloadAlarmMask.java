package moonstone.selene.device.xbee.zcl.domain.ha.measurement.attributes;

import java.util.EnumSet;
import java.util.Set;

import moonstone.selene.device.sensor.SetSensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public enum DcOverloadAlarmMask implements Attribute<SetSensorData> {
	VOLTAGE_OVERLOAD,
	CURRENT_OVERLOAD;

	public static Set<DcOverloadAlarmMask> getByValue(byte... value) {
		Set<DcOverloadAlarmMask> result = EnumSet.noneOf(DcOverloadAlarmMask.class);
		for (DcOverloadAlarmMask item : values()) {
			if ((value[0] >> item.ordinal() & 0x01) == 1) {
				result.add(item);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return ElectricalMeasurementClusterAttributes.DC_OVERLOAD_ALARMS_MASK_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
