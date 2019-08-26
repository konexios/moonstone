package moonstone.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.digi.xbee.api.utils.ByteUtils;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.metering.data.NotificationScheme;

public class ConfigureMirror extends ClusterSpecificCommand<ConfigureMirror> {
	private long issuerEventId;
	private int reportingInterval;
	private boolean mirrorNotificationReporting;
	private NotificationScheme notificationScheme;

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public ConfigureMirror withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public int getReportingInterval() {
		return reportingInterval;
	}

	public ConfigureMirror withReportingInterval(int reportingInterval) {
		this.reportingInterval = reportingInterval;
		return this;
	}

	public boolean isMirrorNotificationReporting() {
		return mirrorNotificationReporting;
	}

	public ConfigureMirror withMirrorNotificationReporting(boolean mirrorNotificationReporting) {
		this.mirrorNotificationReporting = mirrorNotificationReporting;
		return this;
	}

	public NotificationScheme getNotificationScheme() {
		return notificationScheme;
	}

	public ConfigureMirror withNotificationScheme(NotificationScheme notificationScheme) {
		this.notificationScheme = notificationScheme;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.CONFIGURE_MIRROR_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(9);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) issuerEventId);
		buffer.put(ByteUtils.swapByteArray(ByteUtils.intToByteArray(reportingInterval)), 0, 3);
		buffer.put((byte) (mirrorNotificationReporting ? 0x01: 0x00));
		buffer.put((byte) notificationScheme.ordinal());
		return buffer.array();
	}

	@Override
	public ConfigureMirror fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 9, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		byte[] bytes = new byte[3];
		buffer.get(bytes);
		reportingInterval = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(bytes));
		mirrorNotificationReporting = buffer.get() == 0x01;
		notificationScheme = NotificationScheme.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
