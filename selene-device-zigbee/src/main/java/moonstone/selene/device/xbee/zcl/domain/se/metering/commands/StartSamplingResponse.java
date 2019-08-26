package moonstone.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class StartSamplingResponse extends ClusterSpecificCommand<StartSamplingResponse> {
	private int sampleId;

	public int getSampleId() {
		return sampleId;
	}

	public StartSamplingResponse withSampleId(int sampleId) {
		this.sampleId = sampleId;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.START_SAMPLING_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) sampleId);
		return buffer.array();
	}

	@Override
	public StartSamplingResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		sampleId = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
