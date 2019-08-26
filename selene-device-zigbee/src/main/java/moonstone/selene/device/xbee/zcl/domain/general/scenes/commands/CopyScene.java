package moonstone.selene.device.xbee.zcl.domain.general.scenes.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.general.scenes.data.CopyMode;

public class CopyScene extends ClusterSpecificCommand<CopyScene> {
	private CopyMode copyMode;
	private int fromGroupId;
	private int fromSceneId;
	private int toGroupId;
	private int toSceneId;

	public CopyMode getCopyMode() {
		return copyMode;
	}

	public CopyScene withCopyMode(CopyMode copyMode) {
		this.copyMode = copyMode;
		return this;
	}

	public int getFromGroupId() {
		return fromGroupId;
	}

	public CopyScene withFromGroupId(int fromGroupId) {
		this.fromGroupId = fromGroupId;
		return this;
	}

	public int getFromSceneId() {
		return fromSceneId;
	}

	public CopyScene withFromSceneId(int fromSceneId) {
		this.fromSceneId = fromSceneId;
		return this;
	}

	public int getToGroupId() {
		return toGroupId;
	}

	public CopyScene withToGroupId(int toGroupId) {
		this.toGroupId = toGroupId;
		return this;
	}

	public int getToSceneId() {
		return toSceneId;
	}

	public CopyScene withToSceneId(int toSceneId) {
		this.toSceneId = toSceneId;
		return this;
	}

	@Override
	protected int getId() {
		return ScenesClusterCommands.COPY_SCENE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(7);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) copyMode.ordinal());
		buffer.putShort((short) fromGroupId);
		buffer.put((byte) fromSceneId);
		buffer.putShort((short) toGroupId);
		buffer.put((byte) toSceneId);
		return buffer.array();
	}

	@Override
	public CopyScene fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 7, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		copyMode = CopyMode.values()[Byte.toUnsignedInt(buffer.get())];
		fromGroupId = Short.toUnsignedInt(buffer.getShort());
		fromSceneId = Byte.toUnsignedInt(buffer.get());
		toGroupId = Short.toUnsignedInt(buffer.getShort());
		toSceneId = Byte.toUnsignedInt(buffer.get());
		return this;
	}
}
