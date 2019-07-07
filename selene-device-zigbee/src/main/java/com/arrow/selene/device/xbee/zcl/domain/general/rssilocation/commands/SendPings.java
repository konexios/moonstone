package com.arrow.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class SendPings extends ClusterSpecificCommand<SendPings> {
	private byte[] targetAddress;
	private byte numberRssiMeasurements;
	private short calcPeriod;

	public byte[] getTargetAddress() {
		return targetAddress;
	}

	public SendPings withTargetAddress(byte[] targetAddress) {
		this.targetAddress = targetAddress;
		return this;
	}

	public byte getNumberRssiMeasurements() {
		return numberRssiMeasurements;
	}

	public SendPings withNumberRssiMeasurements(byte numberRssiMeasurements) {
		this.numberRssiMeasurements = numberRssiMeasurements;
		return this;
	}

	public short getCalcPeriod() {
		return calcPeriod;
	}

	public SendPings withCalcPeriod(short calcPeriod) {
		this.calcPeriod = calcPeriod;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.SEND_PINGS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(11);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(targetAddress);
		buffer.put(numberRssiMeasurements);
		buffer.putShort(calcPeriod);
		return buffer.array();
	}

	@Override
	public SendPings fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		targetAddress = new byte[8];
		buffer.get(targetAddress);
		numberRssiMeasurements = buffer.get();
		calcPeriod = buffer.getShort();
		return this;
	}
}
