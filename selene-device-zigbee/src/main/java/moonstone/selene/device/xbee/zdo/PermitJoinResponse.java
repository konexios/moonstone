package moonstone.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PermitJoinResponse {
	private int status;

	public PermitJoinResponse(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public static PermitJoinResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.get(); // sequence
		int status = Byte.toUnsignedInt(buffer.get());
		return new PermitJoinResponse(status);
	}
}
