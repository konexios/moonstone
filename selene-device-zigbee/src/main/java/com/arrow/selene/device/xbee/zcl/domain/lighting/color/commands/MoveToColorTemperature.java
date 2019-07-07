package com.arrow.selene.device.xbee.zcl.domain.lighting.color.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class MoveToColorTemperature extends ClusterSpecificCommand<MoveToColorTemperature> {
	private int colorTemperature;
	private int transactionTime;

	public int getColorTemperature() {
		return colorTemperature;
	}

	public MoveToColorTemperature withColorTemperature(int colorTemperature) {
		this.colorTemperature = colorTemperature;
		return this;
	}

	public int getTransactionTime() {
		return transactionTime;
	}

	public MoveToColorTemperature withTransactionTime(int transactionTime) {
		this.transactionTime = transactionTime;
		return this;
	}

	@Override
	protected int getId() {
		return LightingColorClusterCommands.MOVE_TO_COLOR_TEMPERATURE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) colorTemperature);
		buffer.putShort((short) transactionTime);
		return buffer.array();
	}

	@Override
	public MoveToColorTemperature fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 4, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		colorTemperature = Short.toUnsignedInt(buffer.getShort());
		transactionTime = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
