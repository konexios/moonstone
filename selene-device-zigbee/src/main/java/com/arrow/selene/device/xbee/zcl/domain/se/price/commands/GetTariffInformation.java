package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.TariffType;

public class GetTariffInformation extends ClusterSpecificCommand<GetTariffInformation> {
	private long earliestStartTime;
	private long minIssuerEventId;
	private int numberOfCommands;
	private TariffType tariffType;

	public long getEarliestStartTime() {
		return earliestStartTime;
	}

	public GetTariffInformation withEarliestStartTime(long earliestStartTime) {
		this.earliestStartTime = earliestStartTime;
		return this;
	}

	public long getMinIssuerEventId() {
		return minIssuerEventId;
	}

	public GetTariffInformation withMinIssuerEventId(long minIssuerEventId) {
		this.minIssuerEventId = minIssuerEventId;
		return this;
	}

	public int getNumberOfCommands() {
		return numberOfCommands;
	}

	public GetTariffInformation withNumberOfCommands(int numberOfCommands) {
		this.numberOfCommands = numberOfCommands;
		return this;
	}

	public TariffType getTariffType() {
		return tariffType;
	}

	public GetTariffInformation withTariffType(TariffType tariffType) {
		this.tariffType = tariffType;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.GET_TARIFF_INFORMATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(10);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) earliestStartTime);
		buffer.putInt((int) minIssuerEventId);
		buffer.put((byte) numberOfCommands);
		buffer.put((byte) tariffType.ordinal());
		return buffer.array();
	}

	@Override
	public GetTariffInformation fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 10, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		earliestStartTime = Integer.toUnsignedLong(buffer.getInt());
		minIssuerEventId = Integer.toUnsignedLong(buffer.getInt());
		numberOfCommands = Byte.toUnsignedInt(buffer.get());
		tariffType = TariffType.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
