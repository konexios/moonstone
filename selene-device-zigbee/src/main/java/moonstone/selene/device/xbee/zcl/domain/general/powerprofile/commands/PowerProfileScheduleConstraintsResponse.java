package moonstone.selene.device.xbee.zcl.domain.general.powerprofile.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class PowerProfileScheduleConstraintsResponse
		extends ClusterSpecificCommand<PowerProfileScheduleConstraintsResponse> {
	private int powerProfileId;
	private int startAfter;
	private int startBefore;

	public int getPowerProfileId() {
		return powerProfileId;
	}

	public PowerProfileScheduleConstraintsResponse withPowerProfileId(int powerProfileId) {
		this.powerProfileId = powerProfileId;
		return this;
	}

	public int getStartAfter() {
		return startAfter;
	}

	public PowerProfileScheduleConstraintsResponse withStartAfter(int startAfter) {
		this.startAfter = startAfter;
		return this;
	}

	public int getStartBefore() {
		return startBefore;
	}

	public PowerProfileScheduleConstraintsResponse withStartBefore(int startBefore) {
		this.startBefore = startBefore;
		return this;
	}

	@Override
	protected int getId() {
		return PowerProfileClusterCommands.POWER_PROFILE_SCHEDULE_CONSTRAINTS_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) powerProfileId);
		buffer.putShort((short) startAfter);
		buffer.putShort((short) startBefore);
		return buffer.array();
	}

	@Override
	public PowerProfileScheduleConstraintsResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 5, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		powerProfileId = Byte.toUnsignedInt(buffer.get());
		startAfter = Short.toUnsignedInt(buffer.getShort());
		startBefore = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
