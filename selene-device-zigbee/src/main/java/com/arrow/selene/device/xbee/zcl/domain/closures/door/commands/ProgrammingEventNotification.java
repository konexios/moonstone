package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.closures.door.commands.GetLogRecordResponse.Source;
import com.arrow.selene.device.xbee.zcl.domain.closures.door.data.UserStatus;
import com.arrow.selene.device.xbee.zcl.domain.closures.door.data.UserType;

public class ProgrammingEventNotification extends ClusterSpecificCommand<ProgrammingEventNotification> {
	private Source source;
	private ProgrammingEventCode eventCode;
	private int userId;
	private int pin;
	private UserType userType;
	private UserStatus userStatus;
	private long timestamp;
	private byte[] data;

	public Source getSource() {
		return source;
	}

	public ProgrammingEventNotification withSource(Source source) {
		this.source = source;
		return this;
	}

	public ProgrammingEventCode getEventCode() {
		return eventCode;
	}

	public ProgrammingEventNotification withEventCode(ProgrammingEventCode eventCode) {
		this.eventCode = eventCode;
		return this;
	}

	public int getUserId() {
		return userId;
	}

	public ProgrammingEventNotification withUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public int getPin() {
		return pin;
	}

	public ProgrammingEventNotification withPin(int pin) {
		this.pin = pin;
		return this;
	}

	public UserType getUserType() {
		return userType;
	}

	public ProgrammingEventNotification withUserType(UserType userType) {
		this.userType = userType;
		return this;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public ProgrammingEventNotification withUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
		return this;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public ProgrammingEventNotification withTimestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public byte[] getData() {
		return data;
	}

	public ProgrammingEventNotification withData(byte[] data) {
		this.data = data;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.PROGRAMMING_EVENT_NOTIFICATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(11 + data.length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) source.getValue());
		buffer.put((byte) eventCode.ordinal());
		buffer.putShort((short) userId);
		buffer.put((byte) pin);
		buffer.put((byte) userType.ordinal());
		buffer.put((byte) userStatus.ordinal());
		buffer.putInt((int) timestamp);
		buffer.put(data);
		return buffer.array();
	}

	@Override
	protected ProgrammingEventNotification fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 11, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		source = Source.getByValue(Byte.toUnsignedInt(buffer.get()));
		eventCode = ProgrammingEventCode.values()[Byte.toUnsignedInt(buffer.get())];
		userId = Short.toUnsignedInt(buffer.getShort());
		userType = UserType.values()[Byte.toUnsignedInt(buffer.get())];
		userStatus = UserStatus.values()[Byte.toUnsignedInt(buffer.get())];
		timestamp = Integer.toUnsignedLong(buffer.getInt());
		data = new byte[buffer.remaining()];
		buffer.get(data);
		return this;
	}

	public enum ProgrammingEventCode {
		UNKNOWN_OR_MFG_SPECIFIC,
		MASTER_CODE_CHANGED,
		PIN_CODE_ADDED,
		PIN_CODE_DELETED,
		PIN_CODE_CHANGED,
		RFID_CODE_ADDED,
		RFID_CODE_DELETED
	}
}
