package moonstone.selene.device.xbee.zcl.domain.general.power.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class PowerConfigClusterAttributes {
	public static final int MAINS_VOLTAGE_ATTRIBUTE_ID = 0x0000;
	public static final int MAINS_FREQUENCY_ATTRIBUTE_ID = 0x0001;

	public static final int MAINS_ALARM_MASK_ATTRIBUTE_ID = 0x0010;
	public static final int MAINS_VOLTAGE_MIN_THRESHOLD_ATTRIBUTE_ID = 0x0011;
	public static final int MAINS_VOLTAGE_MAX_THRESHOLD_ATTRIBUTE_ID = 0x0012;
	public static final int MAINS_VOLTAGE_DWELL_TRIP_POINT_ATTRIBUTE_ID = 0x0013;

	public static final int BATTERY_VOLTAGE_ATTRIBUTE_ID = 0x0020;
	public static final int BATTERY_PERCENTAGE_REMAINING_ATTRIBUTE_ID = 0x0021;

	public static final int BATTERY_MANUFACTURER_ATTRIBUTE_ID = 0x0030;
	public static final int BATTERY_SIZE_ATTRIBUTE_ID = 0x0031;
	public static final int BATTERY_A_HR_RATING_ATTRIBUTE_ID = 0x0032;
	public static final int BATTERY_QUANTITY_ATTRIBUTE_ID = 0x0033;
	public static final int BATTERY_RATED_VOLTAGE_ATTRIBUTE_ID = 0x0034;
	public static final int BATTERY_ALARM_MASK_ATTRIBUTE_ID = 0x0035;
	public static final int BATTERY_VOLTAGE_MIN_THRESHOLD_ATTRIBUTE_ID = 0x0036;
	public static final int BATTERY_VOLTAGE_THRESHOLD1_ATTRIBUTE_ID = 0x0037;
	public static final int BATTERY_VOLTAGE_THRESHOLD2_ATTRIBUTE_ID = 0x0038;
	public static final int BATTERY_VOLTAGE_THRESHOLD3_ATTRIBUTE_ID = 0x0039;
	public static final int BATTERY_PERCENTAGE_MIN_THRESHOLD_ATTRIBUTE_ID = 0x003a;
	public static final int BATTERY_PERCENTAGE_THRESHOLD1_ATTRIBUTE_ID = 0x003b;
	public static final int BATTERY_PERCENTAGE_THRESHOLD2_ATTRIBUTE_ID = 0x003c;
	public static final int BATTERY_PERCENTAGE_THRESHOLD3_ATTRIBUTE_ID = 0x003d;
	public static final int BATTERY_ALARM_STATE_ATTRIBUTE_ID = 0x003e;

	public static final int BATTERY2_VOLTAGE_ATTRIBUTE_ID = 0x0040;
	public static final int BATTERY2_PERCENTAGE_REMAINING_ATTRIBUTE_ID = 0x0041;

	public static final int BATTERY2_MANUFACTURER_ATTRIBUTE_ID = 0x0050;
	public static final int BATTERY2_SIZE_ATTRIBUTE_ID = 0x0051;
	public static final int BATTERY2_A_HR_RATING_ATTRIBUTE_ID = 0x0052;
	public static final int BATTERY2_QUANTITY_ATTRIBUTE_ID = 0x0053;
	public static final int BATTERY2_RATED_VOLTAGE_ATTRIBUTE_ID = 0x0054;
	public static final int BATTERY2_ALARM_MASK_ATTRIBUTE_ID = 0x0055;
	public static final int BATTERY2_VOLTAGE_MIN_THRESHOLD_ATTRIBUTE_ID = 0x0056;
	public static final int BATTERY2_VOLTAGE_THRESHOLD1_ATTRIBUTE_ID = 0x0057;
	public static final int BATTERY2_VOLTAGE_THRESHOLD2_ATTRIBUTE_ID = 0x0058;
	public static final int BATTERY2_VOLTAGE_THRESHOLD3_ATTRIBUTE_ID = 0x0059;
	public static final int BATTERY2_PERCENTAGE_MIN_THRESHOLD_ATTRIBUTE_ID = 0x005a;
	public static final int BATTERY2_PERCENTAGE_THRESHOLD1_ATTRIBUTE_ID = 0x005b;
	public static final int BATTERY2_PERCENTAGE_THRESHOLD2_ATTRIBUTE_ID = 0x005c;
	public static final int BATTERY2_PERCENTAGE_THRESHOLD3_ATTRIBUTE_ID = 0x005d;
	public static final int BATTERY2_ALARM_STATE_ATTRIBUTE_ID = 0x005e;

	public static final int BATTERY3_VOLTAGE_ATTRIBUTE_ID = 0x0060;
	public static final int BATTERY3_PERCENTAGE_REMAINING_ATTRIBUTE_ID = 0x0061;

	public static final int BATTERY3_MANUFACTURER_ATTRIBUTE_ID = 0x0070;
	public static final int BATTERY3_SIZE_ATTRIBUTE_ID = 0x0071;
	public static final int BATTERY3_A_HR_RATING_ATTRIBUTE_ID = 0x0072;
	public static final int BATTERY3_QUANTITY_ATTRIBUTE_ID = 0x0073;
	public static final int BATTERY3_RATED_VOLTAGE_ATTRIBUTE_ID = 0x0074;
	public static final int BATTERY3_ALARM_MASK_ATTRIBUTE_ID = 0x0075;
	public static final int BATTERY3_VOLTAGE_MIN_THRESHOLD_ATTRIBUTE_ID = 0x0076;
	public static final int BATTERY3_VOLTAGE_THRESHOLD1_ATTRIBUTE_ID = 0x0077;
	public static final int BATTERY3_VOLTAGE_THRESHOLD2_ATTRIBUTE_ID = 0x0078;
	public static final int BATTERY3_VOLTAGE_THRESHOLD3_ATTRIBUTE_ID = 0x0079;
	public static final int BATTERY3_PERCENTAGE_MIN_THRESHOLD_ATTRIBUTE_ID = 0x007a;
	public static final int BATTERY3_PERCENTAGE_THRESHOLD1_ATTRIBUTE_ID = 0x007b;
	public static final int BATTERY3_PERCENTAGE_THRESHOLD2_ATTRIBUTE_ID = 0x007c;
	public static final int BATTERY3_PERCENTAGE_THRESHOLD3_ATTRIBUTE_ID = 0x007d;
	public static final int BATTERY3_ALARM_STATE_ATTRIBUTE_ID = 0x007e;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(MAINS_VOLTAGE_ATTRIBUTE_ID, new ImmutablePair<>("Mains Voltage", new MainsVoltage()));
		ALL.put(MAINS_FREQUENCY_ATTRIBUTE_ID, new ImmutablePair<>("Mains Frequency", MainsFrequency.MEASURED));
		ALL.put(MAINS_ALARM_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("Mains Alarm", MainsAlarmMask.MAINS_VOLTAGE_TOO_HIGH));
		ALL.put(MAINS_VOLTAGE_MIN_THRESHOLD_ATTRIBUTE_ID,
				new ImmutablePair<>("Mains Voltage Min Threshold", new MainsVoltageMinThreshold()));
		ALL.put(MAINS_VOLTAGE_MAX_THRESHOLD_ATTRIBUTE_ID,
				new ImmutablePair<>("Mains Voltage Max Threshold", new MainsVoltageMaxThreshold()));
		ALL.put(MAINS_VOLTAGE_DWELL_TRIP_POINT_ATTRIBUTE_ID,
				new ImmutablePair<>("Mains Voltage Dwell Trip Point", null));
		ALL.put(BATTERY_VOLTAGE_ATTRIBUTE_ID, new ImmutablePair<>("Battery Voltage", new BatteryVoltage()));
		ALL.put(BATTERY_PERCENTAGE_REMAINING_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery Percentage Remaining", new BatteryPercentageRemaining()));
		ALL.put(BATTERY_MANUFACTURER_ATTRIBUTE_ID, new ImmutablePair<>("Battery Manufacturer", null));
		ALL.put(BATTERY_SIZE_ATTRIBUTE_ID, new ImmutablePair<>("Battery Size", BatterySize.RESERVED));
		ALL.put(BATTERY_A_HR_RATING_ATTRIBUTE_ID, new ImmutablePair<>("Battery AHr Rating", new BatteryAHrRating()));
		ALL.put(BATTERY_QUANTITY_ATTRIBUTE_ID, new ImmutablePair<>("Battery Quantity", null));
		ALL.put(BATTERY_RATED_VOLTAGE_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery Rated Voltage", new BatteryRatedVoltage()));
		ALL.put(BATTERY_ALARM_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery Alarm Mask", BatteryAlarmMask.BATTERY_VOLTAGE_TOO_LOW));
		ALL.put(BATTERY_VOLTAGE_MIN_THRESHOLD_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery Voltage Min Threshold", new BatteryVoltageMinThreshold()));
		ALL.put(BATTERY_VOLTAGE_THRESHOLD1_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery Voltage Threshold 1", new BatteryVoltageMinThreshold()));
		ALL.put(BATTERY_VOLTAGE_THRESHOLD2_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery Voltage Threshold 2", new BatteryVoltageMinThreshold()));
		ALL.put(BATTERY_VOLTAGE_THRESHOLD3_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery Voltage Threshold 3", new BatteryVoltageMinThreshold()));
		ALL.put(BATTERY_PERCENTAGE_MIN_THRESHOLD_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery Percentage Min Threshold", null));
		ALL.put(BATTERY_PERCENTAGE_THRESHOLD1_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery Percentage Threshold 1", null));
		ALL.put(BATTERY_PERCENTAGE_THRESHOLD2_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery Percentage Threshold 2", null));
		ALL.put(BATTERY_PERCENTAGE_THRESHOLD3_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery Percentage Threshold 3", null));
		ALL.put(BATTERY_ALARM_STATE_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery Alarm State", BatteryAlarmState.RESERVED_31));
		ALL.put(BATTERY2_VOLTAGE_ATTRIBUTE_ID, new ImmutablePair<>("Battery 2 Voltage", new BatteryVoltage()));
		ALL.put(BATTERY2_PERCENTAGE_REMAINING_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 2 Percentage Remaining", new BatteryPercentageRemaining()));
		ALL.put(BATTERY2_MANUFACTURER_ATTRIBUTE_ID, new ImmutablePair<>("Battery 2 Manufacturer", null));
		ALL.put(BATTERY2_SIZE_ATTRIBUTE_ID, new ImmutablePair<>("Battery 2 Size", BatterySize.RESERVED));
		ALL.put(BATTERY2_A_HR_RATING_ATTRIBUTE_ID, new ImmutablePair<>("Battery 2 AHr Rating", new BatteryAHrRating
				()));
		ALL.put(BATTERY2_QUANTITY_ATTRIBUTE_ID, new ImmutablePair<>("Battery 2 Quantity", null));
		ALL.put(BATTERY2_RATED_VOLTAGE_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 2 Rated Voltage", new BatteryRatedVoltage()));
		ALL.put(BATTERY2_ALARM_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 2 Alarm Mask", BatteryAlarmMask.BATTERY_VOLTAGE_TOO_LOW));
		ALL.put(BATTERY2_VOLTAGE_MIN_THRESHOLD_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 2 Voltage Min Threshold", new BatteryVoltageMinThreshold()));
		ALL.put(BATTERY2_VOLTAGE_THRESHOLD1_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 2 Voltage Threshold 1", new BatteryVoltageMinThreshold()));
		ALL.put(BATTERY2_VOLTAGE_THRESHOLD2_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 2 Voltage Threshold 2", new BatteryVoltageMinThreshold()));
		ALL.put(BATTERY2_VOLTAGE_THRESHOLD3_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 2 Voltage Threshold 3", new BatteryVoltageMinThreshold()));
		ALL.put(BATTERY2_PERCENTAGE_MIN_THRESHOLD_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 2 Percentage Min Threshold", null));
		ALL.put(BATTERY2_PERCENTAGE_THRESHOLD1_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 2 Percentage Threshold 1", null));
		ALL.put(BATTERY2_PERCENTAGE_THRESHOLD2_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 2 Percentage Threshold 2", null));
		ALL.put(BATTERY2_PERCENTAGE_THRESHOLD3_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 2 Percentage Threshold 3", null));
		ALL.put(BATTERY2_ALARM_STATE_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 2 Alarm State", BatteryAlarmState.RESERVED_31));
		ALL.put(BATTERY3_VOLTAGE_ATTRIBUTE_ID, new ImmutablePair<>("Battery 3 Voltage", new BatteryVoltage()));
		ALL.put(BATTERY3_PERCENTAGE_REMAINING_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 3 Percentage Remaining", new BatteryPercentageRemaining()));
		ALL.put(BATTERY3_MANUFACTURER_ATTRIBUTE_ID, new ImmutablePair<>("Battery 3 Manufacturer", null));
		ALL.put(BATTERY3_SIZE_ATTRIBUTE_ID, new ImmutablePair<>("Battery 3 Size", BatterySize.RESERVED));
		ALL.put(BATTERY3_A_HR_RATING_ATTRIBUTE_ID, new ImmutablePair<>("Battery 3 AHr Rating", new BatteryAHrRating
				()));
		ALL.put(BATTERY3_QUANTITY_ATTRIBUTE_ID, new ImmutablePair<>("Battery 3 Quantity", null));
		ALL.put(BATTERY3_RATED_VOLTAGE_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 3 Rated Voltage", new BatteryRatedVoltage()));
		ALL.put(BATTERY3_ALARM_MASK_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 3 Alarm Mask", BatteryAlarmMask.BATTERY_VOLTAGE_TOO_LOW));
		ALL.put(BATTERY3_VOLTAGE_MIN_THRESHOLD_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 3 Voltage Min Threshold", new BatteryVoltageMinThreshold()));
		ALL.put(BATTERY3_VOLTAGE_THRESHOLD1_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 3 Voltage Threshold 1", new BatteryVoltageMinThreshold()));
		ALL.put(BATTERY3_VOLTAGE_THRESHOLD2_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 3 Voltage Threshold 2", new BatteryVoltageMinThreshold()));
		ALL.put(BATTERY3_VOLTAGE_THRESHOLD3_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 3 Voltage Threshold 3", new BatteryVoltageMinThreshold()));
		ALL.put(BATTERY3_PERCENTAGE_MIN_THRESHOLD_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 3 Percentage Min Threshold", null));
		ALL.put(BATTERY3_PERCENTAGE_THRESHOLD1_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 3 Percentage Threshold 1", null));
		ALL.put(BATTERY3_PERCENTAGE_THRESHOLD2_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 3 Percentage Threshold 2", null));
		ALL.put(BATTERY3_PERCENTAGE_THRESHOLD3_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 3 Percentage Threshold 3", null));
		ALL.put(BATTERY3_ALARM_STATE_ATTRIBUTE_ID,
				new ImmutablePair<>("Battery 3 Alarm State", BatteryAlarmState.RESERVED_31));
	}
}
