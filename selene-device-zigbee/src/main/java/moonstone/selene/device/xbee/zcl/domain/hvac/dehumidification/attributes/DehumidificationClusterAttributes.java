package moonstone.selene.device.xbee.zcl.domain.hvac.dehumidification.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class DehumidificationClusterAttributes {
	public static final int RELATIVE_HUMIDITY_ATTRIBUTE_ID = 0x0000;
	public static final int DEHUMIDIFICATION_COOLING_ATTRIBUTE_ID = 0x0001;
	public static final int RH_DEHUMIDIFICATION_SETPOINT_ATTRIBUTE_ID = 0x0010;
	public static final int RELATIVE_HUMIDITY_MODE_ATTRIBUTE_ID = 0x0011;
	public static final int DEHUMIDIFICATION_LOCKOUT_ATTRIBUTE_ID = 0x0012;
	public static final int DEHUMIDIFICATION_HYSTERESIS_ATTRIBUTE_ID = 0x0013;
	public static final int DEHUMIDIFICATION_MAX_COOL_ATTRIBUTE_ID = 0x0014;
	public static final int RELATIVE_HUMIDITY_DISPLAY_ATTRIBUTE_ID = 0x0015;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(RELATIVE_HUMIDITY_ATTRIBUTE_ID, new ImmutablePair<>("Relative Humidity", null));
		ALL.put(DEHUMIDIFICATION_COOLING_ATTRIBUTE_ID, new ImmutablePair<>("Dehumidification Cooling", null));
		ALL.put(RH_DEHUMIDIFICATION_SETPOINT_ATTRIBUTE_ID, new ImmutablePair<>("RH Dehumidification Set Point", null));
		ALL.put(RELATIVE_HUMIDITY_MODE_ATTRIBUTE_ID,
				new ImmutablePair<>("Relative Humidity Mode", RelativeHumidityMode.RESERVED));
		ALL.put(DEHUMIDIFICATION_LOCKOUT_ATTRIBUTE_ID,
				new ImmutablePair<>("Dehumidification Lockout", DehumidificationLockout.RESERVED));
		ALL.put(DEHUMIDIFICATION_HYSTERESIS_ATTRIBUTE_ID, new ImmutablePair<>("Dehumidification Hysteresis", null));
		ALL.put(DEHUMIDIFICATION_MAX_COOL_ATTRIBUTE_ID, new ImmutablePair<>("Dehumidification Max Cool", null));
		ALL.put(RELATIVE_HUMIDITY_DISPLAY_ATTRIBUTE_ID,
				new ImmutablePair<>("Relative Humidity Display", RelativeHumidityDisplay.RESERVED));
	}
}
