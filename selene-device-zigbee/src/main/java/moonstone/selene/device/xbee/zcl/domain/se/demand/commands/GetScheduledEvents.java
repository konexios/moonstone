package moonstone.selene.device.xbee.zcl.domain.se.demand.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetScheduledEvents extends ClusterSpecificCommand<GetScheduledEvents> {
	private long startTime;
	private int numberOfEvents;

	public long getStartTime() {
		return startTime;
	}

	public GetScheduledEvents withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public int getNumberOfEvents() {
		return numberOfEvents;
	}

	public GetScheduledEvents withNumberOfEvents(int numberOfEvents) {
		this.numberOfEvents = numberOfEvents;
		return this;
	}

	@Override
	protected int getId() {
		return DemandResponseAndLoadControlClusterCommands.GET_SCHEDULED_EVENTS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) startTime);
		buffer.put((byte)numberOfEvents);
		return buffer.array();
	}

	@Override
	public GetScheduledEvents fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 5, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		startTime = Integer.toUnsignedLong(buffer.getInt());
		numberOfEvents = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
