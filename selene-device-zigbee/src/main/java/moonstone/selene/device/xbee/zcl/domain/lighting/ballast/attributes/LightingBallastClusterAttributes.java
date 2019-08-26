package moonstone.selene.device.xbee.zcl.domain.lighting.ballast.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class LightingBallastClusterAttributes {
	public static final int PHYSICAL_MIN_LEVEL_ATTRIBUTE_ID = 0x0000;
	public static final int PHYSICAL_MAX_LEVEL_ATTRIBUTE_ID = 0x0001;
	public static final int BALLAST_STATUS_ATTRIBUTE_ID = 0x0002;

	public static final int MIN_LEVEL_ATTRIBUTE_ID = 0x0010;
	public static final int MAX_LEVEL_ATTRIBUTE_ID = 0x0011;
	public static final int POWER_ON_LEVEL_ATTRIBUTE_ID = 0x0012;
	public static final int POWER_ON_FADE_TIME_ATTRIBUTE_ID = 0x0013;
	public static final int INTRINSIC_BALLAST_FACTOR_ATTRIBUTE_ID = 0x0014;
	public static final int BALLAST_FACTOR_ADJUSTMENT_ATTRIBUTE_ID = 0x0015;

	public static final int LAMP_QUANTITY_ATTRIBUTE_ID = 0x0020;

	public static final int LAMP_TYPE_ATTRIBUTE_ID = 0x0030;
	public static final int LAMP_MANUFACTURER_ATTRIBUTE_ID = 0x0031;
	public static final int LAMP_RATED_HOURS_ATTRIBUTE_ID = 0x0032;
	public static final int LAMP_BURN_HOURS_ATTRIBUTE_ID = 0x0033;
	public static final int LAMP_ALARM_MODE_ATTRIBUTE_ID = 0x0034;
	public static final int LAMP_BURN_HOURS_TRIP_POINT_ATTRIBUTE_ID = 0x0035;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(PHYSICAL_MIN_LEVEL_ATTRIBUTE_ID, new ImmutablePair<>("Physical Min Level", null));
		ALL.put(PHYSICAL_MAX_LEVEL_ATTRIBUTE_ID, new ImmutablePair<>("Physical Max Level", null));
		ALL.put(BALLAST_STATUS_ATTRIBUTE_ID, new ImmutablePair<>("Ballast Status", BallastStatus
				.ALL_LAMPS_IN_SOCKETS));
		ALL.put(MIN_LEVEL_ATTRIBUTE_ID, new ImmutablePair<>("Min Level", null));
		ALL.put(MAX_LEVEL_ATTRIBUTE_ID, new ImmutablePair<>("Max Level", null));
		ALL.put(POWER_ON_LEVEL_ATTRIBUTE_ID, new ImmutablePair<>("Power On Level", null));
		ALL.put(POWER_ON_FADE_TIME_ATTRIBUTE_ID, new ImmutablePair<>("Power On Fade Time", null));
		ALL.put(INTRINSIC_BALLAST_FACTOR_ATTRIBUTE_ID, new ImmutablePair<>("Intrinsic Ballast Factor", null));
		ALL.put(BALLAST_FACTOR_ADJUSTMENT_ATTRIBUTE_ID, new ImmutablePair<>("Ballast Factor Adjustment", null));
		ALL.put(LAMP_QUANTITY_ATTRIBUTE_ID, new ImmutablePair<>("Lamp Quantity", null));
		ALL.put(LAMP_TYPE_ATTRIBUTE_ID, new ImmutablePair<>("Lamp Type", null));
		ALL.put(LAMP_MANUFACTURER_ATTRIBUTE_ID, new ImmutablePair<>("Lamp Manufacturer", null));
		ALL.put(LAMP_RATED_HOURS_ATTRIBUTE_ID, new ImmutablePair<>("Lamp Rated Hours", null));
		ALL.put(LAMP_BURN_HOURS_ATTRIBUTE_ID, new ImmutablePair<>("Lamp Burn Hours", null));
		ALL.put(LAMP_ALARM_MODE_ATTRIBUTE_ID, new ImmutablePair<>("Lamp Alarm Mode", LampAlarmMode.LAMP_BURN_HOURS));
		ALL.put(LAMP_BURN_HOURS_TRIP_POINT_ATTRIBUTE_ID, new ImmutablePair<>("Lamp Burn Hours Trip Point", null));
	}
}
