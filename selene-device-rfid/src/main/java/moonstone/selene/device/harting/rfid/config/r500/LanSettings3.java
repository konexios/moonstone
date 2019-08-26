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

public class LanSettings3 implements ConfigParameter<LanSettings3> {
	private static final long serialVersionUID = -3255305892426157816L;

	private InetAddress address;

	private static final List<String> ALL = new ArrayList<>();

	public LanSettings3(InetAddress address) {
		this.address = address;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(30);
		buffer.put(address.getAddress());
		return buffer.array();
	}

	@Override
	public LanSettings3 parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		InetAddress address;
		try {
			byte[] bytes = new byte[4];
			buffer.get(bytes);
			address = Inet4Address.getByAddress(bytes);
		} catch (UnknownHostException e) {
			address = Inet4Address.getLoopbackAddress();
		}
		return new LanSettings3(address);
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
		LanSettings3 that = (LanSettings3) o;
		return Objects.equals(address, that.address);
	}

	@Override
	public int hashCode() {

		return Objects.hash(address);
	}
}
