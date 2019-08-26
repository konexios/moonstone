package moonstone.selene.device.ble.thunderboard.sense.sensor;

import java.util.Collections;
import java.util.List;

import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.thunderboard.sense.GattConstants;
import moonstone.selene.device.sensor.IntegerSensorData;

public class UvSensor extends BleSensorAbstract<UvSensorProperties, IntegerSensorData> {
	private static final String UV = "uv";
	private static final String[] UUIDs = { GattConstants.UUID_UV_SENSOR };

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<IntegerSensorData> parseData(byte[] data) {
		return Collections.singletonList(new IntegerSensorData(UV, (int) data[0]));
	}

	@Override
	protected UvSensorProperties createProperties() {
		return new UvSensorProperties();
	}

	@Override
	public byte[] readValue() {
		return getBluetoothGatt().readValue(GattConstants.UUID_UV_SENSOR);
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
