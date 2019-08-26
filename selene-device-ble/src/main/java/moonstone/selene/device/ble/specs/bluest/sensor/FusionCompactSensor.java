package moonstone.selene.device.ble.specs.bluest.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moonstone.selene.device.ble.BleUtils;
import moonstone.selene.device.ble.sensor.BleSensorAbstract;
import moonstone.selene.device.ble.specs.BlueST;
import moonstone.selene.device.ble.specs.BlueST.Feature;
import moonstone.selene.device.sensor.FloatSensorData;
import moonstone.selene.engine.EngineConstants;

public class FusionCompactSensor extends BleSensorAbstract<FusionCompactSensorProperties, FloatSensorData> {
	private static final float SCALE = 10000.0f;
	private static final String FUSION = "fusionCompact";
	private static final String UUID = BlueST.getUuid(Feature.SENSOR_FUSION_COMPACT);

	{
		payloadSize = 18; // bytes
	}

	@Override
	public void setPeriod(int period) {
		// not applicable
	}

	@Override
	public List<FloatSensorData> parseData(byte[] bytes, int offset) {
		List<FloatSensorData> result = new ArrayList<>(4);
		float qi = BleUtils.shortSignedAtOffset(bytes, offset) / SCALE;
		float qj = BleUtils.shortSignedAtOffset(bytes, 2 + offset) / SCALE;
		float qk = BleUtils.shortSignedAtOffset(bytes, 4 + offset) / SCALE;
		float qs = getQs(qi, qj, qk);
		result.add(new FloatSensorData(FUSION + 'X', qi, EngineConstants.FORMAT_DECIMAL_8));
		result.add(new FloatSensorData(FUSION + 'Y', qj, EngineConstants.FORMAT_DECIMAL_8));
		result.add(new FloatSensorData(FUSION + 'Z', qk, EngineConstants.FORMAT_DECIMAL_8));
		result.add(new FloatSensorData(FUSION + 'W', qs, EngineConstants.FORMAT_DECIMAL_8));
		return Collections.unmodifiableList(result);
	}

	@Override
	public List<FloatSensorData> parseData(byte[] bytes) {
		// not implemented
		return null;
	}

	private static float getQs(float qi, float qj, float qk) {
		float t = 1.0f - (qi * qi + qj * qj + qk * qk);
		return t > 0.0f ? (float) (Math.sqrt(t)) : 0.0f;
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
	protected FusionCompactSensorProperties createProperties() {
		return new FusionCompactSensorProperties();
	}
}
