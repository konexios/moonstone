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

public class NetworkTimeProtocol implements ConfigParameter<NetworkTimeProtocol> {
	private static final long serialVersionUID = -2676022330813988496L;

	private boolean ntpEnabled;
	private long triggerTime;
	private InetAddress serverAddress;
	private int destinationPort;

	private static final List<String> ALL = new ArrayList<>();

	public NetworkTimeProtocol(boolean ntpEnabled, long triggerTime, InetAddress serverAddress, int destinationPort) {
		this.ntpEnabled = ntpEnabled;
		this.triggerTime = triggerTime;
		this.serverAddress = serverAddress;
		this.destinationPort = destinationPort;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(30);
		buffer.put(0, (byte) (ntpEnabled ? 1 : 0));
		buffer.putInt(1, (int) triggerTime);
		buffer.put(serverAddress.getAddress());
		buffer.putShort(20, (short) destinationPort);
		return buffer.array();
	}

	@SuppressWarnings("unused")
	@Override
	public NetworkTimeProtocol parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		boolean ntpEnabled = buffer.get() == 1;
		long triggerTime = Integer.toUnsignedLong(buffer.getInt());
		InetAddress address;
		try {
			byte[] bytes = new byte[4];
			buffer.get(bytes);
			address = Inet4Address.getByAddress(bytes);
		} catch (UnknownHostException e) {
			address = Inet4Address.getLoopbackAddress();
		}
		int destinationPort = Short.toUnsignedInt(buffer.getShort(20));
		return new NetworkTimeProtocol(ntpEnabled, triggerTime, serverAddress, destinationPort);
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
		NetworkTimeProtocol that = (NetworkTimeProtocol) o;
		return ntpEnabled == that.ntpEnabled && triggerTime == that.triggerTime &&
				destinationPort == that.destinationPort && Objects.equals(serverAddress, that.serverAddress);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ntpEnabled, triggerTime, serverAddress, destinationPort);
	}
}
