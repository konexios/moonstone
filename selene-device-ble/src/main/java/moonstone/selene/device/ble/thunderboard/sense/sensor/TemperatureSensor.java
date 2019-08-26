package moonstone.selene.device.ble.thunderboard.sense.sensor;

import java.util.Collections;
import java.util.List;

import moonstone.selene.device.ble.BleUtils;
import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.thunderboard.sense.GattConstants;
import moonstone.selene.device.sensor.FloatSensorData;
import moonstone.selene.engine.EngineConstants;
import moonstone.selene.engine.Utils;

public class TemperatureSensor extends BleSensorAbstract<TemperatureSensorProperties, FloatSensorData> {
	private static final float SCALE = 100.0f;
	private static final String TEMPERATURE = "temperature";
	private static final String[] UUIDs = { GattConstants.UUID_TEMPERATURE_SENSOR };

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<FloatSensorData> parseData(byte[] data) {
		float value = Utils.celsiusToFahrenheit(BleUtils.shortUnsignedAtOffset(data, 0) / SCALE);
		return Collections.singletonList(new FloatSensorData(TEMPERATURE, value, EngineConstants.FORMAT_DECIMAL_2));
	}

	@Override
	protected TemperatureSensorProperties createProperties() {
		return new TemperatureSensorProperties();
	}

	@Override
	public byte[] readValue() {
		return getBluetoothGatt().readValue(GattConstants.UUID_TEMPERATURE_SENSOR);
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
