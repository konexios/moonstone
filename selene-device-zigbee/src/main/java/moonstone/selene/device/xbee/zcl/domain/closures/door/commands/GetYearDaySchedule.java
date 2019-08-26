package moonstone.selene.device.xbee.zcl.domain.closures.door.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetYearDaySchedule extends ClusterSpecificCommand<GetYearDaySchedule> {
	private int scheduleId;
	private int userId;

	public int getScheduleId() {
		return scheduleId;
	}

	public GetYearDaySchedule withScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
		return this;
	}

	public int getUserId() {
		return userId;
	}

	public GetYearDaySchedule withUserId(int userId) {
		this.userId = userId;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.GET_YEARDAY_SCHEDULE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) scheduleId);
		buffer.putShort((short) userId);
		return buffer.array();
	}

	@Override
	protected GetYearDaySchedule fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		scheduleId = Byte.toUnsignedInt(buffer.get());
		userId = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
