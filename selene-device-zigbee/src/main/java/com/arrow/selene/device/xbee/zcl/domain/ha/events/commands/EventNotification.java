package com.arrow.selene.device.xbee.zcl.domain.ha.events.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.ha.events.data.EventId;

public class EventNotification extends ClusterSpecificCommand<EventNotification> {
	private int eventHeader;
	private EventId eventId;

	public int getEventHeader() {
		return eventHeader;
	}

	public EventNotification withEventHeader(int eventHeader) {
		this.eventHeader = eventHeader;
		return this;
	}

	public EventId getEventId() {
		return eventId;
	}

	public EventNotification withEventId(EventId eventId) {
		this.eventId = eventId;
		return this;
	}

	@Override
	protected int getId() {
		return ApplianceEventsAndAlertClusterCommands.EVENTS_NOTIFICATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) eventHeader);
		buffer.put((byte) eventId.getValue());
		return buffer.array();
	}

	@Override
	public EventNotification fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		eventHeader = Byte.toUnsignedInt(buffer.get());
		eventId = EventId.getByValue(Byte.toUnsignedInt(buffer.get()));
		return this;
	}
}
