package moonstone.selene.device.xbee.zcl.domain.general.pollcontrol.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class CheckInResponse extends ClusterSpecificCommand<CheckInResponse> {
	private boolean startFastPolling;
	private int fastPollTimeout;

	public CheckInResponse(boolean startFastPolling, int fastPollTimeout) {
		this.startFastPolling = startFastPolling;
		this.fastPollTimeout = fastPollTimeout;
	}

	public boolean isStartFastPolling() {
		return startFastPolling;
	}

	public int getFastPollTimeout() {
		return fastPollTimeout;
	}

	@Override
	protected int getId() {
		return PollControlClusterCommands.CHECK_IN_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) (startFastPolling ? 0x01 : 0x00));
		buffer.putShort((short) fastPollTimeout);
		return buffer.array();
	}

	@Override
	public CheckInResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		startFastPolling = (buffer.get() & 0x01) == 1;
		fastPollTimeout = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
