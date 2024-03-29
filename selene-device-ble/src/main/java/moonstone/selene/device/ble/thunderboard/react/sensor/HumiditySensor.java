package moonstone.selene.device.ble.thunderboard.react.sensor;

import java.util.Collections;
import java.util.List;

import moonstone.selene.device.ble.BleUtils;
import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.thunderboard.react.GattConstants;
import moonstone.selene.device.sensor.FloatSensorData;
import moonstone.selene.engine.EngineConstants;

public class HumiditySensor extends BleSensorAbstract<HumiditySensorProperties, FloatSensorData> {
	private static final String HUMIDITY = "humidity";
	private static final float SCALE = 100.0f;
	private static final String[] UUIDs = { GattConstants.UUID_HUMIDITY_SENSOR };

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<FloatSensorData> parseData(byte[] data) {
		float value = BleUtils.shortUnsignedAtOffset(data, 0) / SCALE;
		return Collections.singletonList(new FloatSensorData(HUMIDITY, value, EngineConstants.FORMAT_DECIMAL_2));
	}

	@Override
	protected HumiditySensorProperties createProperties() {
		return new HumiditySensorProperties();
	}

	@Override
	public byte[] readValue() {
		return getBluetoothGatt().readValue(GattConstants.UUID_HUMIDITY_SENSOR);
	}

	@Override
	public String[] getUUIDs() {
		return UUIDs;
	}

	@Override
	public boolean isPassive() {
		return true;
	}
}
