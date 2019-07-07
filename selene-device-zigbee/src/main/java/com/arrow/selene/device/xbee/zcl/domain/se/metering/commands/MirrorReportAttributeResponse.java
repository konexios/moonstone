package com.arrow.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.metering.data.NotificationFlags;
import com.arrow.selene.device.xbee.zcl.domain.se.metering.data.NotificationScheme;

public class MirrorReportAttributeResponse extends ClusterSpecificCommand<MirrorReportAttributeResponse> {
	private NotificationScheme notificationScheme;
	private NotificationFlags notificationFlag1;
	private NotificationFlags notificationFlag2;
	private NotificationFlags notificationFlag3;
	private NotificationFlags notificationFlag4;
	private NotificationFlags notificationFlag5;
	private NotificationFlags notificationFlag6;
	private NotificationFlags notificationFlag7;
	private NotificationFlags notificationFlag8;

	public NotificationScheme getNotificationScheme() {
		return notificationScheme;
	}

	public MirrorReportAttributeResponse withNotificationScheme(NotificationScheme notificationScheme) {
		this.notificationScheme = notificationScheme;
		return this;
	}

	public NotificationFlags getNotificationFlag1() {
		return notificationFlag1;
	}

	public MirrorReportAttributeResponse withNotificationFlag1(NotificationFlags notificationFlag1) {
		this.notificationFlag1 = notificationFlag1;
		return this;
	}

	public NotificationFlags getNotificationFlag2() {
		return notificationFlag2;
	}

	public MirrorReportAttributeResponse withNotificationFlag2(NotificationFlags notificationFlag2) {
		this.notificationFlag2 = notificationFlag2;
		return this;
	}

	public NotificationFlags getNotificationFlag3() {
		return notificationFlag3;
	}

	public MirrorReportAttributeResponse withNotificationFlag3(NotificationFlags notificationFlag3) {
		this.notificationFlag3 = notificationFlag3;
		return this;
	}

	public NotificationFlags getNotificationFlag4() {
		return notificationFlag4;
	}

	public MirrorReportAttributeResponse withNotificationFlag4(NotificationFlags notificationFlag4) {
		this.notificationFlag4 = notificationFlag4;
		return this;
	}

	public NotificationFlags getNotificationFlag5() {
		return notificationFlag5;
	}

	public MirrorReportAttributeResponse withNotificationFlag5(NotificationFlags notificationFlag5) {
		this.notificationFlag5 = notificationFlag5;
		return this;
	}

	public NotificationFlags getNotificationFlag6() {
		return notificationFlag6;
	}

	public MirrorReportAttributeResponse withNotificationFlag6(NotificationFlags notificationFlag6) {
		this.notificationFlag6 = notificationFlag6;
		return this;
	}

	public NotificationFlags getNotificationFlag7() {
		return notificationFlag7;
	}

	public MirrorReportAttributeResponse withNotificationFlag7(NotificationFlags notificationFlag7) {
		this.notificationFlag7 = notificationFlag7;
		return this;
	}

	public NotificationFlags getNotificationFlag8() {
		return notificationFlag8;
	}

	public MirrorReportAttributeResponse withNotificationFlag8(NotificationFlags notificationFlag8) {
		this.notificationFlag8 = notificationFlag8;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.MIRROR_REPORT_ATTRIBUTE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) notificationScheme.ordinal());
		buffer.put((byte) (notificationFlag1.ordinal() | notificationFlag2.ordinal() << 4));
		buffer.put((byte) (notificationFlag3.ordinal() | notificationFlag4.ordinal() << 4));
		buffer.put((byte) (notificationFlag5.ordinal() | notificationFlag6.ordinal() << 4));
		buffer.put((byte) (notificationFlag7.ordinal() | notificationFlag8.ordinal() << 4));
		return buffer.array();
	}

	@Override
	public MirrorReportAttributeResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 5, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		notificationScheme = NotificationScheme.values()[Byte.toUnsignedInt(buffer.get())];
		int value = Byte.toUnsignedInt(buffer.get());
		notificationFlag1 = NotificationFlags.values()[value & 0x0f];
		notificationFlag2 = NotificationFlags.values()[value >> 4];
		value = Byte.toUnsignedInt(buffer.get());
		notificationFlag3 = NotificationFlags.values()[value & 0x0f];
		notificationFlag4 = NotificationFlags.values()[value >> 4];
		value = Byte.toUnsignedInt(buffer.get());
		notificationFlag5 = NotificationFlags.values()[value & 0x0f];
		notificationFlag6 = NotificationFlags.values()[value >> 4];
		value = Byte.toUnsignedInt(buffer.get());
		notificationFlag7 = NotificationFlags.values()[value & 0x0f];
		notificationFlag8 = NotificationFlags.values()[value >> 4];
		return this;
	}
}
