package moonstone.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class RequestFastPollMode extends ClusterSpecificCommand<RequestFastPollMode> {
	private int fastPollUpdatePeriod;
	private int duration;

	public int getFastPollUpdatePeriod() {
		return fastPollUpdatePeriod;
	}

	public RequestFastPollMode withFastPollUpdatePeriod(int fastPollUpdatePeriod) {
		this.fastPollUpdatePeriod = fastPollUpdatePeriod;
		return this;
	}

	public int getDuration() {
		return duration;
	}

	public RequestFastPollMode withDuration(int duration) {
		this.duration = duration;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.REQUEST_FAST_POLL_MODE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) fastPollUpdatePeriod);
		buffer.put((byte) duration);
		return buffer.array();
	}

	@Override
	public RequestFastPollMode fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		fastPollUpdatePeriod = Byte.toUnsignedInt(buffer.get());
		duration = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
