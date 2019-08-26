package moonstone.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;

public class ReadConfigurationResponse implements Response<ReadConfigurationResponse> {
	private static final long serialVersionUID = -3059859136927658383L;
	private int status;
	private byte[] parameter;

	public ReadConfigurationResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public ReadConfigurationResponse withParameter(byte[] parameter) {
		this.parameter = parameter;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public byte[] getParameter() {
		return parameter;
	}

	public ReadConfigurationResponse parse(int mode, byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload, 2, payload.length - 2);
		int status = Byte.toUnsignedInt(buffer.get());
		byte[] parameter = new byte[14];
		buffer.get(parameter);
		return new ReadConfigurationResponse().withStatus(status).withParameter(parameter);
	}
}
