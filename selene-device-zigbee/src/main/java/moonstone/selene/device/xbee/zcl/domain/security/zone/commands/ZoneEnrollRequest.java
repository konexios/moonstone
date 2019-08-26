package moonstone.selene.device.xbee.zcl.domain.security.zone.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ZoneEnrollRequest extends ClusterSpecificCommand<ZoneEnrollRequest> {
	private short zoneType;
	private short manufacturerCode;

	public short getZoneType() {
		return zoneType;
	}

	public ZoneEnrollRequest withZoneType(short zoneType) {
		this.zoneType = zoneType;
		return this;
	}

	public short getManufacturerCode() {
		return manufacturerCode;
	}

	public ZoneEnrollRequest withManufacturerCode(short manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
		return this;
	}

	@Override
	protected int getId() {
		return SecurityZoneClusterCommands.ZONE_ENROLL_REQUEST_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort(zoneType);
		buffer.putShort(manufacturerCode);
		return buffer.array();
	}

	@Override
	public ZoneEnrollRequest fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		zoneType = buffer.getShort();
		manufacturerCode = buffer.getShort();
		return this;
	}
}
