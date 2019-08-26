package moonstone.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.metering.data.SupplyState;

public class SetSupplyStatus extends ClusterSpecificCommand<SetSupplyStatus> {
	private long issuerEventId;
	private SupplyState supplyTemperState;
	private SupplyState supplyDepletionState;
	private SupplyState supplyUncontrolledFlowState;
	private SupplyState loadLimitSupplyState;

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public SetSupplyStatus withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public SupplyState getSupplyTemperState() {
		return supplyTemperState;
	}

	public SetSupplyStatus withSupplyTemperState(SupplyState supplyTemperState) {
		this.supplyTemperState = supplyTemperState;
		return this;
	}

	public SupplyState getSupplyDepletionState() {
		return supplyDepletionState;
	}

	public SetSupplyStatus withSupplyDepletionState(SupplyState supplyDepletionState) {
		this.supplyDepletionState = supplyDepletionState;
		return this;
	}

	public SupplyState getSupplyUncontrolledFlowState() {
		return supplyUncontrolledFlowState;
	}

	public SetSupplyStatus withSupplyUncontrolledFlowState(SupplyState supplyUncontrolledFlowState) {
		this.supplyUncontrolledFlowState = supplyUncontrolledFlowState;
		return this;
	}

	public SupplyState getLoadLimitSupplyState() {
		return loadLimitSupplyState;
	}

	public SetSupplyStatus withLoadLimitSupplyState(SupplyState loadLimitSupplyState) {
		this.loadLimitSupplyState = loadLimitSupplyState;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.SET_SUPPLY_STATUS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) issuerEventId);
		buffer.put((byte) supplyTemperState.ordinal());
		buffer.put((byte) supplyDepletionState.ordinal());
		buffer.put((byte) supplyUncontrolledFlowState.ordinal());
		buffer.put((byte) loadLimitSupplyState.ordinal());
		return buffer.array();
	}

	@Override
	public SetSupplyStatus fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 8, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		supplyTemperState = SupplyState.values()[Byte.toUnsignedInt(buffer.get())];
		supplyDepletionState = SupplyState.values()[Byte.toUnsignedInt(buffer.get())];
		supplyUncontrolledFlowState = SupplyState.values()[Byte.toUnsignedInt(buffer.get())];
		loadLimitSupplyState = SupplyState.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
