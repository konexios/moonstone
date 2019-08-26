package moonstone.selene.device.xbee.zcl.domain.closures.door.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.ZclStatus;
import moonstone.selene.device.xbee.zcl.domain.closures.door.attributes.OperatingMode;

public class GetHolidayScheduleResponse extends ClusterSpecificCommand<GetHolidayScheduleResponse> {
	private int holidayScheduleId;
	private int status;
	private long startTime;
	private long endTime;
	private OperatingMode operatingMode;

	public int getHolidayScheduleId() {
		return holidayScheduleId;
	}

	public GetHolidayScheduleResponse withHolidayScheduleId(int holidayScheduleId) {
		this.holidayScheduleId = holidayScheduleId;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public GetHolidayScheduleResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public GetHolidayScheduleResponse withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public long getEndTime() {
		return endTime;
	}

	public GetHolidayScheduleResponse withEndTime(long endTime) {
		this.endTime = endTime;
		return this;
	}

	public OperatingMode getOperatingMode() {
		return operatingMode;
	}

	public GetHolidayScheduleResponse withOperatingMode(OperatingMode operatingMode) {
		this.operatingMode = operatingMode;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.GET_HOLIDAY_SCHEDULE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(status == ZclStatus.SUCCESS ? 11 : 2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) holidayScheduleId);
		buffer.put((byte) status);
		buffer.putInt((int) startTime);
		buffer.putInt((int) endTime);
		buffer.put((byte) operatingMode.ordinal());
		return buffer.array();
	}

	@Override
	protected GetHolidayScheduleResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2 || payload.length == 11, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		holidayScheduleId = Byte.toUnsignedInt(buffer.get());
		status = Byte.toUnsignedInt(buffer.get());
		if(status == ZclStatus.SUCCESS) {
			startTime = Integer.toUnsignedLong(buffer.getInt());
			endTime = Integer.toUnsignedLong(buffer.getInt());
			operatingMode = OperatingMode.getByValue(buffer.get());
		} else {
			startTime = 0;
			endTime = 0;
			operatingMode = null;
		}
		return this;
	}
}
