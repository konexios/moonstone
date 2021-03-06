package moonstone.selene.device.uvc;

import moonstone.selene.engine.DeviceInfo;

public class UvcInfo extends DeviceInfo {
    private static final long serialVersionUID = 2970473160251390664L;

    public static final String DEFAULT_DEVICE_TYPE = "webcam";

    public UvcInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }
}
