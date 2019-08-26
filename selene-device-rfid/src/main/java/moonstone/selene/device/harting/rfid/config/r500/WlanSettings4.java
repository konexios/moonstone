package moonstone.selene.device.harting.rfid.config.r500;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import moonstone.selene.device.harting.rfid.config.ConfigParameter;

public class WlanSettings4 implements ConfigParameter<WlanSettings4> {
	private static final long serialVersionUID = 3484308993677014L;

	private boolean dhcpEnabled;
	private boolean keepAliveEnabled;
	private int keepCount;
	private int keepIdle;
	private int keepInterval;
	private int adHocChannel;
	private NetworkType networkType;

	private static final List<String> ALL = new ArrayList<>();

	public WlanSettings4(boolean dhcpEnabled, boolean keepAliveEnabled, int keepCount, int keepIdle, int keepInterval,
	                     int adHocChannel, NetworkType networkType) {
		this.dhcpEnabled = dhcpEnabled;
		this.keepAliveEnabled = keepAliveEnabled;
		this.keepCount = keepCount;
		this.keepIdle = keepIdle;
		this.keepInterval = keepInterval;
		this.adHocChannel = adHocChannel;
		this.networkType = networkType;
	}

	@Override
	public byte[] build() {
		ByteBuffer buffer = ByteBuffer.allocate(30);
		buffer.put(0, (byte) (dhcpEnabled ? 1 : 0));
		buffer.put(4, (byte) (keepAliveEnabled ? 1 : 0));
		buffer.put(5, (byte) keepCount);
		buffer.putShort(6, (short) keepIdle);
		buffer.putShort(8, (short) keepInterval);
		buffer.put(10, (byte) adHocChannel);
		buffer.put(11, (byte) NetworkType.build(networkType));
		return buffer.array();
	}

	@Override
	public WlanSettings4 parse(byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		boolean dhcpEnabled = buffer.get(0) == 1;
		boolean keepAliveEnabled = buffer.get(4) == 1;
		int keepCount = Byte.toUnsignedInt(buffer.get(5));
		int keepIdle = Short.toUnsignedInt(buffer.getShort(6));
		int keepInterval = Short.toUnsignedInt(buffer.getShort(8));
		int adHocChannel = Byte.toUnsignedInt(buffer.get(10));
		NetworkType networkType = NetworkType.extract(buffer.get(11));
		return new WlanSettings4(dhcpEnabled, keepAliveEnabled, keepCount, keepIdle, keepInterval, adHocChannel,
				networkType);
	}

	@Override
	public int getId() {
		return 0;
	}

	enum NetworkType {
		ACCESS_POINT,
		AD_HOC;

		public static int build(NetworkType value) {
			return value.ordinal();
		}

		public static NetworkType extract(int value) {
			for (NetworkType item : values()) {
				if (item.ordinal() == value) {
					return item;
				}
			}
			return null;
		}
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
		WlanSettings4 that = (WlanSettings4) o;
		return dhcpEnabled == that.dhcpEnabled && keepAliveEnabled == that.keepAliveEnabled &&
				keepCount == that.keepCount && keepIdle == that.keepIdle && keepInterval == that.keepInterval &&
				adHocChannel == that.adHocChannel && networkType == that.networkType;
	}

	@Override
	public int hashCode() {

		return Objects.hash(dhcpEnabled, keepAliveEnabled, keepCount, keepIdle, keepInterval, adHocChannel,
				networkType);
	}
}
