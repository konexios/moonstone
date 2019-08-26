package moonstone.selene.engine;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import moonstone.acn.client.model.DeviceRegistrationModel;
import moonstone.selene.data.Device;
import moonstone.selene.engine.state.DeviceStates;
import moonstone.selene.engine.state.State;
import moonstone.selene.model.StatusModel;

public interface DeviceModule<Info extends DeviceInfo, Prop extends DeviceProperties, States extends DeviceStates,
		Data extends DeviceData>
		extends Module {
	void init(Properties props);

	Info getInfo();

	Prop getProperties();

	States getStates();

	Device getDevice();

	Map<String, String> exportProperties();

	void importProperties(Map<String, String> properties);

	void notifyPropertiesChanged(Map<String, String> properties);

	void notifyTelemetryChanged(Map<String, String> properties);

	boolean notifyStatesChanged(Map<String, State> states);

	StatusModel performCommand(byte... bytes);

	DeviceRegistrationModel populate(DeviceRegistrationModel model);

	void persistUpdatedDeviceInfo();

	DeviceModule<Info, Prop, States, Data> populateFromModel(DeviceRegistrationModel model);

	void upgradeDeviceSoftware(File file);
}
