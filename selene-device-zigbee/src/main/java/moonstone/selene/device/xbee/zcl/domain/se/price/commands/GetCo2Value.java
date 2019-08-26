package moonstone.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.price.data.TariffType;

public class GetCo2Value extends ClusterSpecificCommand<GetCo2Value> {
	private long earliestStartTime;
	private long minIssuerEvent;
	private int numberOfCommands;
	private TariffType tariffType;

	public long getEarliestStartTime() {
		return earliestStartTime;
	}

	public GetCo2Value withEarliestStartTime(long earliestStartTime) {
		this.earliestStartTime = earliestStartTime;
		return this;
	}

	public long getMinIssuerEvent() {
		return minIssuerEvent;
	}

	public GetCo2Value withMinIssuerEvent(long minIssuerEvent) {
		this.minIssuerEvent = minIssuerEvent;
		return this;
	}

	public int getNumberOfCommands() {
		return numberOfCommands;
	}

	public GetCo2Value withNumberOfCommands(int numberOfCommands) {
		this.numberOfCommands = numberOfCommands;
		return this;
	}

	public GetCo2Value withTariffType(TariffType tariffType) {
		this.tariffType = tariffType;
		return this;
	}

	public TariffType getTariffType() {
		return tariffType;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.GET_CO2_VALUE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(10);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) earliestStartTime);
		buffer.putInt((int) minIssuerEvent);
		buffer.put((byte) numberOfCommands);
		buffer.put((byte) tariffType.ordinal());
		return buffer.array();
	}

	@Override
	public GetCo2Value fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 10, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		earliestStartTime = Integer.toUnsignedLong(buffer.getInt());
		minIssuerEvent = Integer.toUnsignedLong(buffer.getInt());
		numberOfCommands = Byte.toUnsignedInt(buffer.get());
		tariffType = TariffType.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
