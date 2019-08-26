package moonstone.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.TariffType;

public class GetBlockPeriods extends ClusterSpecificCommand<GetBlockPeriods> {
	private long startTime;
	private int numberOfEvents;
	private TariffType tariffType;

	public long getStartTime() {
		return startTime;
	}

	public GetBlockPeriods withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public int getNumberOfEvents() {
		return numberOfEvents;
	}

	public GetBlockPeriods withNumberOfEvents(int numberOfEvents) {
		this.numberOfEvents = numberOfEvents;
		return this;
	}

	public TariffType getTariffType() {
		return tariffType;
	}

	public GetBlockPeriods withTariffType(TariffType tariffType) {
		this.tariffType = tariffType;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.GET_BLOCK_PERIODS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(6);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) startTime);
		buffer.put((byte) numberOfEvents);
		buffer.put((byte) tariffType.ordinal());
		return buffer.array();
	}

	@Override
	public GetBlockPeriods fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 6, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		startTime = Integer.toUnsignedLong(buffer.getInt());
		numberOfEvents = Byte.toUnsignedInt(buffer.get());
		tariffType = TariffType.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
