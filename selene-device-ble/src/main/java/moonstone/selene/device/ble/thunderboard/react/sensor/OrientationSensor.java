package moonstone.selene.device.ble.thunderboard.react.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moonstone.selene.device.ble.BleUtils;
import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.thunderboard.react.GattConstants;
import moonstone.selene.device.sensor.FloatCubeSensorData;
import moonstone.selene.device.sensor.FloatSensorData;
import moonstone.selene.device.sensor.SensorDataAbstract;
import moonstone.selene.engine.EngineConstants;

public class OrientationSensor extends BleSensorAbstract<OrientationSensorProperties, SensorDataAbstract<?>> {
	private static final float SCALE = 100.0f;
	private static final String ORIENTATION_ALPHA = "orientationAlpha";
	private static final String ORIENTATION_BETA = "orientationBeta";
	private static final String ORIENTATION_GAMMA = "orientationGamma";
	private static final String ORIENTATION_ABG = "orientationABG";
	private static final String[] UUIDs = { GattConstants.UUID_ORIENTATION_SENSOR };

	@Override
	public void enable() {
		getBluetoothGatt().enableNotification(GattConstants.UUID_ORIENTATION_SENSOR, GattConstants.ENABLE_NOTIFICATION);
		super.enable();
	}

	@Override
	public void disable() {
		getBluetoothGatt().disableNotification(GattConstants.UUID_ORIENTATION_SENSOR,
		        GattConstants.DISABLE_NOTIFICATION);
		super.disable();
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<SensorDataAbstract<?>> parseData(byte[] bytes) {
		List<SensorDataAbstract<?>> result = new ArrayList<>(3);
		float x = BleUtils.shortSignedAtOffset(bytes, 0) / SCALE;
		result.add(new FloatSensorData(ORIENTATION_ALPHA, x, EngineConstants.FORMAT_DECIMAL_8));
		float y = BleUtils.shortSignedAtOffset(bytes, 2) / SCALE;
		result.add(new FloatSensorData(ORIENTATION_BETA, y, EngineConstants.FORMAT_DECIMAL_8));
		float z = BleUtils.shortSignedAtOffset(bytes, 4) / SCALE;
		result.add(new FloatSensorData(ORIENTATION_GAMMA, z, EngineConstants.FORMAT_DECIMAL_8));
		result.add(new FloatCubeSensorData(ORIENTATION_ABG, new float[] { x, y, z }, EngineConstants.FORMAT_DECIMAL_8));
		return Collections.unmodifiableList(result);
	}

	@Override
	public boolean isPassive() {
		return false;
	}

	@Override
	public String[] getUUIDs() {
		return UUIDs;
	}

	@Override
	protected OrientationSensorProperties createProperties() {
		return new OrientationSensorProperties();
	}
}
