package com.arrow.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;

public class WriteMultipleBlocksResponse extends HostCommandResponse<WriteMultipleBlocksResponse> {
	private static final long serialVersionUID = -1854325968521330981L;

	private int status;
	private int epcClass1Gen2Error;
	private int dbAddressError;

	public int getStatus() {
		return status;
	}

	public int getEpcClass1Gen2Error() {
		return epcClass1Gen2Error;
	}

	public int getDbAddressError() {
		return dbAddressError;
	}

	public WriteMultipleBlocksResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public WriteMultipleBlocksResponse withEpcClass1Gen2Error(int epcClass1Gen2Error) {
		this.epcClass1Gen2Error = epcClass1Gen2Error;
		return this;
	}

	public WriteMultipleBlocksResponse withDbAddressError(int dbAddressError) {
		this.dbAddressError = dbAddressError;
		return this;
	}

	public WriteMultipleBlocksResponse parse(int mode, byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int status = Byte.toUnsignedInt(buffer.get(2));
		WriteMultipleBlocksResponse result = new WriteMultipleBlocksResponse();
		result.withStatus(status);
		int shift = 3;
		if (status == 95) {
			int epcClass1Gen2Error = Byte.toUnsignedInt(buffer.get(shift));
			result.withEpcClass1Gen2Error(epcClass1Gen2Error);
			shift++;
		}
		if (status == 3 || status == 5) {
			buffer.position(shift);
			int dbAddressError = buffer.remaining() == 2 ? Short.toUnsignedInt(buffer.getShort(shift)) :
					Byte.toUnsignedInt(buffer.get(shift));
			result.withDbAddressError(dbAddressError);
		}
		return result;
	}
}
