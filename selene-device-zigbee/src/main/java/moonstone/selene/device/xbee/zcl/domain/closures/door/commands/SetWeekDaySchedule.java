package moonstone.selene.device.xbee.zcl.domain.closures.door.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.closures.door.data.DayMask;

public class SetWeekDaySchedule extends ClusterSpecificCommand<SetWeekDaySchedule> {
	private int scheduleId;
	private int userId;
	private Set<DayMask> dayMasks;
	private int startHour;
	private int startMinute;
	private int endHour;
	private int endMinute;

	public int getScheduleId() {
		return scheduleId;
	}

	public SetWeekDaySchedule withScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
		return this;
	}

	public int getUserId() {
		return userId;
	}

	public SetWeekDaySchedule withUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public Set<DayMask> getDayMasks() {
		return dayMasks;
	}

	public SetWeekDaySchedule withDayMasks(Set<DayMask> dayMasks) {
		this.dayMasks = dayMasks;
		return this;
	}

	public int getStartHour() {
		return startHour;
	}

	public SetWeekDaySchedule withStartHour(int startHour) {
		this.startHour = startHour;
		return this;
	}

	public int getStartMinute() {
		return startMinute;
	}

	public SetWeekDaySchedule withStartMinute(int startMinute) {
		this.startMinute = startMinute;
		return this;
	}

	public int getEndHour() {
		return endHour;
	}

	public SetWeekDaySchedule withEndHour(int endHour) {
		this.endHour = endHour;
		return this;
	}

	public int getEndMinute() {
		return endMinute;
	}

	public SetWeekDaySchedule withEndMinute(int endMinute) {
		this.endMinute = endMinute;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.SET_WEEKDAY_SCHEDULE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) scheduleId);
		buffer.putShort((short) userId);
		int dayMask = 0;
		for (DayMask item : dayMasks) {
			dayMask |= 1 << item.ordinal();
		}
		buffer.put((byte) dayMask);
		buffer.put((byte) startHour);
		buffer.put((byte) startMinute);
		buffer.put((byte) endHour);
		buffer.put((byte) endMinute);
		return buffer.array();
	}

	@Override
	protected SetWeekDaySchedule fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 8, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		scheduleId = Byte.toUnsignedInt(buffer.get());
		userId = Short.toUnsignedInt(buffer.getShort());
		byte dayMask = buffer.get();
		dayMasks = EnumSet.noneOf(DayMask.class);
		for (DayMask item : DayMask.values()) {
			if ((dayMask >> item.ordinal() & 0x01) == 1) {
				dayMasks.add(item);
			}
		}
		startHour = Byte.toUnsignedInt(buffer.get());
		startMinute = Byte.toUnsignedInt(buffer.get());
		endHour = Byte.toUnsignedInt(buffer.get());
		endMinute = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
