package moonstone.selene.device.xbee.zcl.domain.security.zone.commands;

import java.nio.ByteBuffer;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ZoneEnrollResponse extends ClusterSpecificCommand<ZoneEnrollResponse> {
	private int enrollResponseCode;
	private int zoneId;

	public int getEnrollResponseCode() {
		return enrollResponseCode;
	}

	public ZoneEnrollResponse withEnrollResponseCode(int enrollResponseCode) {
		this.enrollResponseCode = enrollResponseCode;
		return this;
	}

	public int getZoneId() {
		return zoneId;
	}

	public ZoneEnrollResponse withZoneId(int zoneId) {
		this.zoneId = zoneId;
		return this;
	}

	@Override
	protected int getId() {
		return SecurityZoneClusterCommands.ZONE_ENROLL_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		byte[] buffer = new byte[2];
		buffer[0] = (byte) enrollResponseCode;
		buffer[1] = (byte) zoneId;
		return buffer;
	}

	@Override
	public ZoneEnrollResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		enrollResponseCode = Byte.toUnsignedInt(buffer.get());
		zoneId = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
