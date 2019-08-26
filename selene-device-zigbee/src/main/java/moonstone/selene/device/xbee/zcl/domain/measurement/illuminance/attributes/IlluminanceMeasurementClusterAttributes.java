package moonstone.selene.device.xbee.zcl.domain.measurement.illuminance.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class IlluminanceMeasurementClusterAttributes {
	public static final int MEASURED_VALUE_ATTRIBUTE_ID = 0x0000;
	public static final int MIN_MEASURED_VALUE_ATTRIBUTE_ID = 0x0001;
	public static final int MAX_MEASURED_VALUE_ATTRIBUTE_ID = 0x0002;
	public static final int TOLERANCE_ATTRIBUTE_ID = 0x0003;
	public static final int LIGHT_SENSOR_TYPE_ATTRIBUTE_ID = 0x0004;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(MEASURED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Measured Value", MeasuredValue.INVALID));
		ALL.put(MIN_MEASURED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Min Measured Value", MeasuredValue.INVALID));
		ALL.put(MAX_MEASURED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Max Measured Value", MeasuredValue.INVALID));
		ALL.put(TOLERANCE_ATTRIBUTE_ID, new ImmutablePair<>("Tolerance", null));
		ALL.put(LIGHT_SENSOR_TYPE_ATTRIBUTE_ID, new ImmutablePair<>("Light Sensor Type", LightSensorType.RESERVED));
	}
}
