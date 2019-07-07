package com.arrow.selene.device.ble.puck;

import com.arrow.selene.device.ble.beacon.BeaconModule;
import com.arrow.selene.engine.DeviceModule;

public interface PuckModule extends DeviceModule<PuckInfo, PuckProperties, PuckStates, PuckData>, BeaconModule {
}
