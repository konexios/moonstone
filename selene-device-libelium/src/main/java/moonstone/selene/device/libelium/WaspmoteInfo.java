package moonstone.selene.device.libelium;

import moonstone.selene.engine.DeviceInfo;

public class WaspmoteInfo extends DeviceInfo {
	private static final long serialVersionUID = -6586856724042535494L;

	public final static String DEFAULT_DEVICE_TYPE = "waspmote";

	public WaspmoteInfo() {
		setType(DEFAULT_DEVICE_TYPE);
	}
}
