package moonstone.selene.device.harting.rfid.config.r500;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import moonstone.selene.device.harting.rfid.config.ConfigParameter;

public class NotificationChannel2 implements ConfigParameter<NotificationChannel2> {
	private static final long serialVersionUID = -5575166388680074003L;

	private InetAddress address;
	private int portNumber;

	private static final List<String> ALL = new ArrayList<>();

	public NotificationChannel2(InetAddress address, int portNumber) {
		this.address = address;
		this.portNumber = portNumber;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(30);
		buffer.put(address.getAddress());
		buffer.putShort(20, (short) portNumber);
		return buffer.array();
	}

	@Override
	public NotificationChannel2 parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		InetAddress address;
		try {
			byte[] bytes = new byte[4];
			buffer.get(bytes);
			address = Inet4Address.getByAddress(bytes);
		} catch (UnknownHostException e) {
			address = Inet4Address.getLoopbackAddress();
		}
		int portNumber = Short.toUnsignedInt(buffer.getShort(20));
		return new NotificationChannel2(address, portNumber);
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public boolean updateState(String name, String value) {
		return false;
	}

	@Override
	public Map<String, String> getStates() {
		return Collections.emptyMap();
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
		NotificationChannel2 that = (NotificationChannel2) o;
		return portNumber == that.portNumber && Objects.equals(address, that.address);
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, portNumber);
	}
}
