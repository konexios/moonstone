package moonstone.selene.device.ble.puck;

import moonstone.selene.device.ble.beacon.BeaconModule;
import moonstone.selene.engine.DeviceModule;

public interface PuckModule extends DeviceModule<PuckInfo, PuckProperties, PuckStates, PuckData>, BeaconModule {
}
