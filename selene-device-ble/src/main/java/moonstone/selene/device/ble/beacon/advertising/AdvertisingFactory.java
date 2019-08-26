package moonstone.selene.device.ble.beacon.advertising;

import moonstone.selene.device.ble.beacon.BeaconControllerModule;

public class AdvertisingFactory {
    public static AdvertisingReader initReader(BeaconControllerModule controller, boolean useDbus) {
        if (useDbus) {
            return initDbusReader(controller);
        } else {
            return initHcidumpReader(controller);
        }
    }

    public static AdvertisingReader initHcidumpReader(BeaconControllerModule controller) {
        return new HcidumpAdvertisingReader(controller);
    }

    public static AdvertisingReader initDbusReader(BeaconControllerModule controller) {
        return new DbusAdvertisingReader(controller);
    }

}
