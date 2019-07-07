package com.arrow.selene.device.xbee.zcl.domain.ha.identification.attributes;

import com.arrow.selene.device.sensor.StringSensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public enum CecedSpecificationVersion implements Attribute<StringSensorData> {
	COMPLIANT_WITH_V1_0_NOT_CERTIFIED(0x10),
	COMPLIANT_WITH_V1_0_CERTIFIED(0x1A),
	COMPLIANT_WITH_V2_0_NOT_CERTIFIED(0x20),
	COMPLIANT_WITH_V2_0_CERTIFIED(0x2A),
	COMPLIANT_WITH_V3_0_NOT_CERTIFIED(0x30),
	COMPLIANT_WITH_V3_0_CERTIFIED(0x3A),
	COMPLIANT_WITH_V4_0_NOT_CERTIFIED(0x40),
	COMPLIANT_WITH_V4_0_CERTIFIED(0x4A),
	COMPLIANT_WITH_V5_0_NOT_CERTIFIED(0x50),
	COMPLIANT_WITH_V5_0_CERTIFIED(0x5A),
	COMPLIANT_WITH_V6_0_NOT_CERTIFIED(0x60),
	COMPLIANT_WITH_V6_0_CERTIFIED(0x6A),
	COMPLIANT_WITH_V7_0_NOT_CERTIFIED(0x70),
	COMPLIANT_WITH_V7_0_CERTIFIED(0x7A),
	COMPLIANT_WITH_V8_0_NOT_CERTIFIED(0x80),
	COMPLIANT_WITH_V8_0_CERTIFIED(0x8A),
	COMPLIANT_WITH_V9_0_NOT_CERTIFIED(0x90),
	COMPLIANT_WITH_V9_0_CERTIFIED(0x9A);

	private final int value;

	CecedSpecificationVersion(int value) {
		this.value = value;
	}

	@Override
	public int getId() {
		return ApplianceIdentificationClusterAttributes.CECED_SPECIFICATION_VERSION_ATTRIBUTE_ID;
	}

	public static CecedSpecificationVersion getByValue(byte... value) {
		int intValue = Byte.toUnsignedInt(value[0]);
		for (CecedSpecificationVersion item : values()) {
			if (item.value == intValue) {
				return item;
			}
		}
		return null;
	}

	@Override
	public StringSensorData toData(String name, byte... value) {
		return new StringSensorData(name, getByValue(value));
	}
}
