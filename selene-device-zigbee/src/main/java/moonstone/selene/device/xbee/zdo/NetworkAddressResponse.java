package moonstone.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

public class NetworkAddressResponse {
	private XBee64BitAddress address64;
	private XBee16BitAddress address16;

	public NetworkAddressResponse(XBee64BitAddress address64, XBee16BitAddress address16) {
		this.address64 = address64;
		this.address16 = address16;
	}

	public XBee64BitAddress getAddress64() {
		return address64;
	}

	public XBee16BitAddress getAddress16() {
		return address16;
	}

	public static NetworkAddressResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.get(); // sequence
		buffer.get(); // status
		byte[] address64 = new byte[8];
		buffer.get(address64);
		byte[] address16 = new byte[2];
		buffer.get(address16);
		return new NetworkAddressResponse(new XBee64BitAddress(ByteUtils.swapByteArray(address64)),
				new XBee16BitAddress(ByteUtils.swapByteArray(address16)));
	}
}
