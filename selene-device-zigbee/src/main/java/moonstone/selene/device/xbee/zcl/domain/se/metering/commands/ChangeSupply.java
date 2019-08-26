package moonstone.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.metering.data.SupplyControl;
import moonstone.selene.device.xbee.zcl.domain.se.metering.data.SupplyStatus;

public class ChangeSupply extends ClusterSpecificCommand<ChangeSupply> {
	private long providerId;
	private long issuerEventId;
	private long requestTime;
	private long implementationTime;
	private SupplyStatus proposedSupplyStatus;
	private Set<SupplyControl> supplyControls;

	public long getProviderId() {
		return providerId;
	}

	public ChangeSupply withProviderId(long providerId) {
		this.providerId = providerId;
		return this;
	}

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public ChangeSupply withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public long getRequestTime() {
		return requestTime;
	}

	public ChangeSupply withRequestTime(long requestTime) {
		this.requestTime = requestTime;
		return this;
	}

	public long getImplementationTime() {
		return implementationTime;
	}

	public ChangeSupply withImplementationTime(long implementationTime) {
		this.implementationTime = implementationTime;
		return this;
	}

	public SupplyStatus getProposedSupplyStatus() {
		return proposedSupplyStatus;
	}

	public ChangeSupply withProposedSupplyStatus(SupplyStatus proposedSupplyStatus) {
		this.proposedSupplyStatus = proposedSupplyStatus;
		return this;
	}

	public Set<SupplyControl> getSupplyControls() {
		return supplyControls;
	}

	public ChangeSupply withSupplyControls(Set<SupplyControl> supplyControls) {
		this.supplyControls = supplyControls;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.CHANGE_SUPPLY_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(18);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) providerId);
		buffer.putInt((int) issuerEventId);
		buffer.putInt((int) requestTime);
		buffer.putInt((int) implementationTime);
		buffer.put((byte) proposedSupplyStatus.ordinal());
		buffer.put((byte) SupplyControl.getByValue(supplyControls));
		return buffer.array();
	}

	@Override
	public ChangeSupply fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 18, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		providerId = Integer.toUnsignedLong(buffer.getInt());
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		requestTime = Integer.toUnsignedLong(buffer.getInt());
		implementationTime = Integer.toUnsignedLong(buffer.getInt());
		proposedSupplyStatus = SupplyStatus.values()[Byte.toUnsignedInt(buffer.get())];
		supplyControls = SupplyControl.getByValue(buffer.get());
		return this;
	}
}
