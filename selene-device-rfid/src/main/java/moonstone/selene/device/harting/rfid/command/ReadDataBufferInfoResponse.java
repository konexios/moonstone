package moonstone.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;

public class ReadDataBufferInfoResponse implements Response<ReadDataBufferInfoResponse> {
	private static final long serialVersionUID = -7311319917963721547L;
	private int status;
	private int tabSize;
	private int tabStart;
	private int tabLength;

	public ReadDataBufferInfoResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public int getTabSize() {
		return tabSize;
	}

	public ReadDataBufferInfoResponse withTabSize(int tabSize) {
		this.tabSize = tabSize;
		return this;
	}

	public int getTabStart() {
		return tabStart;
	}

	public ReadDataBufferInfoResponse withTabStart(int tabStart) {
		this.tabStart = tabStart;
		return this;
	}

	public int getTabLength() {
		return tabLength;
	}

	public ReadDataBufferInfoResponse withTabLength(int tabLength) {
		this.tabLength = tabLength;
		return this;
	}

	public ReadDataBufferInfoResponse parse(int mode, byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int status = Byte.toUnsignedInt(buffer.get(2));
		ReadDataBufferInfoResponse result = new ReadDataBufferInfoResponse();
		result.withStatus(status);
		if (status == 0) {
			result.withTabSize(Short.toUnsignedInt(buffer.getShort(3)));
			result.withTabStart(Short.toUnsignedInt(buffer.getShort(5)));
			result.withTabLength(Short.toUnsignedInt(buffer.getShort(7)));
		}
		return result;
	}
}
