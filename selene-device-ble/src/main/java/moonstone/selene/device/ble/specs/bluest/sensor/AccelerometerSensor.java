package moonstone.selene.device.ble.specs.bluest.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moonstone.selene.device.ble.BleUtils;
import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.specs.BlueST;
import moonstone.selene.device.ble.specs.BlueST.Feature;
import moonstone.selene.device.sensor.FloatCubeSensorData;
import moonstone.selene.device.sensor.FloatSensorData;
import moonstone.selene.device.sensor.SensorDataAbstract;
import moonstone.selene.engine.EngineConstants;

public class AccelerometerSensor extends BleSensorAbstract<AccelerometerSensorProperties, SensorDataAbstract<?>> {
	private static final String ACCELEROMETER_X = "accelerometerX";
	private static final String ACCELEROMETER_Y = "accelerometerY";
	private static final String ACCELEROMETER_Z = "accelerometerZ";
	private static final String ACCELEROMETER_XYZ = "accelerometerXYZ";
	private static final String UUID = BlueST.getUuid(Feature.ACCELEROMETER);

	{
		payloadSize = 6;
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<SensorDataAbstract<?>> parseData(byte[] data, int offset) {
		List<SensorDataAbstract<?>> list = new ArrayList<>(3);
		float accX = BleUtils.shortSignedAtOffset(data, 0 + offset);
		float accY = BleUtils.shortSignedAtOffset(data, 2 + offset);
		float accZ = BleUtils.shortSignedAtOffset(data, 4 + offset);
		list.add(new FloatSensorData(ACCELEROMETER_X, accX, EngineConstants.FORMAT_DECIMAL_8));
		list.add(new FloatSensorData(ACCELEROMETER_Y, accY, EngineConstants.FORMAT_DECIMAL_8));
		list.add(new FloatSensorData(ACCELEROMETER_Z, accZ, EngineConstants.FORMAT_DECIMAL_8));
		list.add(new FloatCubeSensorData(ACCELEROMETER_XYZ, new float[] { accX, accY, accZ },
		        EngineConstants.FORMAT_DECIMAL_8));
		return Collections.unmodifiableList(list);
	}

	@Override
	public List<SensorDataAbstract<?>> parseData(byte[] data) {
		// not implemented
		return null;
	}

	@Override
	public boolean isPassive() {
		return false;
	}

	@Override
	public String[] getUUIDs() {
		return new String[] { UUID };
	}

	@Override
	protected AccelerometerSensorProperties createProperties() {
		return new AccelerometerSensorProperties();
	}
}
