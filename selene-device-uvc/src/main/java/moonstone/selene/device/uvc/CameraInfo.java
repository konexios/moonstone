package moonstone.selene.device.uvc;

import moonstone.selene.engine.DeviceInfo;

public class CameraInfo extends DeviceInfo {
    private static final long serialVersionUID = -8780123863961891538L;

    public static final String DEFAULT_DEVICE_TYPE = "camera";

    public CameraInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }
}
