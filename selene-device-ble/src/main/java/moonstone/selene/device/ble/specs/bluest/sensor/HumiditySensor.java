package moonstone.selene.device.ble.specs.bluest.sensor;

import java.util.Collections;
import java.util.List;

import moonstone.selene.device.ble.BleUtils;
import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.specs.BlueST;
import moonstone.selene.device.ble.specs.BlueST.Feature;
import moonstone.selene.device.sensor.FloatSensorData;
import moonstone.selene.engine.EngineConstants;

public class HumiditySensor extends BleSensorAbstract<HumiditySensorProperties, FloatSensorData> {
	private static final String HUMIDITY = "humidity";
	private static final float HUMIDITY_SCALE = 10.0f;
	private static final String UUID = BlueST.getUuid(Feature.HUMIDITY);

	{
		payloadSize = 2; // bytes
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<FloatSensorData> parseData(byte[] data, int offset) {
		return Collections.singletonList(new FloatSensorData(HUMIDITY,
		        BleUtils.shortSignedAtOffset(data, offset) / HUMIDITY_SCALE, EngineConstants.FORMAT_DECIMAL_2));
	}

	@Override
	public List<FloatSensorData> parseData(byte[] data) {
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
	protected HumiditySensorProperties createProperties() {
		return new HumiditySensorProperties();
	}
}
