package moonstone.selene.device.xbee.zcl.domain.se.demand.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.se.demand.data.CancelControl;
import moonstone.selene.device.xbee.zcl.domain.se.demand.data.DeviceClass;

public class CancelLoadControlEvent extends ClusterSpecificCommand<CancelLoadControlEvent> {
	private long issuerEventId;
	private Set<DeviceClass> deviceClasses;
	private int utilityEnrollmentGroup;
	private Set<CancelControl> cancelControls;
	private long effectiveTime;

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public CancelLoadControlEvent withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public Set<DeviceClass> getDeviceClasses() {
		return deviceClasses;
	}

	public CancelLoadControlEvent withDeviceClasses(Set<DeviceClass> deviceClasses) {
		this.deviceClasses = deviceClasses;
		return this;
	}

	public int getUtilityEnrollmentGroup() {
		return utilityEnrollmentGroup;
	}

	public CancelLoadControlEvent withUtilityEnrollmentGroup(int utilityEnrollmentGroup) {
		this.utilityEnrollmentGroup = utilityEnrollmentGroup;
		return this;
	}

	public Set<CancelControl> getCancelControls() {
		return cancelControls;
	}

	public CancelLoadControlEvent withCancelControls(Set<CancelControl> cancelControls) {
		this.cancelControls = cancelControls;
		return this;
	}

	public long getEffectiveTime() {
		return effectiveTime;
	}

	public CancelLoadControlEvent withEffectiveTime(long effectiveTime) {
		this.effectiveTime = effectiveTime;
		return this;
	}

	@Override
	protected int getId() {
		return DemandResponseAndLoadControlClusterCommands.CANCEL_LOAD_CONTROL_EVENT_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(12);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) issuerEventId);
		buffer.putShort((short) DeviceClass.getByValues(deviceClasses));
		buffer.put((byte) utilityEnrollmentGroup);
		buffer.put((byte) CancelControl.getValue(cancelControls));
		buffer.putInt((int) effectiveTime);
		return buffer.array();
	}

	@Override
	public CancelLoadControlEvent fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 12, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		deviceClasses = DeviceClass.getByValue(Short.toUnsignedInt(buffer.getShort()));
		utilityEnrollmentGroup = Byte.toUnsignedInt(buffer.get());
		cancelControls = CancelControl.getByValue(buffer.get());
		effectiveTime = Integer.toUnsignedLong(buffer.getInt());
		return this;
	}
}
