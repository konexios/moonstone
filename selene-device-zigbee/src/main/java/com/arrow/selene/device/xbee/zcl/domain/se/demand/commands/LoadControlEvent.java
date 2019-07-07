package com.arrow.selene.device.xbee.zcl.domain.se.demand.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.demand.data.CriticallyLevel;
import com.arrow.selene.device.xbee.zcl.domain.se.demand.data.DeviceClass;
import com.arrow.selene.device.xbee.zcl.domain.se.demand.data.EventControl;

public class LoadControlEvent extends ClusterSpecificCommand<LoadControlEvent> {
	private long issuerEventId;
	private Set<DeviceClass> deviceClasses;
	private int utilityEnrollmentGroup;
	private long startTime;
	private int durationInMinutes;
	private CriticallyLevel criticallyLevel;
	private int coolingTemperatureOffset;
	private int heatingTemperatureOffset;
	private int coolingTemperatureSetpoint;
	private int heatingTemperatureSetpoint;
	private int averageLoadAdjustmentPercentage;
	private int dutyCycle;
	private Set<EventControl> eventControls;

	public long getIssuerEventId() {
		return issuerEventId;
	}

	public LoadControlEvent withIssuerEventId(long issuerEventId) {
		this.issuerEventId = issuerEventId;
		return this;
	}

	public Set<DeviceClass> getDeviceClasses() {
		return deviceClasses;
	}

	public LoadControlEvent withDeviceClasses(Set<DeviceClass> deviceClasses) {
		this.deviceClasses = deviceClasses;
		return this;
	}

	public int getUtilityEnrollmentGroup() {
		return utilityEnrollmentGroup;
	}

	public LoadControlEvent withUtilityEnrollmentGroup(int utilityEnrollmentGroup) {
		this.utilityEnrollmentGroup = utilityEnrollmentGroup;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	public LoadControlEvent withStartTime(long startTime) {
		this.startTime = startTime;
		return this;
	}

	public int getDurationInMinutes() {
		return durationInMinutes;
	}

	public LoadControlEvent withDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
		return this;
	}

	public CriticallyLevel getCriticallyLevel() {
		return criticallyLevel;
	}

	public LoadControlEvent withCriticallyLevel(CriticallyLevel criticallyLevel) {
		this.criticallyLevel = criticallyLevel;
		return this;
	}

	public int getCoolingTemperatureOffset() {
		return coolingTemperatureOffset;
	}

	public LoadControlEvent withCoolingTemperatureOffset(int coolingTemperatureOffset) {
		this.coolingTemperatureOffset = coolingTemperatureOffset;
		return this;
	}

	public int getHeatingTemperatureOffset() {
		return heatingTemperatureOffset;
	}

	public LoadControlEvent withHeatingTemperatureOffset(int heatingTemperatureOffset) {
		this.heatingTemperatureOffset = heatingTemperatureOffset;
		return this;
	}

	public int getCoolingTemperatureSetpoint() {
		return coolingTemperatureSetpoint;
	}

	public LoadControlEvent withCoolingTemperatureSetpoint(int coolingTemperatureSetpoint) {
		this.coolingTemperatureSetpoint = coolingTemperatureSetpoint;
		return this;
	}

	public int getHeatingTemperatureSetpoint() {
		return heatingTemperatureSetpoint;
	}

	public LoadControlEvent withHeatingTemperatureSetpoint(int heatingTemperatureSetpoint) {
		this.heatingTemperatureSetpoint = heatingTemperatureSetpoint;
		return this;
	}

	public int getAverageLoadAdjustmentPercentage() {
		return averageLoadAdjustmentPercentage;
	}

	public LoadControlEvent withAverageLoadAdjustmentPercentage(int averageLoadAdjustmentPercentage) {
		this.averageLoadAdjustmentPercentage = averageLoadAdjustmentPercentage;
		return this;
	}

	public int getDutyCycle() {
		return dutyCycle;
	}

	public LoadControlEvent withDutyCycle(int dutyCycle) {
		this.dutyCycle = dutyCycle;
		return this;
	}

	public Set<EventControl> getEventControls() {
		return eventControls;
	}

	public LoadControlEvent withEventControls(Set<EventControl> eventControls) {
		this.eventControls = eventControls;
		return this;
	}

	@Override
	protected int getId() {
		return DemandResponseAndLoadControlClusterCommands.LOAD_CONTROL_EVENT_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(23);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) issuerEventId);
		buffer.putShort((short) DeviceClass.getByValues(deviceClasses));
		buffer.put((byte) utilityEnrollmentGroup);
		buffer.putInt((int) startTime);
		buffer.putShort((short) durationInMinutes);
		buffer.put((byte) criticallyLevel.ordinal());
		buffer.put((byte) coolingTemperatureOffset);
		buffer.put((byte) heatingTemperatureOffset);
		buffer.putShort((short) coolingTemperatureSetpoint);
		buffer.putShort((short) heatingTemperatureSetpoint);
		buffer.put((byte) averageLoadAdjustmentPercentage);
		buffer.put((byte) dutyCycle);
		buffer.put((byte) EventControl.getValue(eventControls));
		return buffer.array();
	}

	@Override
	public LoadControlEvent fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 23, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		issuerEventId = Integer.toUnsignedLong(buffer.getInt());
		deviceClasses = DeviceClass.getByValue(Short.toUnsignedInt(buffer.getShort()));
		utilityEnrollmentGroup = Byte.toUnsignedInt(buffer.get());
		startTime = Integer.toUnsignedLong(buffer.getInt());
		durationInMinutes = Short.toUnsignedInt(buffer.getShort());
		criticallyLevel = CriticallyLevel.values()[Byte.toUnsignedInt(buffer.get())];
		coolingTemperatureOffset = Byte.toUnsignedInt(buffer.get());
		heatingTemperatureOffset = Byte.toUnsignedInt(buffer.get());
		coolingTemperatureSetpoint = Short.toUnsignedInt(buffer.getShort());
		heatingTemperatureSetpoint = Short.toUnsignedInt(buffer.getShort());
		averageLoadAdjustmentPercentage = Byte.toUnsignedInt(buffer.get());
		dutyCycle = Byte.toUnsignedInt(buffer.get());
		eventControls = EventControl.getByValue(buffer.get());
		return this;
	}
}
