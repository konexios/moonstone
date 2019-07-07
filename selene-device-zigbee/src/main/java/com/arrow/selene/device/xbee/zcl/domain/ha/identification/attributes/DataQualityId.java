package com.arrow.selene.device.xbee.zcl.domain.ha.identification.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;
import com.digi.xbee.api.utils.ByteUtils;

public enum DataQualityId implements Attribute<StringSensorData> {
	ALL_DATA_CERTIFIED,
	ONLY_INSTANTANEOUS_POWER_NOT_CERTIFIED,
	ONLY_CUMULATED_CONSUMPTION_NOT_CERTIFIED,
	NOT_CERTIFIED_DATA;

	public static DataQualityId getByValue(int value) {
		return values()[value];
	}

	public static DataQualityId getByValue(byte... value) {
		return getByValue(ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(value)));
	}

	@Override
	public int getId() {
		return MeterIdentificationClusterAttributes.DATA_QUALITY_ID_ATTRIBUTE_ID;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
