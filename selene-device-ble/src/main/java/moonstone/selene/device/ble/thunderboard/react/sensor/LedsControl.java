package moonstone.selene.device.ble.thunderboard.react.sensor;

import java.util.Collections;
import java.util.List;

import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.thunderboard.react.GattConstants;
import moonstone.selene.device.sensor.IntegerSensorData;

public class LedsControl extends BleSensorAbstract<LedsControlProperties, IntegerSensorData> {
	public static final String LEDS = "leds";
	private static final String[] UUIDs = { GattConstants.UUID_LEDS_CONTROL };

	public LedsControl() {
		super(LEDS);
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<IntegerSensorData> parseData(byte[] bytes) {
		return Collections.singletonList(new IntegerSensorData(LEDS, (int) bytes[0]));
	}

	@Override
	protected LedsControlProperties createProperties() {
		return new LedsControlProperties();
	}

	@Override
	public void setTelemetry(String value) {
		getBluetoothGatt().controlSensor(GattConstants.UUID_LEDS_CONTROL, value);
	}

	@Override
	public String[] getUUIDs() {
		return UUIDs;
	}

	@Override
	public byte[] readValue() {
		return getBluetoothGatt().readValue(GattConstants.UUID_LEDS_CONTROL);
	}

	@Override
	public boolean isPassive() {
		return true;
	}
}
