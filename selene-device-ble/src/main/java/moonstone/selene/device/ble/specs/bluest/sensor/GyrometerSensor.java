package moonstone.selene.device.ble.specs.bluest.sensor;

import java.util.ArrayList;
import java.util.List;

import moonstone.selene.device.ble.BleUtils;
import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.specs.BlueST;
import moonstone.selene.device.ble.specs.BlueST.Feature;
import moonstone.selene.device.sensor.FloatCubeSensorData;
import moonstone.selene.device.sensor.FloatSensorData;
import moonstone.selene.device.sensor.SensorDataAbstract;
import moonstone.selene.engine.EngineConstants;

public class GyrometerSensor extends BleSensorAbstract<GyrometerSensorProperties, SensorDataAbstract<?>> {
	private static final String GYROMETER_X = "gyrometerX";
	private static final String GYROMETER_Y = "gyrometerY";
	private static final String GYROMETER_Z = "gyrometerZ";
	private static final String GYROMETER_XYZ = "gyrometerXYZ";
	private static final float GYROSCOPE_SCALE = 10.0f;
	private static final String UUID = BlueST.getUuid(Feature.GYROMETER);

	{
		payloadSize = 6; // bytes
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<SensorDataAbstract<?>> parseData(byte[] data, int offset) {
		List<SensorDataAbstract<?>> list = new ArrayList<>(3);
		float gyroX = BleUtils.shortSignedAtOffset(data, offset) / GYROSCOPE_SCALE;
		float gyroY = BleUtils.shortSignedAtOffset(data, 2 + offset) / GYROSCOPE_SCALE;
		float gyroZ = BleUtils.shortSignedAtOffset(data, 4 + offset) / GYROSCOPE_SCALE;
		list.add(new FloatSensorData(GYROMETER_X, gyroX, EngineConstants.FORMAT_DECIMAL_8));
		list.add(new FloatSensorData(GYROMETER_Y, gyroY, EngineConstants.FORMAT_DECIMAL_8));
		list.add(new FloatSensorData(GYROMETER_Z, gyroZ, EngineConstants.FORMAT_DECIMAL_8));
		list.add(new FloatCubeSensorData(GYROMETER_XYZ, new float[] { gyroX, gyroY, gyroZ },
		        EngineConstants.FORMAT_DECIMAL_8));
		return list;
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
	protected GyrometerSensorProperties createProperties() {
		return new GyrometerSensorProperties();
	}
}
