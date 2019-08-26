package moonstone.selene.device.libelium;

import moonstone.selene.device.libelium.data.SensorParser;
import moonstone.selene.engine.DeviceModule;

public interface WaspmoteModule extends DeviceModule<WaspmoteInfo, WaspmoteProperties, WaspmoteStates,
		WaspmoteData> {
	public void processSensorData(SensorParser data);
}
