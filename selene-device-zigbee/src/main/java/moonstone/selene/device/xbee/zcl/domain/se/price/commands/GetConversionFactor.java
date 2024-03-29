package moonstone.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetConversionFactor extends ClusterSpecificCommand<GetConversionFactor> {
	private long earliestStartTime;
	private long minIssuerEventId;
	private int numberOfCommands;

	public long getEarliestStartTime() {
		return earliestStartTime;
	}

	public GetConversionFactor withEarliestStartTime(long earliestStartTime) {
		this.earliestStartTime = earliestStartTime;
		return this;
	}

	public long getMinIssuerEventId() {
		return minIssuerEventId;
	}

	public GetConversionFactor withMinIssuerEventId(long minIssuerEventId) {
		this.minIssuerEventId = minIssuerEventId;
		return this;
	}

	public int getNumberOfCommands() {
		return numberOfCommands;
	}

	public GetConversionFactor withNumberOfCommands(int numberOfCommands) {
		this.numberOfCommands = numberOfCommands;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.GET_CONVERSION_FACTOR_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(7);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) earliestStartTime);
		buffer.putInt((int) minIssuerEventId);
		buffer.put((byte) numberOfCommands);
		return buffer.array();
	}

	@Override
	public GetConversionFactor fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 7, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		earliestStartTime = Integer.toUnsignedLong(buffer.getInt());
		minIssuerEventId = Integer.toUnsignedLong(buffer.getInt());
		numberOfCommands = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
