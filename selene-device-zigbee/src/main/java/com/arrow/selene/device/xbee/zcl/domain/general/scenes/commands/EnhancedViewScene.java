package com.arrow.selene.device.xbee.zcl.domain.general.scenes.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class EnhancedViewScene extends ClusterSpecificCommand<EnhancedViewScene> {
	private int groupId;
	private int sceneId;

	public int getGroupId() {
		return groupId;
	}

	public EnhancedViewScene withGroupId(int groupId) {
		this.groupId = groupId;
		return this;
	}

	public int getSceneId() {
		return sceneId;
	}

	public EnhancedViewScene withSceneId(int sceneId) {
		this.sceneId = sceneId;
		return this;
	}

	@Override
	protected int getId() {
		return ScenesClusterCommands.ENHANCED_VIEW_SCENE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) groupId);
		buffer.put((byte) sceneId);
		return buffer.array();
	}

	@Override
	public EnhancedViewScene fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		groupId = Short.toUnsignedInt(buffer.getShort());
		sceneId = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
