package moonstone.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class RequestFastPollModeResponse extends ClusterSpecificCommand<RequestFastPollModeResponse> {
	private int appliedUpdatePeriod;
	private long fastPollModeEndTime;

	public int getAppliedUpdatePeriod() {
		return appliedUpdatePeriod;
	}

	public RequestFastPollModeResponse withAppliedUpdatePeriod(int appliedUpdatePeriod) {
		this.appliedUpdatePeriod = appliedUpdatePeriod;
		return this;
	}

	public long getFastPollModeEndTime() {
		return fastPollModeEndTime;
	}

	public RequestFastPollModeResponse withFastPollModeEndTime(long fastPollModeEndTime) {
		this.fastPollModeEndTime = fastPollModeEndTime;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.REQUEST_FAST_POLL_MODE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) appliedUpdatePeriod);
		buffer.putInt((int) fastPollModeEndTime);
		return buffer.array();
	}

	@Override
	public RequestFastPollModeResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 5, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		appliedUpdatePeriod = Byte.toUnsignedInt(buffer.get());
		fastPollModeEndTime = Integer.toUnsignedLong(buffer.getInt());
		return this;
	}
}
