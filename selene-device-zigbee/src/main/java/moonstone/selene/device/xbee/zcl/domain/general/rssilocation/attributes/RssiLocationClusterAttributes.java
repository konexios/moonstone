package moonstone.selene.device.xbee.zcl.domain.general.rssilocation.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class RssiLocationClusterAttributes {
	public static final int LOCATION_TYPE_ATTRIBUTE_ID = 0x0000;
	public static final int LOCATION_METHOD_ATTRIBUTE_ID = 0x0001;
	public static final int LOCATION_AGE_ATTRIBUTE_ID = 0x0002;
	public static final int QUALITY_MEASURE_ATTRIBUTE_ID = 0x0003;
	public static final int NUMBER_OF_DEVICES_ATTRIBUTE_ID = 0x0004;

	public static final int COORDINATE1_ATTRIBUTE_ID = 0x0010;
	public static final int COORDINATE2_ATTRIBUTE_ID = 0x0011;
	public static final int COORDINATE3_ATTRIBUTE_ID = 0x0012;
	public static final int POWER_ATTRIBUTE_ID = 0x0013;
	public static final int PATH_LOSS_EXPONENT_ATTRIBUTE_ID = 0x0014;
	public static final int REPORTING_PERIOD_ATTRIBUTE_ID = 0x0015;
	public static final int CALCULATION_PERIOD_ATTRIBUTE_ID = 0x0016;
	public static final int NUMBER_RSSI_MEASUREMENTS_ATTRIBUTE_ID = 0x0017;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(LOCATION_TYPE_ATTRIBUTE_ID, new ImmutablePair<>("Location Type", LocationType.RECTANGULAR));
		ALL.put(LOCATION_METHOD_ATTRIBUTE_ID, new ImmutablePair<>("Location Method", LocationMethod.RESERVED));
		ALL.put(LOCATION_AGE_ATTRIBUTE_ID, new ImmutablePair<>("Location Age", null));
		ALL.put(QUALITY_MEASURE_ATTRIBUTE_ID, new ImmutablePair<>("Quality Measure", null));
		ALL.put(NUMBER_OF_DEVICES_ATTRIBUTE_ID, new ImmutablePair<>("Number of Devices", null));
		ALL.put(COORDINATE1_ATTRIBUTE_ID, new ImmutablePair<>("Coordinate 1", new Coordinate123()));
		ALL.put(COORDINATE2_ATTRIBUTE_ID, new ImmutablePair<>("Coordinate 2", new Coordinate123()));
		ALL.put(COORDINATE3_ATTRIBUTE_ID, new ImmutablePair<>("Coordinate 3", new Coordinate123()));
		ALL.put(POWER_ATTRIBUTE_ID, new ImmutablePair<>("Power", new Power()));
		ALL.put(PATH_LOSS_EXPONENT_ATTRIBUTE_ID, new ImmutablePair<>("Path Loss Exponent", new PathLossExponent()));
		ALL.put(REPORTING_PERIOD_ATTRIBUTE_ID, new ImmutablePair<>("Reporting Period", null));
		ALL.put(CALCULATION_PERIOD_ATTRIBUTE_ID, new ImmutablePair<>("Calculation Period", null));
		ALL.put(NUMBER_RSSI_MEASUREMENTS_ATTRIBUTE_ID, new ImmutablePair<>("Number RSSI Measurements", null));
	}
}
