package moonstone.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

public class NodeDescriptorRequest {
	public static byte[] toPayload(byte sequence, XBee16BitAddress address) {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(sequence);
		buffer.put(ByteUtils.swapByteArray(address.getValue()));
		return buffer.array();
	}
}
