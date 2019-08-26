package moonstone.selene.device.ble.thunderboard.sense.sensor;

import java.util.Collections;
import java.util.List;

import moonstone.selene.device.ble.BleUtils;
import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.thunderboard.sense.GattConstants;
import moonstone.selene.device.sensor.IntegerSensorData;

public class CO2Sensor extends BleSensorAbstract<HumiditySensorProperties, IntegerSensorData> {
	private static final String CO2 = "co2";
	private static final String[] UUIDs = { GattConstants.UUID_CO2_READING_SENSOR };

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<IntegerSensorData> parseData(byte[] data) {
		return Collections.singletonList(new IntegerSensorData(CO2, BleUtils.shortUnsignedAtOffset(data, 0)));
	}

	@Override
	protected HumiditySensorProperties createProperties() {
		return new HumiditySensorProperties();
	}

	@Override
	public byte[] readValue() {
		return getBluetoothGatt().readValue(GattConstants.UUID_CO2_READING_SENSOR);
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
