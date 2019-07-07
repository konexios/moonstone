package com.arrow.selene.device.xbee.zcl.domain.general.rssilocation.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class SetAbsoluteLocation extends ClusterSpecificCommand<SetAbsoluteLocation> {
	private short coord1;
	private short coord2;
	private short coord3;
	private short power;
	private short pathLossExponent;

	public short getCoord1() {
		return coord1;
	}

	public SetAbsoluteLocation withCoord1(short coord1) {
		this.coord1 = coord1;
		return this;
	}

	public short getCoord2() {
		return coord2;
	}

	public SetAbsoluteLocation withCoord2(short coord2) {
		this.coord2 = coord2;
		return this;
	}

	public short getCoord3() {
		return coord3;
	}

	public SetAbsoluteLocation withCoord3(short coord3) {
		this.coord3 = coord3;
		return this;
	}

	public short getPower() {
		return power;
	}

	public SetAbsoluteLocation withPower(short power) {
		this.power = power;
		return this;
	}

	public short getPathLossExponent() {
		return pathLossExponent;
	}

	public SetAbsoluteLocation withPathLossExponent(short pathLossExponent) {
		this.pathLossExponent = pathLossExponent;
		return this;
	}

	@Override
	protected int getId() {
		return RssiLocationClusterCommands.SET_ABSOLUTE_LOCATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(10);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort(coord1);
		buffer.putShort(coord2);
		buffer.putShort(coord3);
		buffer.putShort(power);
		buffer.putShort(pathLossExponent);
		return buffer.array();
	}

	@Override
	public SetAbsoluteLocation fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		coord1 = buffer.getShort();
		coord2 = buffer.getShort();
		coord3 = buffer.getShort();
		power = buffer.getShort();
		pathLossExponent = buffer.getShort();
		return this;
	}
}
