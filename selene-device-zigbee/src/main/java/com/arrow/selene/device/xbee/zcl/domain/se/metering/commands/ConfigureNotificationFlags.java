package com.arrow.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.metering.data.NotificationScheme;

public class ConfigureNotificationFlags extends ClusterSpecificCommand<ConfigureNotificationFlags> {
	private long issuerEventId;
	private NotificationScheme notificationScheme;
	private int notificationFlagAttributeId;
	private int clusterId;
	private int manufacturerCode;
	private int[] commandIds;

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public ConfigureNotificationFlags withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public NotificationScheme getNotificationScheme() {
		return notificationScheme;
	}

	public ConfigureNotificationFlags withNotificationScheme(NotificationScheme notificationScheme) {
		this.notificationScheme = notificationScheme;
		return this;
	}

	public int getNotificationFlagAttributeId() {
		return notificationFlagAttributeId;
	}

	public ConfigureNotificationFlags withNotificationFlagAttributeId(int notificationFlagAttributeId) {
		this.notificationFlagAttributeId = notificationFlagAttributeId;
		return this;
	}

	public int getClusterId() {
		return clusterId;
	}

	public ConfigureNotificationFlags withClusterId(int clusterId) {
		this.clusterId = clusterId;
		return this;
	}

	public int getManufacturerCode() {
		return manufacturerCode;
	}

	public ConfigureNotificationFlags withManufacturerCode(int manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
		return this;
	}

	public int[] getCommandIds() {
		return commandIds;
	}

	public ConfigureNotificationFlags withCommandIds(int[] commandIds) {
		this.commandIds = commandIds;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.CONFIGURE_NOTIFICATION_FLAGS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(12 + commandIds.length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) issuerEventId);
		buffer.put((byte) notificationScheme.ordinal());
		buffer.putShort((short) notificationFlagAttributeId);
		buffer.putShort((short) clusterId);
		buffer.putShort((short) manufacturerCode);
		buffer.put((byte) commandIds.length);
		for (int item : commandIds) {
			buffer.put((byte) item);
		}
		return buffer.array();
	}

	@Override
	public ConfigureNotificationFlags fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 13 && payload.length <= 44, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		notificationScheme = NotificationScheme.values()[Byte.toUnsignedInt(buffer.get())];
		notificationFlagAttributeId = Short.toUnsignedInt(buffer.getShort());
		clusterId = Short.toUnsignedInt(buffer.getShort());
		manufacturerCode = Short.toUnsignedInt(buffer.getShort());
		commandIds = new int[Byte.toUnsignedInt(buffer.get())];
		for (int i = 0; i < commandIds.length; i++) {
			commandIds[i] = Byte.toUnsignedInt(buffer.get());
		}
		return this;
	}
}
