package moonstone.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

public class BindUnbindRequest {
	public static byte[] toPayload(byte sequence, XBee64BitAddress source, int srcEndpoint, int clusterId,
	                               XBee64BitAddress destination, int dstEndpoint) {
		ByteBuffer buffer = ByteBuffer.allocate(22);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(sequence);
		buffer.put(ByteUtils.swapByteArray(source.getValue()));
		buffer.put((byte) srcEndpoint);
		buffer.putShort((short) clusterId);
		buffer.put((byte) 3);
		buffer.put(ByteUtils.swapByteArray(destination.getValue()));
		buffer.put((byte) dstEndpoint);
		return buffer.array();
	}
}
