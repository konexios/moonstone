package com.arrow.selene.device.xbee.zcl.domain.general.basic.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.sensor.SensorData;
import com.arrow.selene.device.xbee.zcl.data.Attribute;

public class BasicClusterAttributes {
	public static final int ZCL_VERSION_ATTRIBUTE_ID = 0x0000;
	public static final int APPLICATION_VERSION_ATTRIBUTE_ID = 0x0001;
	public static final int STACK_VERSION_ATTRIBUTE_ID = 0x0002;
	public static final int HW_VERSION_ATTRIBUTE_ID = 0x0003;
	public static final int MANUFACTURER_NAME_ATTRIBUTE_ID = 0x0004;
	public static final int MODEL_IDENTIFIER_ATTRIBUTE_ID = 0x0005;
	public static final int DATE_CODE_ATTRIBUTE_ID = 0x0006;
	public static final int POWER_SOURCE_ATTRIBUTE_ID = 0x0007;
	public static final int APPLICATION_PROFILE_VERSION_ATTRIBUTE_ID = 0x0008;

	public static final int LOCATION_DESCRIPTION_ATTRIBUTE_ID = 0x0010;
	public static final int PHYSICAL_ENVIRONMENT_ATTRIBUTE_ID = 0x0011;
	public static final int DEVICE_ENABLED_ATTRIBUTE_ID = 0x0012;
	public static final int ALARM_MASK_ATTRIBUTE_ID = 0x0013;
	public static final int DISABLE_LOCAL_CONFIG_ATTRIBUTE_ID = 0x0014;

	public static final int SW_BUILD_ID_ATTRIBUTE_ID = 0x4000;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(ZCL_VERSION_ATTRIBUTE_ID, new ImmutablePair<>("ZCL Version", null));
		ALL.put(APPLICATION_VERSION_ATTRIBUTE_ID, new ImmutablePair<>("Application Version", null));
		ALL.put(STACK_VERSION_ATTRIBUTE_ID, new ImmutablePair<>("Stack Version", null));
		ALL.put(HW_VERSION_ATTRIBUTE_ID, new ImmutablePair<>("Hardware Version", null));
		ALL.put(MANUFACTURER_NAME_ATTRIBUTE_ID, new ImmutablePair<>("Manufacturer Name", null));
		ALL.put(MODEL_IDENTIFIER_ATTRIBUTE_ID, new ImmutablePair<>("Model Identifier", null));
		ALL.put(DATE_CODE_ATTRIBUTE_ID, new ImmutablePair<>("Date Code", null));
		ALL.put(POWER_SOURCE_ATTRIBUTE_ID, new ImmutablePair<>("Power Source", PowerSource.UNKNOWN));
		ALL.put(APPLICATION_PROFILE_VERSION_ATTRIBUTE_ID, new ImmutablePair<>("Application Profile Version", null));
		ALL.put(LOCATION_DESCRIPTION_ATTRIBUTE_ID, new ImmutablePair<>("Location Description", null));
		ALL.put(PHYSICAL_ENVIRONMENT_ATTRIBUTE_ID,
				new ImmutablePair<>("Physical Environment", PhysicalEnvironment.RESERVED));
		ALL.put(DEVICE_ENABLED_ATTRIBUTE_ID, new ImmutablePair<>("Device Enabled", null));
		ALL.put(ALARM_MASK_ATTRIBUTE_ID, new ImmutablePair<>("Alarm Mask", AlarmMask.GENERAL_HARDWARE_FAULT));
		ALL.put(DISABLE_LOCAL_CONFIG_ATTRIBUTE_ID,
				new ImmutablePair<>("Disable Local Config", DisableLocalConfig.DEVICE_CONFIG_DISABLED));
		ALL.put(SW_BUILD_ID_ATTRIBUTE_ID, new ImmutablePair<>("SW Build ID", null));
	}
}
