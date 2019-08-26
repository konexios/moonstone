package moonstone.selene.device.ble.thunderboard.sense.sensor;

import java.util.ArrayList;
import java.util.List;

import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.thunderboard.sense.GattConstants;
import moonstone.selene.device.sensor.IntegerSensorData;

public class RgbLedsControl extends BleSensorAbstract<RgbLedsControlProperties, IntegerSensorData> {
	public static final String LEDS = "leds";
	public static final String COLOR = "color";
	private static final String[] UUIDs = { GattConstants.UUID_RGB_LEDS };

	public RgbLedsControl() {
		super(LEDS);
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<IntegerSensorData> parseData(byte[] bytes) {
		List<IntegerSensorData> data = new ArrayList<>(2);
		data.add(new IntegerSensorData(LEDS, (int) bytes[0]));
		data.add(
		        new IntegerSensorData(COLOR, ((bytes[1] & 0xff) << 16) + ((bytes[2] & 0xff) << 8) + (bytes[3] & 0xff)));
		return data;
	}

	@Override
	protected RgbLedsControlProperties createProperties() {
		return new RgbLedsControlProperties();
	}

	@Override
	public void setTelemetry(String value) {
		getBluetoothGatt().controlSensor(GattConstants.UUID_RGB_LEDS, value);
	}

	@Override
	public String[] getUUIDs() {
		return UUIDs;
	}

	@Override
	public byte[] readValue() {
		return getBluetoothGatt().readValue(GattConstants.UUID_RGB_LEDS);
	}

	@Override
	public boolean isPassive() {
		return true;
	}
}
