package com.arrow.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.ZclStatus;

public class DeviceConfigurationResponse extends ClusterSpecificCommand<DeviceConfigurationResponse> {
	private byte status;
	private short power;
	private short pathLostExponent;
	private short calcPeriod;
	private byte numberRssiMeasurements;
	private short reportingPeriod;

	public byte getStatus() {
		return status;
	}

	public DeviceConfigurationResponse withStatus(byte status) {
		this.status = status;
		return this;
	}

	public short getPower() {
		return power;
	}

	public DeviceConfigurationResponse withPower(short power) {
		this.power = power;
		return this;
	}

	public short getPathLostExponent() {
		return pathLostExponent;
	}

	public DeviceConfigurationResponse withPathLostExponent(short pathLostExponent) {
		this.pathLostExponent = pathLostExponent;
		return this;
	}

	public short getCalcPeriod() {
		return calcPeriod;
	}

	public DeviceConfigurationResponse withCalcPeriod(short calcPeriod) {
		this.calcPeriod = calcPeriod;
		return this;
	}

	public byte getNumberRssiMeasurements() {
		return numberRssiMeasurements;
	}

	public DeviceConfigurationResponse withNumberRssiMeasurements(byte numberRssiMeasurements) {
		this.numberRssiMeasurements = numberRssiMeasurements;
		return this;
	}

	public short getReportingPeriod() {
		return reportingPeriod;
	}

	public DeviceConfigurationResponse withReportingPeriod(short reportingPeriod) {
		this.reportingPeriod = reportingPeriod;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.DEVICE_CONFIGURATION_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(status == ZclStatus.SUCCESS ? 10 : 1);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(status);
		if (status == ZclStatus.SUCCESS) {
			buffer.putShort(power);
			buffer.putShort(pathLostExponent);
			buffer.putShort(calcPeriod);
			buffer.put(numberRssiMeasurements);
			buffer.putShort(reportingPeriod);
		}
		return buffer.array();
	}

	@Override
	public DeviceConfigurationResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		status = buffer.get();
		if (status == ZclStatus.SUCCESS) {
			power = buffer.getShort();
			pathLostExponent = buffer.getShort();
			calcPeriod = buffer.getShort();
			numberRssiMeasurements = buffer.get();
			reportingPeriod = buffer.getShort();
		}
		return this;
	}
}
