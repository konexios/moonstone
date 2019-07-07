package com.arrow.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class EnhancedMoveToHueAndSaturation extends ClusterSpecificCommand<EnhancedMoveToHueAndSaturation> {
	private int hue;
	private int saturation;
	private int transactionTime;

	public int getHue() {
		return hue;
	}

	public EnhancedMoveToHueAndSaturation withHue(int hue) {
		this.hue = hue;
		return this;
	}

	public int getSaturation() {
		return saturation;
	}

	public EnhancedMoveToHueAndSaturation withSaturation(int saturation) {
		this.saturation = saturation;
		return this;
	}

	public int getTransactionTime() {
		return transactionTime;
	}

	public EnhancedMoveToHueAndSaturation withTransactionTime(int transactionTime) {
		this.transactionTime = transactionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.ENHANCED_MOVE_TO_HUE_AND_SATURATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) hue);
		buffer.put((byte) saturation);
		buffer.putShort((short) transactionTime);
		return buffer.array();
	}

	@Override
	public EnhancedMoveToHueAndSaturation fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 5, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		hue = Short.toUnsignedInt(buffer.getShort());
		saturation = Byte.toUnsignedInt(buffer.get());
		transactionTime = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
