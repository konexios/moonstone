package com.arrow.selene.device.xbee.zcl.domain.general.appliance.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.general.appliance.data.ApplianceStatus;
import com.arrow.selene.device.xbee.zcl.domain.general.appliance.data.DeviceStatus;
import com.arrow.selene.device.xbee.zcl.domain.general.appliance.data.RemoteEnableFlags;

public class SignalStateNotification extends ClusterSpecificCommand<SignalStateNotification> {
	private ApplianceStatus status;
	private RemoteEnableFlags flag;
	private DeviceStatus deviceStatus;
	private byte[] applianceStatus2;

	public ApplianceStatus getStatus() {
		return status;
	}

	public SignalStateNotification withStatus(ApplianceStatus status) {
		this.status = status;
		return this;
	}

	public RemoteEnableFlags getFlag() {
		return flag;
	}

	public SignalStateNotification withFlag(RemoteEnableFlags flag) {
		this.flag = flag;
		return this;
	}

	public DeviceStatus getDeviceStatus() {
		return deviceStatus;
	}

	public SignalStateNotification withDeviceStatus(DeviceStatus deviceStatus) {
		this.deviceStatus = deviceStatus;
		return this;
	}

	public byte[] getApplianceStatus2() {
		return applianceStatus2;
	}

	public SignalStateNotification withApplianceStatus2(byte[] applianceStatus2) {
		this.applianceStatus2 = applianceStatus2;
		return this;
	}

	@Override
	protected int getId() {
		return ApplianceControlClusterCommands.SIGNAL_STATE_NOTIFICATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(applianceStatus2 == null ? 2 : 5);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) status.ordinal());
		buffer.put((byte) (flag.getValue() | deviceStatus.ordinal()));
		if (applianceStatus2 != null) {
			buffer.put(applianceStatus2);
		}
		return buffer.array();
	}

	@Override
	public SignalStateNotification fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2 || payload.length == 5, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		status = ApplianceStatus.values()[buffer.get()];
		byte value = buffer.get();
		flag = RemoteEnableFlags.getByValue(value & 0x0f);
		deviceStatus = DeviceStatus.values()[value >> 4 & 0x0f];
		if (buffer.hasRemaining()) {
			applianceStatus2 = new byte[3];
			buffer.get(applianceStatus2);
		}
		return this;
	}
}
