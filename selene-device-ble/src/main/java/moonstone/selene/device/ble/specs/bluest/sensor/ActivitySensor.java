package moonstone.selene.device.ble.specs.bluest.sensor;

import java.util.Collections;
import java.util.List;

import moonstone.selene.device.ble.BleUtils;
import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.specs.BlueST;
import moonstone.selene.device.ble.specs.BlueST.Feature;
import moonstone.selene.device.sensor.IntegerSensorData;

public class ActivitySensor extends BleSensorAbstract<ActivitySensorProperties, IntegerSensorData> {
	private static final String ACTIVITY = "activity";
	private static String UUID = BlueST.getUuid(Feature.ACTIVITY);

	{
		payloadSize = 1; // byte
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<IntegerSensorData> parseData(byte[] data, int offset) {
		return Collections.singletonList(new IntegerSensorData(ACTIVITY, BleUtils.byteUnsignedAtOffset(data, offset)));
	}

	@Override
	public List<IntegerSensorData> parseData(byte[] data) {
		// not implemented
		return null;
	}

	@Override
	public boolean isPassive() {
		return false;
	}

	@Override
	public String[] getUUIDs() {
		return new String[] { UUID };
	}

	@Override
	protected ActivitySensorProperties createProperties() {
		return new ActivitySensorProperties();
	}
}
