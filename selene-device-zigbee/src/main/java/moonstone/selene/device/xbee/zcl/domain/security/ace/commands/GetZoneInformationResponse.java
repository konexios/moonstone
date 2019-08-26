package moonstone.selene.device.xbee.zcl.domain.security.ace.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class GetZoneInformationResponse extends ClusterSpecificCommand<GetZoneInformationResponse> {
	private byte zoneId;
	private short zoneType;
	private byte[] ieeeAddress;

	public byte getZoneId() {
		return zoneId;
	}

	public GetZoneInformationResponse withZoneId(byte zoneId) {
		this.zoneId = zoneId;
		return this;
	}

	public short getZoneType() {
		return zoneType;
	}

	public GetZoneInformationResponse withZoneType(short zoneType) {
		this.zoneType = zoneType;
		return this;
	}

	public byte[] getIeeeAddress() {
		return ieeeAddress;
	}

	public GetZoneInformationResponse withIeeeAddress(byte[] ieeeAddress) {
		this.ieeeAddress = ieeeAddress;
		return this;
	}

	@Override
	protected int getId() {
		return SecurityAceClusterCommands.GET_ZONE_INFORMATION_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(11);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(zoneId);
		buffer.putShort(zoneType);
		buffer.put(ieeeAddress);
		return buffer.array();
	}

	@Override
	public GetZoneInformationResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		zoneId = buffer.get();
		zoneType = buffer.getShort();
		ieeeAddress = new byte[8];
		buffer.get(ieeeAddress);
		return this;
	}
}
