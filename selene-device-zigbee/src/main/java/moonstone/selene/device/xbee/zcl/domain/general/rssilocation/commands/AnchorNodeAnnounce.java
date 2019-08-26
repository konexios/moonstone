package moonstone.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class AnchorNodeAnnounce extends ClusterSpecificCommand<AnchorNodeAnnounce> {
	private byte[] anchorNodeAddress;
	private short coord1;
	private short coord2;
	private short coord3;

	public byte[] getAnchorNodeAddress() {
		return anchorNodeAddress;
	}

	public AnchorNodeAnnounce withAnchorNodeAddress(byte[] anchorNodeAddress) {
		this.anchorNodeAddress = anchorNodeAddress;
		return this;
	}

	public short getCoord1() {
		return coord1;
	}

	public AnchorNodeAnnounce withCoord1(short coord1) {
		this.coord1 = coord1;
		return this;
	}

	public short getCoord2() {
		return coord2;
	}

	public AnchorNodeAnnounce withCoord2(short coord2) {
		this.coord2 = coord2;
		return this;
	}

	public short getCoord3() {
		return coord3;
	}

	public AnchorNodeAnnounce withCoord3(short coord3) {
		this.coord3 = coord3;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.ANCHOR_NODE_ANNOUNCE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(16);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(anchorNodeAddress);
		buffer.putShort(coord1);
		buffer.putShort(coord2);
		buffer.putShort(coord3);
		return buffer.array();
	}

	@Override
	public AnchorNodeAnnounce fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		anchorNodeAddress = new byte[8];
		buffer.get(anchorNodeAddress);
		coord1 = buffer.getShort();
		coord2 = buffer.getShort();
		coord3 = buffer.getShort();
		return this;
	}
}
