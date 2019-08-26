package moonstone.selene.device.xbee.zcl.domain.general.alarms.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class Alarm extends ClusterSpecificCommand<Alarm> {
	private byte alarmCode;
	private short clusterId;

	public byte getAlarmCode() {
		return alarmCode;
	}

	public Alarm withAlarmCode(byte alarmCode) {
		this.alarmCode = alarmCode;
		return this;
	}

	public short getClusterId() {
		return clusterId;
	}

	public Alarm withClusterId(short clusterId) {
		this.clusterId = clusterId;
		return this;
	}

	@Override
	protected int getId() {
		return AlarmsClusterCommands.ALARM_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(alarmCode);
		buffer.putShort(clusterId);
		return buffer.array();
	}

	@Override
	public Alarm fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		alarmCode = buffer.get();
		clusterId = buffer.getShort();
		return this;
	}
}
