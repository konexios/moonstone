package moonstone.selene.device.libelium;

import moonstone.selene.device.libelium.data.Sensor;
import moonstone.selene.engine.DeviceModule;

public interface MeshliumModule extends DeviceModule<MeshliumInfo, MeshliumProperties, MeshliumStates, MeshliumData> {
	DbConnectionInfo getDbConnectionInfo();

	Sensor getSensorInfo(String idAscii);
}
