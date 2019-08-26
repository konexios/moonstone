package moonstone.selene.device.harting.rfid.command;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;

public class GetInputResponse implements Response<GetInputResponse> {
	private static final long serialVersionUID = 6702654878452750116L;
	private int status;
	private Set<Inputs> inputs;

	public GetInputResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public GetInputResponse withInputs(Set<Inputs> inputs) {
		this.inputs = inputs;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public Set<Inputs> getInputs() {
		return inputs;
	}

	public GetInputResponse parse(int mode, byte... payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		int status = Byte.toUnsignedInt(buffer.get(2));
		Set<Inputs> inputs = Inputs.extract(Byte.toUnsignedInt(buffer.get(3)));
		return new GetInputResponse().withStatus(status).withInputs(inputs);
	}

	enum Inputs {
		IN1,
		IN2,
		IN3,
		IN4,
		IN5;

		public static Set<Inputs> extract(int value) {
			Set<Inputs> result = EnumSet.noneOf(Inputs.class);
			for (Inputs item : values()) {
				if ((value >> item.ordinal() & 0x01) == 1) {
					result.add(item);
				}
			}
			return result;
		}

		public static int build(Iterable<Inputs> value) {
			int result = 0;
			for (Inputs item : value) {
				result |= 1 << item.ordinal();
			}
			return result;
		}
	}
}
