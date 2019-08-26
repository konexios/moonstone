package moonstone.selene.device.ble.thunderboard.sense;

import moonstone.selene.device.ble.BleInfo;

public class ThunderboardSenseInfo extends BleInfo {
	public static final String DEFAULT_DEVICE_TYPE = "silabs-thunderboard-sense";

	private static final long serialVersionUID = 2455120877417896396L;

	public ThunderboardSenseInfo() {
		setType(DEFAULT_DEVICE_TYPE);
	}
}
