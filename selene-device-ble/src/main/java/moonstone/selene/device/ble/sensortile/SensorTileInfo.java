package moonstone.selene.device.ble.sensortile;

import moonstone.selene.device.ble.BleInfo;

public class SensorTileInfo extends BleInfo {
    private static final long serialVersionUID = -2933696976685273159L;

    public static final String DEFAULT_DEVICE_TYPE = "st-sensortile";

    public SensorTileInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }
}
