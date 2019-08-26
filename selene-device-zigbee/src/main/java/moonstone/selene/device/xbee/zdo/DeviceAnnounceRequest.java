package moonstone.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

public class DeviceAnnounceRequest {
	private int sequence;
	private XBee16BitAddress address16;
	private XBee64BitAddress address64;
	private int caps;

	public DeviceAnnounceRequest(int sequence, XBee16BitAddress address16, XBee64BitAddress address64, int caps) {
		this.address16 = address16;
		this.address64 = address64;
		this.caps = caps;
		this.sequence = sequence;
	}

	public int getSequence() {
		return sequence;
	}

	public XBee16BitAddress getAddress16() {
		return address16;
	}

	public XBee64BitAddress getAddress64() {
		return address64;
	}

	public int getCaps() {
		return caps;
	}

	public static DeviceAnnounceRequest fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		int sequence = Byte.toUnsignedInt(buffer.get());
		byte[] address16 = new byte[2];
		buffer.get(address16);
		byte[] address64 = new byte[8];
		buffer.get(address64);
		int caps = Byte.toUnsignedInt(buffer.get());
		return new DeviceAnnounceRequest(sequence, new XBee16BitAddress(ByteUtils.swapByteArray(address16)),
				new XBee64BitAddress(ByteUtils.swapByteArray(address64)), caps);
	}
}
