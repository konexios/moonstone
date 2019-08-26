package moonstone.selene.device.ble.sensortag.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moonstone.selene.device.ble.BleUtils;
import moonstone.selene.device.ble.gatt.Gatt;
import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.sensortag.SensorTagGattConstants;
import moonstone.selene.device.sensor.FloatSensorData;
import moonstone.selene.engine.EngineConstants;

public class MovementSensor extends BleSensorAbstract<MovementSensorProperties, FloatSensorData> {
	private static final float ACCELERATION_SCALE = 4096.0f;
	private static final float GYROSCOPE_SCALE = 65535.0f / 500.0f;
	private static final float MAGNETIC_SCALE = 32768.0f / 4912.0f;
	private static final String ACCELEROMETER = "accelerometer";
	private static final String GYROSCOPE = "gyroscope";
	private static final String MAGNETOMETER = "magnetometer";
	private static final String[] UUIDs = { SensorTagGattConstants.UUID_MOVEMENT_SENSOR_DATA };
	private String configuration;

	@Override
	public void enable() {
		getBluetoothGatt().controlSensor(SensorTagGattConstants.UUID_MOVEMENT_SENSOR_CONFIG, configuration);
		getBluetoothGatt().enableNotification(SensorTagGattConstants.UUID_MOVEMENT_SENSOR_DATA,
		        SensorTagGattConstants.ENABLE_NOTIFICATION);
		super.enable();
	}

	@Override
	public void disable() {
		getBluetoothGatt().controlSensor(SensorTagGattConstants.UUID_MOVEMENT_SENSOR_CONFIG, "0000");
		getBluetoothGatt().disableNotification(SensorTagGattConstants.UUID_MOVEMENT_SENSOR_DATA,
		        SensorTagGattConstants.DISABLE_NOTIFICATION);
		super.disable();
	}

	@Override
	public void setPeriod(int period) {
		getBluetoothGatt().setPeriod(SensorTagGattConstants.UUID_MOVEMENT_SENSOR_PERIOD,
		        String.format("%02x", period / 10));
	}

	@Override
	public List<FloatSensorData> parseData(byte[] bytes) {
		List<FloatSensorData> result = new ArrayList<>(9);
		result.add(new FloatSensorData(ACCELEROMETER + 'X',
		        BleUtils.shortSignedAtOffset(bytes, 6) / ACCELERATION_SCALE * -1, EngineConstants.FORMAT_DECIMAL_8));
		result.add(new FloatSensorData(ACCELEROMETER + 'Y', BleUtils.shortSignedAtOffset(bytes, 8) / ACCELERATION_SCALE,
		        EngineConstants.FORMAT_DECIMAL_8));
		result.add(new FloatSensorData(ACCELEROMETER + 'Z',
		        BleUtils.shortSignedAtOffset(bytes, 10) / ACCELERATION_SCALE * -1, EngineConstants.FORMAT_DECIMAL_8));
		result.add(new FloatSensorData(GYROSCOPE + 'X', BleUtils.shortSignedAtOffset(bytes, 0) / GYROSCOPE_SCALE,
		        EngineConstants.FORMAT_DECIMAL_8));
		result.add(new FloatSensorData(GYROSCOPE + 'Y', BleUtils.shortSignedAtOffset(bytes, 2) / GYROSCOPE_SCALE,
		        EngineConstants.FORMAT_DECIMAL_8));
		result.add(new FloatSensorData(GYROSCOPE + 'Z', BleUtils.shortSignedAtOffset(bytes, 4) / GYROSCOPE_SCALE,
		        EngineConstants.FORMAT_DECIMAL_8));
		result.add(new FloatSensorData(MAGNETOMETER + 'X', BleUtils.shortSignedAtOffset(bytes, 12) / MAGNETIC_SCALE,
		        EngineConstants.FORMAT_DECIMAL_8));
		result.add(new FloatSensorData(MAGNETOMETER + 'Y', BleUtils.shortSignedAtOffset(bytes, 14) / MAGNETIC_SCALE,
		        EngineConstants.FORMAT_DECIMAL_8));
		result.add(new FloatSensorData(MAGNETOMETER + 'Z', BleUtils.shortSignedAtOffset(bytes, 16) / MAGNETIC_SCALE,
		        EngineConstants.FORMAT_DECIMAL_8));
		return Collections.unmodifiableList(result);
	}

	@Override
	public void configure(Gatt bluetoothGatt) {
		setBluetoothGatt(bluetoothGatt);
		if (getProperties().isAccelerometerEnabled() || getProperties().isGyroscopeEnabled()
		        || getProperties().isMagnetometerEnabled()) {
			setConfiguration(getProperties().isAccelerometerEnabled(), getProperties().isGyroscopeEnabled(),
			        getProperties().isMagnetometerEnabled(), getProperties().getAccelerometerRange());
			enable();
			setPeriod(getProperties().getPeriod());
		} else {
			disable();
		}
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
	protected MovementSensorProperties createProperties() {
		return new MovementSensorProperties();
	}

	private void setConfiguration(boolean enableAccelerometer, boolean enableGyroscope, boolean enableMagnetometer,
	        int accelerometerRange) {
		int movementConfig = 0;
		if (enableAccelerometer) {
			movementConfig |= 0b00111000_00000000;
		}
		if (enableGyroscope) {
			movementConfig |= 0b00000111_00000000;
		}
		if (enableMagnetometer) {
			movementConfig |= 0b01000000_00000000;
		}
		if (accelerometerRange < 0) {
			accelerometerRange = 0;
		}
		if (accelerometerRange > 3) {
			accelerometerRange = 3;
		}
		movementConfig |= accelerometerRange;

		configuration = String.format("%04x", movementConfig);
	}
}
