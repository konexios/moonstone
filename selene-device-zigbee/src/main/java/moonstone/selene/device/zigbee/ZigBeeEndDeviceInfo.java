package moonstone.selene.device.zigbee;

import moonstone.selene.device.zigbee.data.BindingsHolder;

public class ZigBeeEndDeviceInfo extends ZigBeeInfoAbstract {
	private static final long serialVersionUID = -572602171253742921L;

	public static final String DEFAULT_DEVICE_TYPE = "zigbee-device";

	private BindingsHolder bindings = new BindingsHolder();

	public ZigBeeEndDeviceInfo() {
		setType(DEFAULT_DEVICE_TYPE);
	}

	public BindingsHolder getBindings() {
		return bindings;
	}

	public void setBindings(BindingsHolder bindings) {
		this.bindings = bindings;
	}
}
