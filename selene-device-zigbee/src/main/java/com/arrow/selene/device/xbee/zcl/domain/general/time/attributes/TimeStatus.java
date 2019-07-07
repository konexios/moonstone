package com.arrow.selene.device.xbee.zcl.domain.general.time.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum TimeStatus implements Attribute<SetSensorData> {
	NOT_MASTER_CLOCK((byte) 0, false),
	MASTER_CLOCK((byte) 0, true),
	NOT_SYNCHRONIZED((byte) 1, false),
	SYNCHRONIZED((byte) 1, true),
	NOT_MASTER_FOR_TZ_AND_DST((byte) 2, false),
	MASTER_FOR_TZ_AND_DST((byte) 2, true),
	TIME_SYNCHRONIZATION_SHOULD_NOT_BE_SUPERSEDED((byte) 3, false),
	TIME_SYNCHRONIZATION_SHOULD_BE_SUPERSEDED((byte) 3, true);

	private final byte bit;
	private final boolean value;

	TimeStatus(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<TimeStatus> getByValue(byte... value) {
		Set<TimeStatus> result = EnumSet.noneOf(TimeStatus.class);
		for (TimeStatus alarmMask : values()) {
			if (((value[0] >> alarmMask.bit & 0x01) == 1) == alarmMask.value) {
				result.add(alarmMask);
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return TimeClusterAttributes.TIME_STATUS_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
