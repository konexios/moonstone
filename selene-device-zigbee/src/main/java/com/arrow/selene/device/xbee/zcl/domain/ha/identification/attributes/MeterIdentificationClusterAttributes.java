package com.arrow.selene.device.xbee.zcl.domain.ha.identification.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class MeterIdentificationClusterAttributes {
	public static final int METER_COMPANY_NAME_ATTRIBUTE_ID = 0x0000;
	public static final int METER_TYPE_ID_ATTRIBUTE_ID = 0x0001;
	public static final int DATA_QUALITY_ID_ATTRIBUTE_ID = 0x0004;
	public static final int CUSTOMER_NAME_ATTRIBUTE_ID = 0x0005;
	public static final int METER_MODEL_ATTRIBUTE_ID = 0x0006;
	public static final int METER_PART_NUMBER_ATTRIBUTE_ID = 0x0007;
	public static final int METER_PRODUCT_REVISION_ATTRIBUTE_ID = 0x0008;
	public static final int METER_SOFTWARE_REVISION_ATTRIBUTE_ID = 0x000A;
	public static final int UTILITY_NAME_ATTRIBUTE_ID = 0x000B;
	public static final int POD_ATTRIBUTE_ID = 0x000C;
	public static final int AVAILABLE_POWER_ATTRIBUTE_ID = 0x000D;
	public static final int POWER_THRESHOLD_ATTRIBUTE_ID = 0x000E;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(METER_COMPANY_NAME_ATTRIBUTE_ID, new ImmutablePair<>("Meter Company Name", null));
		ALL.put(METER_TYPE_ID_ATTRIBUTE_ID, new ImmutablePair<>("Meter Type Id", MeterTypeId.GENERIC_METER));
		ALL.put(DATA_QUALITY_ID_ATTRIBUTE_ID, new ImmutablePair<>("Data Quality Id", DataQualityId
				.ALL_DATA_CERTIFIED));
		ALL.put(CUSTOMER_NAME_ATTRIBUTE_ID, new ImmutablePair<>("Customer Name", null));
		ALL.put(METER_MODEL_ATTRIBUTE_ID, new ImmutablePair<>("Meter Model", null));
		ALL.put(METER_PART_NUMBER_ATTRIBUTE_ID, new ImmutablePair<>("Meter Part Number", null));
		ALL.put(METER_PRODUCT_REVISION_ATTRIBUTE_ID, new ImmutablePair<>("Meter Product Revision", null));
		ALL.put(METER_SOFTWARE_REVISION_ATTRIBUTE_ID, new ImmutablePair<>("Meter Software Revision", null));
		ALL.put(UTILITY_NAME_ATTRIBUTE_ID, new ImmutablePair<>("Utility Name", null));
		ALL.put(POD_ATTRIBUTE_ID, new ImmutablePair<>("POD", null));
		ALL.put(AVAILABLE_POWER_ATTRIBUTE_ID, new ImmutablePair<>("Available Power", null));
		ALL.put(POWER_THRESHOLD_ATTRIBUTE_ID, new ImmutablePair<>("Power Threshold", null));
	}
}
