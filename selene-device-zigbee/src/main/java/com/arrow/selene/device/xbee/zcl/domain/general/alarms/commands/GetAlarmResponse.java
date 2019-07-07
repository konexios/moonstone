package com.arrow.selene.device.xbee.zcl.domain.general.alarms.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.ZclStatus;

public class GetAlarmResponse extends ClusterSpecificCommand<GetAlarmResponse> {
	private byte status;
	private byte alarmCode;
	private short clusterId;
	private int timestamp;

	public byte getStatus() {
		return status;
	}

	public GetAlarmResponse withStatus(byte status) {
		this.status = status;
		return this;
	}

	public byte getAlarmCode() {
		return alarmCode;
	}

	public GetAlarmResponse withAlarmCode(byte alarmCode) {
		this.alarmCode = alarmCode;
		return this;
	}

	public short getClusterId() {
		return clusterId;
	}

	public GetAlarmResponse withClusterId(short clusterId) {
		this.clusterId = clusterId;
		return this;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public GetAlarmResponse withTimestamp(int timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	@Override
	protected int getId() {
		return AlarmsClusterCommands.GET_ALARM_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(status == ZclStatus.SUCCESS ? 8 : 1);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(status);
		if (status == ZclStatus.SUCCESS) {
			buffer.put(alarmCode);
			buffer.putShort(clusterId);
			buffer.putInt(timestamp);
		}
		return buffer.array();
	}

	@Override
	public GetAlarmResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		status = buffer.get();
		if (status == ZclStatus.SUCCESS) {
			alarmCode = buffer.get();
			clusterId = buffer.getShort();
			timestamp = buffer.getInt();
		}
		return this;
	}
}
