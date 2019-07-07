package com.arrow.selene.device.xbee.zcl.domain.security.zone.attributes;

import java.util.EnumSet;
import java.util.Set;

import com.arrow.selene.device.sensor.SetSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;
import com.digi.xbee.api.utils.ByteUtils;

public enum ZoneStatus implements Attribute<SetSensorData> {
	ALARM1_CLOSED_OR_NOT_ALARMED((byte) 0, false),
	ALARM1_OPENED_OR_ALARMED((byte) 0, true),
	ALARM2_CLOSED_OR_NOT_ALARMED((byte) 1, false),
	ALARM2_OPENED_OR_ALARMED((byte) 1, true),
	NOT_TAMPERED((byte) 2, false),
	TAMPERED((byte) 2, true),
	BATTERY_OK((byte) 3, false),
	BATTERY_LOW((byte) 3, true),
	SUPERVISION_REPORTS_NOT_REPORTED((byte) 4, false),
	SUPERVISION_REPORTS_REPORTED((byte) 4, true),
	NOT_REPORT_RESTORE((byte) 5, false),
	REPORT_RESTORE((byte) 5, true),
	OK((byte) 6, false),
	TROUBLE_FAILURE((byte) 6, true),
	AC_MAINS_OK((byte) 7, false),
	AC_MAINS_FAULT((byte) 7, true),
	SENSOR_IN_OPERATION_MODE((byte) 8, false),
	SENSOR_IN_TEST_MODE((byte) 8, true),
	SENSOR_BATTERY_FUNCTIONING_NORMALLY((byte) 9, false),
	SENSOR_DETECTS_DEFECTIVE_BATTERY((byte) 9, true);

	private final byte bit;
	private final boolean value;

	ZoneStatus(byte bit, boolean value) {
		this.bit = bit;
		this.value = value;
	}

	public static Set<ZoneStatus> getByValue(byte... value) {
		int zoneStatus = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(value));
		Set<ZoneStatus> result = EnumSet.noneOf(ZoneStatus.class);
		for (ZoneStatus item : values()) {
			if (((zoneStatus >> item.bit & 0x01) == 1) == item.value) {
				result.add(item);
			}
		}
		return result;
	}

	public static int getValue(Set<ZoneStatus> zoneStatuses) {
		int result = 0;
		for (ZoneStatus item : zoneStatuses) {
			if (item.value) {
				result |= 1 << item.bit;
			}
		}
		return result;
	}

	@Override
	public int getId() {
		return SecurityZoneClusterAttributes.ZONE_STATUS_ATTRIBUTE_ID;
	}

	@Override
	public SetSensorData toData(String name, byte... value) {
		return new SetSensorData(name, getByValue(value));
	}
}
