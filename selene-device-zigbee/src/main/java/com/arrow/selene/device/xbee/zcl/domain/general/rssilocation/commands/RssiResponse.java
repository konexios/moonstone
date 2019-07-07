package com.arrow.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class RssiResponse extends ClusterSpecificCommand<RssiResponse> {
	private byte[] replyingDevice;
	private short coord1;
	private short coord2;
	private short coord3;
	private byte rssi;
	private byte numberRssiMeasurements;

	public byte[] getReplyingDevice() {
		return replyingDevice;
	}

	public RssiResponse withReplyingDevice(byte[] replyingDevice) {
		this.replyingDevice = replyingDevice;
		return this;
	}

	public short getCoord1() {
		return coord1;
	}

	public RssiResponse withCoord1(short coord1) {
		this.coord1 = coord1;
		return this;
	}

	public short getCoord2() {
		return coord2;
	}

	public RssiResponse withCoord2(short coord2) {
		this.coord2 = coord2;
		return this;
	}

	public short getCoord3() {
		return coord3;
	}

	public RssiResponse withCoord3(short coord3) {
		this.coord3 = coord3;
		return this;
	}

	public byte getRssi() {
		return rssi;
	}

	public RssiResponse withRssi(byte rssi) {
		this.rssi = rssi;
		return this;
	}

	public byte getNumberRssiMeasurements() {
		return numberRssiMeasurements;
	}

	public RssiResponse withNumberRssiMeasurements(byte numberRssiMeasurements) {
		this.numberRssiMeasurements = numberRssiMeasurements;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.RSSI_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(16);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(replyingDevice);
		buffer.putShort(coord1);
		buffer.putShort(coord2);
		buffer.putShort(coord3);
		buffer.put(rssi);
		buffer.put(numberRssiMeasurements);
		return buffer.array();
	}

	@Override
	public RssiResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		replyingDevice = new byte[8];
		buffer.get(replyingDevice);
		coord1 = buffer.getShort();
		coord2 = buffer.getShort();
		coord3 = buffer.getShort();
		rssi = buffer.get();
		numberRssiMeasurements = buffer.get();
		return this;
	}
}
