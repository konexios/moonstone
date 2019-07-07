package com.arrow.selene.device.ble.thunderboard.sense.sensor;

import java.util.Collections;
import java.util.List;

import com.arrow.selene.device.ble.BleUtils;
import com.arrow.selene.device.ble.sensor.BleSensorAbstract;
import com.arrow.selene.device.ble.thunderboard.sense.GattConstants;
import com.arrow.selene.device.sensor.IntegerSensorData;

public class TvocSensor extends BleSensorAbstract<HumiditySensorProperties, IntegerSensorData> {
	private static final String TVOC = "tvoc";
	private static final String[] UUIDs = { GattConstants.UUID_TVOC_READING_SENSOR };

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<IntegerSensorData> parseData(byte[] data) {
		return Collections.singletonList(new IntegerSensorData(TVOC, BleUtils.shortUnsignedAtOffset(data, 0)));
	}

	@Override
	protected HumiditySensorProperties createProperties() {
		return new HumiditySensorProperties();
	}

	@Override
	public byte[] readValue() {
		return getBluetoothGatt().readValue(GattConstants.UUID_TVOC_READING_SENSOR);
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
