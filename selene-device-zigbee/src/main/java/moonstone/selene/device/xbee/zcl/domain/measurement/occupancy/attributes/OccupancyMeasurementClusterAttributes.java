package moonstone.selene.device.xbee.zcl.domain.measurement.occupancy.attributes;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import moonstone.selene.device.sensor.SensorData;
import moonstone.selene.device.xbee.zcl.data.Attribute;

public class OccupancyMeasurementClusterAttributes {
	public static final int OCCUPANCY_ATTRIBUTE_ID = 0x0000;
	public static final int OCCUPANCY_SENSOR_TYPE_ATTRIBUTE_ID = 0x0001;

	public static final int PIR_OCCUPIED_TO_UNOCCUPIED_DELAY_ATTRIBUTE_ID = 0x0010;
	public static final int PIR_UNOCCUPIED_TO_OCCUPIED_DELAY_ATTRIBUTE_ID = 0x0011;
	public static final int PIR_UNOCCUPIED_TO_OCCUPIED_THRESHOLD_ATTRIBUTE_ID = 0x0012;

	public static final int ULTRASONIC_OCCUPIED_TO_UNOCCUPIED_DELAY_ATTRIBUTE_ID = 0x0020;
	public static final int ULTRASONIC_UNOCCUPIED_TO_OCCUPIED_DELAY_ATTRIBUTE_ID = 0x0021;
	public static final int ULTRASONIC_UNOCCUPIED_TO_OCCUPIED_THRESHOLD_ATTRIBUTE_ID = 0x0022;

	public static final Map<Integer, ImmutablePair<String, Attribute<? extends SensorData<?>>>> ALL = new HashMap<>();

	static {
		ALL.put(OCCUPANCY_ATTRIBUTE_ID, new ImmutablePair<>("Occupancy", null));
		ALL.put(OCCUPANCY_SENSOR_TYPE_ATTRIBUTE_ID,
				new ImmutablePair<>("Occupancy Sensor Type", OccupancySensorType.RESERVED));
		ALL.put(PIR_OCCUPIED_TO_UNOCCUPIED_DELAY_ATTRIBUTE_ID,
				new ImmutablePair<>("PIR Occupied to Unoccupied Delay", null));
		ALL.put(PIR_UNOCCUPIED_TO_OCCUPIED_DELAY_ATTRIBUTE_ID,
				new ImmutablePair<>("PIR Unoccupied to Occupied Delay", null));
		ALL.put(PIR_UNOCCUPIED_TO_OCCUPIED_THRESHOLD_ATTRIBUTE_ID,
				new ImmutablePair<>("PIR Unoccupied to Occupied Threshold", null));
		ALL.put(ULTRASONIC_OCCUPIED_TO_UNOCCUPIED_DELAY_ATTRIBUTE_ID,
				new ImmutablePair<>("Ultrasonic Occupied to Unoccupied Delay", null));
		ALL.put(ULTRASONIC_UNOCCUPIED_TO_OCCUPIED_DELAY_ATTRIBUTE_ID,
				new ImmutablePair<>("Ultrasonic Unoccupied to Occupied Delay", null));
		ALL.put(ULTRASONIC_UNOCCUPIED_TO_OCCUPIED_THRESHOLD_ATTRIBUTE_ID,
				new ImmutablePair<>("Ultrasonic Unoccupied to Occupied Threshold", null));
	}
}
