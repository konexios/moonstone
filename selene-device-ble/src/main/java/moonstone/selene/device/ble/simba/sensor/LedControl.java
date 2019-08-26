package moonstone.selene.device.ble.simba.sensor;

import java.util.ArrayList;
import java.util.List;

import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.specs.BlueST;
import moonstone.selene.device.sensor.BooleanSensorData;

public class LedControl extends BleSensorAbstract<LedControlProperties, BooleanSensorData> {
	public static final String LED = "led";

	private static final String UUID = BlueST.UUID_FEATURE_COMMAND;

	public LedControl() {
		super(LED);
	}

	// @Override
	// public void enable() {
	// getBluetoothGatt().enableNotification(UUID,
	// GattConstants.ENABLE_NOTIFICATION);
	// super.enable();
	// }
	//
	// @Override
	// public void disable() {
	// getBluetoothGatt().disableNotification(UUID,
	// GattConstants.DISABLE_NOTIFICATION);
	// super.disable();
	// }

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<BooleanSensorData> parseData(byte[] bytes, int offset) {
		List<BooleanSensorData> data = new ArrayList<>(1);
		// if (bytes[2] == 32) {
		// Found notification for led state change
		data.add(new BooleanSensorData(LED, (bytes[4] > 0)));
		return data;
		// }
	}

	@Override
	public List<BooleanSensorData> parseData(byte[] bytes) {
		// not implemented
		return null;
	}

	@Override
	protected LedControlProperties createProperties() {
		return new LedControlProperties();
	}

	@Override
	public void setTelemetry(String value) {
		String method = "setTelemetry";
		try {
			getBluetoothGatt().controlSensor(UUID, value);
		} catch (Exception e) {
			logError(method, "Error:: %s", e.toString());
		}
	}

	@Override
	public String[] getUUIDs() {
		return new String[] { UUID };
	}

	@Override
	public boolean isPassive() {
		return true;
	}
}
