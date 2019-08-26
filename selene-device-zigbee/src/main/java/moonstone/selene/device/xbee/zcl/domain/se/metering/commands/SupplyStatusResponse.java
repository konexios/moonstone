package moonstone.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.metering.data.SupplyStatus;

public class SupplyStatusResponse extends ClusterSpecificCommand<SupplyStatusResponse> {
	private long providerId;
	private long issuerEventId;
	private long implementationTime;
	private SupplyStatus supplyStatus;

	public long getProviderId() {
		return providerId;
	}

	public SupplyStatusResponse withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public SupplyStatusResponse withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getImplementationTime() {
		return implementationTime;
	}

	public SupplyStatusResponse withImplementationTime(long implementationTime) {
		this.implementationTime = implementationTime;
		return this;
	}

	public SupplyStatus getSupplyStatus() {
		return supplyStatus;
	}

	public SupplyStatusResponse withSupplyStatus(SupplyStatus supplyStatus) {
		this.supplyStatus = supplyStatus;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.SUPPLY_STATUS_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(13);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) implementationTime);
		buffer.put((byte) supplyStatus.ordinal());
		return buffer.array();
	}

	@Override
	public SupplyStatusResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 13, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		implementationTime = Integer.toUnsignedLong(buffer.getInt());
		supplyStatus = SupplyStatus.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
