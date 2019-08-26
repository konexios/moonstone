package moonstone.selene.device.ble.simba;

import moonstone.selene.device.ble.BleInfo;

public class SimbaInfo extends BleInfo {
    private static final long serialVersionUID = -8914200992099329401L;

	public static final String DEFAULT_DEVICE_TYPE = "simba-pro";

    public SimbaInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }
}
