package com.arrow.selene.device.xbee.zcl.domain.general.appliance.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class WriteFunctions extends ClusterSpecificCommand<WriteFunctions> {
	private int functionId;
	private byte dataType;
	private byte[] data;

	public int getFunctionId() {
		return functionId;
	}

	public WriteFunctions withFunctionId(int functionId) {
		this.functionId = functionId;
		return this;
	}

	public byte getDataType() {
		return dataType;
	}

	public WriteFunctions withDataType(byte dataType) {
		this.dataType = dataType;
		return this;
	}

	public byte[] getData() {
		return data;
	}

	public WriteFunctions withData(byte[] data) {
		this.data = data;
		return this;
	}

	@Override
	protected int getId() {
		return ApplianceControlClusterCommands.EXECUTION_OF_A_COMMAND_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3 + data.length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) functionId);
		buffer.put(dataType);
		buffer.put(data);
		return buffer.array();
	}

	@Override
	public WriteFunctions fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length > 3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		functionId = Short.toUnsignedInt(buffer.getShort());
		dataType = buffer.get();
		data = new byte[buffer.remaining()];
		buffer.get(data);
		return this;
	}
}
