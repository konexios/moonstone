package com.arrow.selene.device.xbee.zcl.domain.general.scenes.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class CopySceneResponse extends ClusterSpecificCommand<CopySceneResponse> {
	private int status;
	private int fromGroupId;
	private int fromSceneId;

	public int getStatus() {
		return status;
	}

	public CopySceneResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public int getFromGroupId() {
		return fromGroupId;
	}

	public CopySceneResponse withFromGroupId(int fromGroupId) {
		this.fromGroupId = fromGroupId;
		return this;
	}

	public int getFromSceneId() {
		return fromSceneId;
	}

	public CopySceneResponse withFromSceneId(int fromSceneId) {
		this.fromSceneId = fromSceneId;
		return this;
	}

	@Override
	protected int getId() {
		return ScenesClusterCommands.COPY_SCENE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) status);
		buffer.putShort((short) fromGroupId);
		buffer.put((byte) fromSceneId);
		return buffer.array();
	}

	@Override
	public CopySceneResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 4, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		status = Byte.toUnsignedInt(buffer.get());
		fromGroupId = Short.toUnsignedInt(buffer.getShort());
		fromSceneId = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
