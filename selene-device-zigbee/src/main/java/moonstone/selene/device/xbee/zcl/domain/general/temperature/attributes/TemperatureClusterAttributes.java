package moonstone.selene.device.xbee.zcl.domain.general.temperature.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class TemperatureClusterAttributes {
	public static final int CURRENT_TEMPERATURE_ATTRIBUTE_ID = 0x0000;
	public static final int MIN_TEMP_EXPERIENCED_ATTRIBUTE_ID = 0x0001;
	public static final int MAX_TEMP_EXPERIENCED_ATTRIBUTE_ID = 0x0002;
	public static final int OVER_TEMP_TOTAL_DWELL_ATTRIBUTE_ID = 0x0003;

	public static final int DEVICE_TEMP_ALARM_MASK_ATTRIBUTE_ID = 0x0010;
	public static final int LOW_TEMP_THRESHOLD_ATTRIBUTE_ID = 0x0011;
	public static final int HIGH_TEMP_THRESHOLD_ATTRIBUTE_ID = 0x0012;
	public static final int LOW_TEMP_DWELL_TRIP_POINT_ATTRIBUTE_ID = 0x0013;
	public static final int HIGH_TEMP_DWELL_TRIP_POINT_ATTRIBUTE_ID = 0x0014;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(CURRENT_TEMPERATURE_ATTRIBUTE_ID, new ImmutablePair<>("Current Temperature", null));
		ALL.put(MIN_TEMP_EXPERIENCED_ATTRIBUTE_ID, new ImmutablePair<>("Min Temperature Experienced", null));
		ALL.put(MAX_TEMP_EXPERIENCED_ATTRIBUTE_ID, new ImmutablePair<>("Max Temperature Experienced", null));
		ALL.put(OVER_TEMP_TOTAL_DWELL_ATTRIBUTE_ID, new ImmutablePair<>("Over Temperature Total Dwell", null));
		ALL.put(DEVICE_TEMP_ALARM_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("Device Temperature Alarm Mask", DeviceTempAlarmMask.DEVICE_TEMP_TOO_LOW));
		ALL.put(LOW_TEMP_THRESHOLD_ATTRIBUTE_ID, new ImmutablePair<>("Low Temperature Threshold", null));
		ALL.put(HIGH_TEMP_THRESHOLD_ATTRIBUTE_ID, new ImmutablePair<>("High Temperature Threshold", null));
		ALL.put(LOW_TEMP_DWELL_TRIP_POINT_ATTRIBUTE_ID, new ImmutablePair<>("Low Temperature Dwell Trip Point", null));
		ALL.put(HIGH_TEMP_DWELL_TRIP_POINT_ATTRIBUTE_ID,
				new ImmutablePair<>("High Temperature Dwell Trip Point", null));
	}
}
