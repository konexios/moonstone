package com.arrow.selene.device.libelium;

import com.arrow.selene.device.libelium.data.Sensor;
import com.arrow.selene.engine.DeviceModule;

public interface MeshliumModule extends DeviceModule<MeshliumInfo, MeshliumProperties, MeshliumStates, MeshliumData> {
	DbConnectionInfo getDbConnectionInfo();

	Sensor getSensorInfo(String idAscii);
}
