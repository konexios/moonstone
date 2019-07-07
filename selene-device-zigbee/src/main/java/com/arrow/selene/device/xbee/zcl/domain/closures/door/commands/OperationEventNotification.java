package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.closures.door.commands.GetLogRecordResponse.Source;

public class OperationEventNotification extends ClusterSpecificCommand<OperationEventNotification> {
	private Source source;
	private OperationEventCode eventCode;
	private int userId;
	private int pin;
	private long timestamp;
	private byte[] data;

	public Source getSource() {
		return source;
	}

	public OperationEventNotification withSource(Source source) {
		this.source = source;
		return this;
	}

	public OperationEventCode getEventCode() {
		return eventCode;
	}

	public OperationEventNotification withEventCode(OperationEventCode eventCode) {
		this.eventCode = eventCode;
		return this;
	}

	public int getUserId() {
		return userId;
	}

	public OperationEventNotification withUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public int getPin() {
		return pin;
	}

	public OperationEventNotification withPin(int pin) {
		this.pin = pin;
		return this;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public OperationEventNotification withTimestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public byte[] getData() {
		return data;
	}

	public OperationEventNotification withData(byte[] data) {
		this.data = data;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.OPERATION_EVENT_NOTIFICATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(9 + data.length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) source.getValue());
		buffer.put((byte) eventCode.ordinal());
		buffer.putShort((short) userId);
		buffer.put((byte) pin);
		buffer.putInt((int) timestamp);
		buffer.put(data);
		return buffer.array();
	}

	@Override
	protected OperationEventNotification fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 9, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		source = Source.getByValue(Byte.toUnsignedInt(buffer.get()));
		eventCode = OperationEventCode.values()[Byte.toUnsignedInt(buffer.get())];
		userId = Short.toUnsignedInt(buffer.getShort());
		timestamp = Integer.toUnsignedLong(buffer.getInt());
		data = new byte[buffer.remaining()];
		buffer.get(data);
		return this;
	}

	public enum OperationEventCode {
		UNKNOWN_OR_MFG_SPECIFIC,
		LOCK,
		UNLOCK,
		LOCK_FAILURE_INVALID_PIN_OR_ID,
		LOCK_FAILURE_INVALID_SCHEDULE,
		UNLOCK_FAILURE_INVALID_PIN_OR_ID,
		UNLOCK_FAILURE_INVALID_SCHEDULE,
		ONE_TOUCH_LOCK,
		KEY_LOCK,
		KEY_UNLOCK,
		AUTO_LOCK,
		SCHEDULE_LOCK,
		SCHEDULE_UNLOCK,
		MANUAL_LOCK_KEY_OR_THUMBTURN,
		MANUAL_UNLOCK_KEY_OR_THUMBTURN,
		NON_ACCESS_USER_OPERATIONAL_EVENT
	}
}
