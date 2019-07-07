package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetLogRecordResponse extends ClusterSpecificCommand<GetLogRecordResponse> {
	private int logEntryId;
	private long timestamp;
	private EventType eventType;
	private Source source;
	private int eventId;
	private int userId;
	private byte[] code;

	public int getLogEntryId() {
		return logEntryId;
	}

	public GetLogRecordResponse withLogEntryId(int logEntryId) {
		this.logEntryId = logEntryId;
		return this;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public GetLogRecordResponse withTimestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public EventType getEventType() {
		return eventType;
	}

	public GetLogRecordResponse withEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}

	public Source getSource() {
		return source;
	}

	public GetLogRecordResponse withSource(Source source) {
		this.source = source;
		return this;
	}

	public int getEventId() {
		return eventId;
	}

	public GetLogRecordResponse withEventId(int eventId) {
		this.eventId = eventId;
		return this;
	}

	public int getUserId() {
		return userId;
	}

	public GetLogRecordResponse withUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public byte[] getCode() {
		return code;
	}

	public GetLogRecordResponse withCode(byte[] code) {
		this.code = code;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.GET_LOG_RECORD_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(11 + code.length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) logEntryId);
		buffer.putInt((int) timestamp);
		buffer.put((byte) eventType.ordinal());
		buffer.put((byte) source.getValue());
		buffer.put((byte) eventId);
		buffer.putShort((short) userId);
		buffer.put(code);
		return buffer.array();
	}

	@Override
	protected GetLogRecordResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 11, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		logEntryId = Short.toUnsignedInt(buffer.getShort());
		timestamp = Integer.toUnsignedLong(buffer.getInt());
		eventType = EventType.values()[Byte.toUnsignedInt(buffer.get())];
		source = Source.getByValue(Byte.toUnsignedInt(buffer.get()));
		eventId = Byte.toUnsignedInt(buffer.get());
		userId = Short.toUnsignedInt(buffer.getShort());
		code = new byte[buffer.remaining()];
		buffer.get(code);
		return this;
	}

	public enum EventType {
		OPERATION,
		PROGRAMMING,
		ALARM
	}

	public enum Source {
		KEYPAD(0x00),
		RF(0x01),
		MANUAL(0x02),
		RFID(0x03),
		INDETERMINATE(0xff);

		private final int value;

		Source(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static Source getByValue(int value) {
			for (Source item : values()) {
				if (item.value == value) {
					return item;
				}
			}
			return null;
		}
	}
}
