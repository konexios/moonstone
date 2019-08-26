package moonstone.selene.device.xbee.zcl.domain.general.scenes.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.ZclStatus;

public class GetSceneMembershipResponse extends ClusterSpecificCommand<GetSceneMembershipResponse> {
	private int status;
	private int capacity;
	private int groupId;
	private int sceneCount;
	private List<Integer> scenes;

	public int getStatus() {
		return status;
	}

	public GetSceneMembershipResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public int getCapacity() {
		return capacity;
	}

	public GetSceneMembershipResponse withCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	public int getGroupId() {
		return groupId;
	}

	public GetSceneMembershipResponse withGroupId(int groupId) {
		this.groupId = groupId;
		return this;
	}

	public int getSceneCount() {
		return sceneCount;
	}

	public GetSceneMembershipResponse withSceneCount(int sceneCount) {
		this.sceneCount = sceneCount;
		return this;
	}

	public List<Integer> getScenes() {
		return scenes;
	}

	public GetSceneMembershipResponse withScenes(List<Integer> scenes) {
		this.scenes = scenes;
		return this;
	}

	@Override
	protected int getId() {
		return ScenesClusterCommands.GET_SCENE_MEMBERSHIP_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(status == ZclStatus.SUCCESS ? 5 + scenes.size() : 4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) status);
		buffer.put((byte) capacity);
		buffer.putShort((short) groupId);
		if (status == ZclStatus.SUCCESS) {
			buffer.put((byte) sceneCount);
			for (Integer scene : scenes) {
				buffer.put(scene.byteValue());
			}
		}
		return buffer.array();
	}

	@Override
	public GetSceneMembershipResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 4, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		status = Byte.toUnsignedInt(buffer.get());
		capacity = Byte.toUnsignedInt(buffer.get());
		groupId = Short.toUnsignedInt(buffer.getShort());
		if (status == ZclStatus.SUCCESS) {
			sceneCount = Byte.toUnsignedInt(buffer.get());
			scenes.clear();
			for (int i = 0; i < sceneCount; i++) {
				scenes.add((int) buffer.get());
			}
		}
		return this;
	}
}
