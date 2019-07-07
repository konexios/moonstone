package com.arrow.selene.device.xbee.zcl.domain.security.ace.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.security.zone.attributes.ZoneStatus;

public class ZoneStatusChanged extends ClusterSpecificCommand<ZoneStatusChanged> {
	private int zoneId;
	private Set<ZoneStatus> zoneStatuses;
	private String zoneLabel;

	public int getZoneId() {
		return zoneId;
	}

	public ZoneStatusChanged withZoneId(int zoneId) {
		this.zoneId = zoneId;
		return this;
	}

	public Set<ZoneStatus> getZoneStatuses() {
		return zoneStatuses;
	}

	public ZoneStatusChanged withZoneStatuses(Set<ZoneStatus> zoneStatuses) {
		this.zoneStatuses = zoneStatuses;
		return this;
	}

	public String getZoneLabel() {
		return zoneLabel;
	}

	public ZoneStatusChanged withZoneLabel(String zoneLabel) {
		this.zoneLabel = zoneLabel;
		return this;
	}

	@Override
	protected int getId() {
		return SecurityAceClusterCommands.ZONE_STATUS_CHANGED_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(11);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) zoneId);
		buffer.putShort((short) ZoneStatus.getValue(zoneStatuses));
		buffer.put(zoneLabel.getBytes(StandardCharsets.UTF_8));
		return buffer.array();
	}

	@Override
	public ZoneStatusChanged fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 11, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		zoneId = Byte.toUnsignedInt(buffer.get());
		byte[] zoneStatus = new byte[2];
		buffer.get(zoneStatus);
		zoneStatuses = ZoneStatus.getByValue(zoneStatus);
		byte[] label = new byte[8];
		buffer.get(label);
		zoneLabel = new String(label, StandardCharsets.UTF_8);
		return this;
	}
}
