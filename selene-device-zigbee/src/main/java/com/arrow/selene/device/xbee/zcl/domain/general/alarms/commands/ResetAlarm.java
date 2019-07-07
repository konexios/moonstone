package com.arrow.selene.device.xbee.zcl.domain.general.alarms.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ResetAlarm extends ClusterSpecificCommand<ResetAlarm> {
	private byte alarmCode;
	private short clusterId;

	public byte getAlarmCode() {
		return alarmCode;
	}

	public ResetAlarm withAlarmCode(byte alarmCode) {
		this.alarmCode = alarmCode;
		return this;
	}

	public short getClusterId() {
		return clusterId;
	}

	public ResetAlarm withClusterId(short clusterId) {
		this.clusterId = clusterId;
		return this;
	}

	@Override
	protected int getId() {
		return AlarmsClusterCommands.RESET_ALARM_COMMAND_ID;
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
	public ResetAlarm fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		alarmCode = buffer.get();
		clusterId = buffer.getShort();
		return this;
	}
}
