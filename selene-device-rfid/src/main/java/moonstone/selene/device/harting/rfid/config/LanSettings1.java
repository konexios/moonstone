package moonstone.selene.device.harting.rfid.config;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.SerializationUtils;

public class LanSettings1 implements ConfigParameter<LanSettings1> {
	private static final long serialVersionUID = -9112485684448466415L;

	private static final int ID = 40;

	private static final String HOST_INTERFACE_INTERFACES_LAN_IPV4_IP_ADDRESS =
			"HostInterface_Interfaces_LAN_IPv4_IPAddress";
	private InetAddress address;
	private static final String HOST_INTERFACE_INTERFACES_LAN_PORT_NUMBER = "HostInterface_Interfaces_LAN_PortNumber";
	private int portNumber;

	private static final List<String> ALL = new ArrayList<>();

	static {
		ALL.add(HOST_INTERFACE_INTERFACES_LAN_IPV4_IP_ADDRESS);
		ALL.add(HOST_INTERFACE_INTERFACES_LAN_PORT_NUMBER);
	}

	public LanSettings1 withAddress(InetAddress address) {
		this.address = address;
		return this;
	}

	public LanSettings1 withPortNumber(int portNumber) {
		this.portNumber = portNumber;
		return this;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(14);
		buffer.put(address.getAddress());
		buffer.putShort(8, (short) portNumber);
		return buffer.array();
	}

	@Override
	public LanSettings1 parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		InetAddress address;
		try {
			byte[] bytes = new byte[4];
			buffer.get(bytes);
			address = Inet4Address.getByAddress(bytes);
		} catch (UnknownHostException e) {
			address = Inet4Address.getLoopbackAddress();
		}
		int portNumber = Short.toUnsignedInt(buffer.getShort(8));
		return new LanSettings1().withAddress(address).withPortNumber(portNumber);
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public boolean updateState(String name, String value) {
		LanSettings1 stored = SerializationUtils.clone(this);
		switch (name) {
			case HOST_INTERFACE_INTERFACES_LAN_IPV4_IP_ADDRESS: {
				try {
					address = InetAddress.getByName(value);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				break;
			}
			case HOST_INTERFACE_INTERFACES_LAN_PORT_NUMBER: {
				portNumber = Integer.parseInt(value);
				break;
			}
		}
		return !equals(stored);
	}

	@Override
	public Map<String, String> getStates() {
		Map<String, String> result = new HashMap<>();
		result.put(HOST_INTERFACE_INTERFACES_LAN_IPV4_IP_ADDRESS, address.getHostAddress());
		result.put(HOST_INTERFACE_INTERFACES_LAN_PORT_NUMBER, Integer.toString(portNumber));
		return result;
	}

	@Override
	public List<String> getParams() {
		return ALL;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LanSettings1 that = (LanSettings1) o;
		return portNumber == that.portNumber && Objects.equals(address, that.address);
	}

	@Override
	public int hashCode() {

		return Objects.hash(address, portNumber);
	}
}
