package moonstone.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;

public class ReadMultipleBlocksResponse extends HostCommandResponse<ReadMultipleBlocksResponse> {
	private static final long serialVersionUID = -939599337026750274L;

	private int status;
	private int dbNumber;
	private int dbSize;
	private int securityStatus;
	private byte[] data;

	public int getStatus() {
		return status;
	}

	public int getDbNumber() {
		return dbNumber;
	}

	public int getDbSize() {
		return dbSize;
	}

	public int getSecurityStatus() {
		return securityStatus;
	}

	public byte[] getData() {
		return data;
	}

	public ReadMultipleBlocksResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public ReadMultipleBlocksResponse withDbNumber(int dbNumber) {
		this.dbNumber = dbNumber;
		return this;
	}

	public ReadMultipleBlocksResponse withDbSize(int dbSize) {
		this.dbSize = dbSize;
		return this;
	}

	public ReadMultipleBlocksResponse withSecurityStatus(int securityStatus) {
		this.securityStatus = securityStatus;
		return this;
	}

	public ReadMultipleBlocksResponse withData(byte[] data) {
		this.data = data;
		return this;
	}

	public ReadMultipleBlocksResponse parse(int mode, byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int status = Byte.toUnsignedInt(buffer.get(2));
		ReadMultipleBlocksResponse result = new ReadMultipleBlocksResponse();
		result.withStatus(status);
		if (status == 0) {
			result.withDbNumber(Byte.toUnsignedInt(buffer.get(3)));
			result.withDbSize(Byte.toUnsignedInt(buffer.get(4)));
			result.withSecurityStatus(Byte.toUnsignedInt(buffer.get(5)));
			buffer.position(6);
			byte[] data = new byte[buffer.remaining() - 2];
			buffer.get(data);
			result.withData(data);
		}
		return result;
	}
}
