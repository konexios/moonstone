package moonstone.selene.device.wifi;

import moonstone.selene.engine.DeviceInfo;

public class WifiInfo extends DeviceInfo {
    private static final long serialVersionUID = -7081834746365317017L;

    public static final String DEFAULT_DEVICE_TYPE = "wifi";

    public WifiInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }
}
