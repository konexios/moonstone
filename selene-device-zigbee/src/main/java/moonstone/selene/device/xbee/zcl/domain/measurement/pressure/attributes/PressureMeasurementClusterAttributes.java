package moonstone.selene.device.xbee.zcl.domain.measurement.pressure.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class PressureMeasurementClusterAttributes {
	public static final int MEASURED_VALUE_ATTRIBUTE_ID = 0x0000;
	public static final int MIN_MEASURED_VALUE_ATTRIBUTE_ID = 0x0001;
	public static final int MAX_MEASURED_VALUE_ATTRIBUTE_ID = 0x0002;
	public static final int TOLERANCE_ATTRIBUTE_ID = 0x0003;

	public static final int SCALED_VALUE_ATTRIBUTE_ID = 0x0010;
	public static final int MIN_SCALED_VALUE_ATTRIBUTE_ID = 0x0011;
	public static final int MAX_SCALED_VALUE_ATTRIBUTE_ID = 0x0012;
	public static final int SCALED_TOLERANCE_ATTRIBUTE_ID = 0x0013;
	public static final int SCALE_ATTRIBUTE_ID = 0x0014;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(MEASURED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Measured Value", new MeasuredValue()));
		ALL.put(MIN_MEASURED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Min Measured Value", new MeasuredValue()));
		ALL.put(MAX_MEASURED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Max Measured Value", new MeasuredValue()));
		ALL.put(TOLERANCE_ATTRIBUTE_ID, new ImmutablePair<>("Tolerance", null));
		ALL.put(SCALED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Scaled Value", new ScaledValue()));
		ALL.put(MIN_SCALED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Min Scaled Value", new ScaledValue()));
		ALL.put(MAX_SCALED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Max Scaled value", new ScaledValue()));
		ALL.put(SCALED_TOLERANCE_ATTRIBUTE_ID, new ImmutablePair<>("Scaled Tolerance", null));
		ALL.put(SCALE_ATTRIBUTE_ID, new ImmutablePair<>("Scale", null));
	}
}
