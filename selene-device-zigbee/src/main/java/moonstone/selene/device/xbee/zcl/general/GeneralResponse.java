package moonstone.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;

import moonstone.selene.device.xbee.zcl.ZclFrame;

@SuppressWarnings("rawtypes")
public abstract class GeneralResponse<T extends GeneralResponse> extends ZclFrame {
	public final T extract(byte[] payload) {
		header.extractHeader(payload);
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.position(header.isManufacturerSpecific() ? 5 : 3);
		byte[] data = new byte[buffer.remaining()];
		buffer.get(data);
		return fromPayload(data);
	}

	protected abstract T fromPayload(byte[] payload);
}
