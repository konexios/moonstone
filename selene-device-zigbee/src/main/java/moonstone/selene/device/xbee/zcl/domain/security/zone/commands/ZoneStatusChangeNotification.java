package moonstone.selene.device.xbee.zcl.domain.security.zone.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ZoneStatusChangeNotification extends ClusterSpecificCommand<ZoneStatusChangeNotification> {
	private int zoneStatus;
	private byte extendedStatus;
	private int zoneId;
	private int delay;

	public int getZoneStatus() {
		return zoneStatus;
	}

	public ZoneStatusChangeNotification withZoneStatus(int zoneStatus) {
		this.zoneStatus = zoneStatus;
		return this;
	}

	public byte getExtendedStatus() {
		return extendedStatus;
	}

	public ZoneStatusChangeNotification withExtendedStatus(byte extendedStatus) {
		this.extendedStatus = extendedStatus;
		return this;
	}

	public int getZoneId() {
		return zoneId;
	}

	public ZoneStatusChangeNotification withZoneId(int zoneId) {
		this.zoneId = zoneId;
		return this;
	}

	public int getDelay() {
		return delay;
	}

	public ZoneStatusChangeNotification withDelay(int delay) {
		this.delay = delay;
		return this;
	}

	@Override
	protected int getId() {
		return SecurityZoneClusterCommands.ZONE_STATUS_CHANGE_NOTIFICATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(6);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) zoneStatus);
		buffer.put(extendedStatus);
		buffer.put((byte) zoneId);
		buffer.putShort((short) delay);
		return buffer.array();
	}

	@Override
	public ZoneStatusChangeNotification fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 6, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		zoneStatus = Short.toUnsignedInt(buffer.getShort());
		extendedStatus = buffer.get();
		zoneId = Byte.toUnsignedInt(buffer.get());
		delay = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
