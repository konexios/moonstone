package moonstone.selene.device.cli;

import moonstone.selene.engine.DeviceInfo;

public class CliInfo extends DeviceInfo {
    private static final long serialVersionUID = -6269568398529840867L;

    public static final String DEFAULT_DEVICE_TYPE = "cli";

    public CliInfo() {
        setType(DEFAULT_DEVICE_TYPE);
    }
}
