package moonstone.selene.device.zigbee;

import moonstone.selene.device.zigbee.data.AddressesHolder;

public class ZigBeeCoordinatorInfo extends ZigBeeEndDeviceInfo {
	private static final long serialVersionUID = -7323409103471433958L;

	public static final String DEFAULT_DEVICE_TYPE = "zigbee-coordinator";

	private String port;
	private int baudRate;
	private AddressesHolder addresses = new AddressesHolder();

	public ZigBeeCoordinatorInfo() {
		setType(DEFAULT_DEVICE_TYPE);
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

	public AddressesHolder getAddresses() {
		return addresses;
	}

	public void setAddresses(AddressesHolder addresses) {
		this.addresses = addresses;
	}
}
