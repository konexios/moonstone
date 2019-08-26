package moonstone.selene.device.ble.specs.bluest.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moonstone.selene.device.ble.BleUtils;
import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.specs.BlueST;
import moonstone.selene.device.ble.specs.BlueST.Feature;
import moonstone.selene.device.sensor.IntegerSensorData;

public class MicLevelSensor extends BleSensorAbstract<MicLevelSensorProperties, IntegerSensorData> {
	private static final String MIC_LEVEL = "micLevel";
	private static final String UUID = BlueST.getUuid(Feature.MIC_LEVEL);

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<IntegerSensorData> parseData(byte[] data, int offset) {
		int length = data.length;
		List<IntegerSensorData> result = new ArrayList<>(length);
		for (int i = offset; i < length; i++) {
			result.add(new IntegerSensorData(MIC_LEVEL + (i + 1), BleUtils.byteUnsignedAtOffset(data, i)));
		}
		return Collections.unmodifiableList(result);
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
	protected MicLevelSensorProperties createProperties() {
		return new MicLevelSensorProperties();
	}
}
