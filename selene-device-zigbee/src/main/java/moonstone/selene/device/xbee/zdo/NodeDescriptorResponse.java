package moonstone.selene.device.xbee.zdo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

import moonstone.selene.device.xbee.zcl.ZclStatus;

public class NodeDescriptorResponse {
	private int status;
	private XBee16BitAddress address;
	private boolean router;
	private int manufacturerCode;

	public NodeDescriptorResponse(int status, XBee16BitAddress address, boolean router, int manufacturerCode) {
		this.status = status;
		this.address = address;
		this.router = router;
		this.manufacturerCode = manufacturerCode;
	}

	public int getStatus() {
		return status;
	}

	public XBee16BitAddress getAddress() {
		return address;
	}

	public boolean isRouter() {
		return router;
	}

	public int getManufacturerCode() {
		return manufacturerCode;
	}

	public static NodeDescriptorResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.get(); // sequence
		int status = Byte.toUnsignedInt(buffer.get());
		XBee16BitAddress address = null;
		boolean router = false;
		int manufacturerCode = 0;
		if (status == ZclStatus.SUCCESS) {
			byte[] array = new byte[2];
			buffer.get(array);
			address = new XBee16BitAddress(ByteUtils.swapByteArray(array));
			router = (buffer.get() & 0b000000_111) == 1;
			buffer.get(); // frequency
			buffer.get(); // capability
			manufacturerCode = Short.toUnsignedInt(buffer.getShort());
		}
		return new NodeDescriptorResponse(status, address, router, manufacturerCode);
	}
}
