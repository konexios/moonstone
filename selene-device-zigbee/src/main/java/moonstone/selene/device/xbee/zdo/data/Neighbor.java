package moonstone.selene.device.xbee.zdo.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.utils.ByteUtils;

public class Neighbor {
	private byte[] expandedPanId = new byte[8];
	private XBee64BitAddress expandedAddress;
	private XBee16BitAddress networkAddress;
	private DeviceType deviceType;
	private ReceiverState receiverOnWhenIdle;
	private Relationship relationship;
	private PermitJoining permitJoining;
	private int depth;
	private int lqi;
	private boolean on;

	public byte[] getExpandedPanId() {
		return expandedPanId;
	}

	public Neighbor withExpandedPanId(byte[] expandedPanId) {
		this.expandedPanId = expandedPanId;
		return this;
	}

	public XBee64BitAddress getExpandedAddress() {
		return expandedAddress;
	}

	public Neighbor withExpandedAddress(XBee64BitAddress expandedAddress) {
		this.expandedAddress = expandedAddress;
		return this;
	}

	public XBee16BitAddress getNetworkAddress() {
		return networkAddress;
	}

	public Neighbor withNetworkAddress(XBee16BitAddress networkAddress) {
		this.networkAddress = networkAddress;
		return this;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public Neighbor withDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
		return this;
	}

	public ReceiverState getReceiverOnWhenIdle() {
		return receiverOnWhenIdle;
	}

	public Neighbor withReceiverOnWhenIdle(ReceiverState receiverOnWhenIdle) {
		this.receiverOnWhenIdle = receiverOnWhenIdle;
		return this;
	}

	public Relationship getRelationship() {
		return relationship;
	}

	public Neighbor withRelationship(Relationship relationship) {
		this.relationship = relationship;
		return this;
	}

	public PermitJoining getPermitJoining() {
		return permitJoining;
	}

	public Neighbor withPermitJoining(PermitJoining permitJoining) {
		this.permitJoining = permitJoining;
		return this;
	}

	public int getDepth() {
		return depth;
	}

	public Neighbor withDepth(int depth) {
		this.depth = depth;
		return this;
	}

	public int getLqi() {
		return lqi;
	}

	public Neighbor withLqi(int lqi) {
		this.lqi = lqi;
		return this;
	}

	public boolean isOn() {
		return on;
	}

	public Neighbor withOn(boolean on) {
		this.on = on;
		return this;
	}

	public static Neighbor[] fromPayload(byte[] payload) {
		Neighbor[] result = new Neighbor[payload.length / 22];
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < result.length; i++) {
			byte[] expandedPanId = new byte[8];
			buffer.get(expandedPanId);
			Neighbor neighbor = new Neighbor().withExpandedPanId(ByteUtils.swapByteArray(expandedPanId));
			byte[] expandedAddress = new byte[8];
			buffer.get(expandedAddress);
			neighbor.withExpandedAddress(new XBee64BitAddress(ByteUtils.swapByteArray(expandedAddress)));
			byte[] networkAddress = new byte[2];
			buffer.get(networkAddress);
			neighbor.withNetworkAddress(new XBee16BitAddress(ByteUtils.swapByteArray(networkAddress)));
			int value = buffer.get();
			neighbor.withDeviceType(DeviceType.values()[value & 0b0_000_00_11]).withReceiverOnWhenIdle(
					ReceiverState.values()[(value & 0b0_000_11_00) >> 2]).withRelationship(
					Relationship.values()[(value & 0b0_111_00_00) >> 4]).withPermitJoining(
					PermitJoining.values()[buffer.get() & 0b0_000_00_11]).withDepth(Byte.toUnsignedInt(buffer.get()))
					.withLqi(Byte.toUnsignedInt(buffer.get()));
			result[i] = neighbor;
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Neighbor neighbor = (Neighbor) o;

		return Objects.equals(expandedAddress, neighbor.expandedAddress);
	}

	@Override
	public int hashCode() {
		return expandedAddress != null ? expandedAddress.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Neighbor{" + "expandedAddress=" + expandedAddress + ", lqi=" + lqi + '}';
	}
}
