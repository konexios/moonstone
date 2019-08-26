package moonstone.selene.device.ble;

import java.util.Map;

import moonstone.selene.device.ble.sensor.BleSensor;
import moonstone.selene.engine.DeviceModule;

public interface BleModule<Info extends BleInfo, Prop extends BleProperties, State extends BleStates, Data extends
		BleData> extends DeviceModule<Info, Prop, State, Data> {

	Map<String, BleSensor<?, ?>> getSensors();

	boolean isRandomAddress();

	void addHandleToChar(String handle, String characteristic);

	String getCharacteristic(String handle);
}
