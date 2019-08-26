package moonstone.selene.device.xbee.zcl.domain.measurement.temperature.attributes;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class TemperatureMeasurementClusterAttributes {
	public static final int MEASURED_VALUE_ATTRIBUTE_ID = 0x0000;
	public static final int MIN_MEASURED_VALUE_ATTRIBUTE_ID = 0x0001;
	public static final int MAX_MEASURED_VALUE_ATTRIBUTE_ID = 0x0002;
	public static final int TOLERANCE_ATTRIBUTE_ID = 0x0003;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL =
			new LinkedHashMap<>();

	static {
		ALL.put(MEASURED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Measured Value", new MeasuredValue()));
		ALL.put(MIN_MEASURED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Min Measured Value", new MeasuredValue()));
		ALL.put(MAX_MEASURED_VALUE_ATTRIBUTE_ID, new ImmutablePair<>("Max Measured Value", new MeasuredValue()));
		ALL.put(TOLERANCE_ATTRIBUTE_ID, new ImmutablePair<>("Tolerance", null));
	}
}
