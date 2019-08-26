package moonstone.selene.device.ble.sensortag.sensor;

import java.util.Collections;
import java.util.List;

import moonstone.selene.device.ble.BleUtils;
import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.sensortag.SensorTagGattConstants;
import moonstone.selene.device.sensor.FloatSensorData;
import moonstone.selene.engine.EngineConstants;

public class LightSensor extends BleSensorAbstract<LightSensorProperties, FloatSensorData> {
	private static final String LIGHT = "light";
	private static final String[] UUIDs = { SensorTagGattConstants.UUID_OPTICAL_SENSOR_DATA };

	@Override
	public void enable() {
		getBluetoothGatt().controlSensor(SensorTagGattConstants.UUID_OPTICAL_SENSOR_CONFIG, "01");
		getBluetoothGatt().enableNotification(SensorTagGattConstants.UUID_OPTICAL_SENSOR_DATA,
		        SensorTagGattConstants.ENABLE_NOTIFICATION);
		super.enable();
	}

	@Override
	public void disable() {
		getBluetoothGatt().controlSensor(SensorTagGattConstants.UUID_OPTICAL_SENSOR_CONFIG, "00");
		getBluetoothGatt().disableNotification(SensorTagGattConstants.UUID_OPTICAL_SENSOR_DATA,
		        SensorTagGattConstants.DISABLE_NOTIFICATION);
		super.disable();
	}

	@Override
	public void setPeriod(int period) {
		getBluetoothGatt().setPeriod(SensorTagGattConstants.UUID_OPTICAL_SENSOR_PERIOD,
		        String.format("%02x", period / 10));
	}

	@Override
	public List<FloatSensorData> parseData(byte[] bytes) {
		int value = BleUtils.shortUnsignedAtOffset(bytes, 0);
		int mantissa = value & 0x0FFF;
		int exponent = (value & 0xF000) >> 12;
		float light = (float) (mantissa * (0.01 * Math.pow(2.0, exponent)));
		return Collections.singletonList(new FloatSensorData(LIGHT, light, EngineConstants.FORMAT_DECIMAL_2));
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
	protected LightSensorProperties createProperties() {
		return new LightSensorProperties();
	}
}
