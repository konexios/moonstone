package moonstone.selene.device.xbee.zcl.domain.general.scenes.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class EnhancedAddSceneResponse extends ClusterSpecificCommand<EnhancedAddSceneResponse> {
	private int status;
	private int groupId;
	private int sceneId;

	public int getStatus() {
		return status;
	}

	public EnhancedAddSceneResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public int getGroupId() {
		return groupId;
	}

	public EnhancedAddSceneResponse withGroupId(int groupId) {
		this.groupId = groupId;
		return this;
	}

	public int getSceneId() {
		return sceneId;
	}

	public EnhancedAddSceneResponse withSceneId(int sceneId) {
		this.sceneId = sceneId;
		return this;
	}

	@Override
	protected int getId() {
		return ScenesClusterCommands.ENHANCED_ADD_SCENE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) status);
		buffer.putShort((short) groupId);
		buffer.put((byte) sceneId);
		return buffer.array();
	}

	@Override
	public EnhancedAddSceneResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 4, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		status = Byte.toUnsignedInt(buffer.get());
		groupId = Short.toUnsignedInt(buffer.getShort());
		sceneId = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
