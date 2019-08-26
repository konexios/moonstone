package moonstone.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;

public class GetSoftwareVersionResponse implements Response<GetSoftwareVersionResponse> {
	private static final long serialVersionUID = 2932519449740720906L;
	private int status;
	private byte[] softwareRevision = new byte[3];
	private int hardwareType;
	private int readerFirmware;
	private byte[] transponderTypes = new byte[2];
	private int maxRxBufferSize;
	private int maxTxBufferSize;

	public int getStatus() {
		return status;
	}

	public GetSoftwareVersionResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public byte[] getSoftwareRevision() {
		return softwareRevision;
	}

	public GetSoftwareVersionResponse withSoftwareRevision(byte[] softwareRevision) {
		this.softwareRevision = softwareRevision;
		return this;
	}

	public int getHardwareType() {
		return hardwareType;
	}

	public GetSoftwareVersionResponse withHardwareType(int hardwareType) {
		this.hardwareType = hardwareType;
		return this;
	}

	public int getReaderFirmware() {
		return readerFirmware;
	}

	public GetSoftwareVersionResponse withReaderFirmware(int readerFirmware) {
		this.readerFirmware = readerFirmware;
		return this;
	}

	public byte[] getTransponderTypes() {
		return transponderTypes;
	}

	public GetSoftwareVersionResponse withTransponderTypes(byte[] transponderTypes) {
		this.transponderTypes = transponderTypes;
		return this;
	}

	public int getMaxRxBufferSize() {
		return maxRxBufferSize;
	}

	public GetSoftwareVersionResponse withMaxRxBufferSize(int maxRxBufferSize) {
		this.maxRxBufferSize = maxRxBufferSize;
		return this;
	}

	public int getMaxTxBufferSize() {
		return maxTxBufferSize;
	}

	public GetSoftwareVersionResponse withMaxTxBufferSize(int maxTxBufferSize) {
		this.maxTxBufferSize = maxTxBufferSize;
		return this;
	}

	public GetSoftwareVersionResponse parse(int mode, byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int status = Byte.toUnsignedInt(buffer.get(2));
		byte[] softwareRevision = new byte[3];
		buffer.position(3);
		buffer.get(softwareRevision);
		int hardwareType = Byte.toUnsignedInt(buffer.get(6));
		int readerFirmware = Byte.toUnsignedInt(buffer.get(7));
		byte[] transponderTypes = new byte[2];
		buffer.position(8);
		buffer.get(transponderTypes);
		int maxRxBufferSize = Short.toUnsignedInt(buffer.getShort(10));
		int maxTxBufferSize = Short.toUnsignedInt(buffer.getShort(12));
		return new GetSoftwareVersionResponse().withStatus(status).withSoftwareRevision(softwareRevision)
				.withHardwareType(hardwareType).withReaderFirmware(readerFirmware).withTransponderTypes(
						transponderTypes).withMaxRxBufferSize(maxRxBufferSize).withMaxTxBufferSize(maxTxBufferSize);
	}
}
