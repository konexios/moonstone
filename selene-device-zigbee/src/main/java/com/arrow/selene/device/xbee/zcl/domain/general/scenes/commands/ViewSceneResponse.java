package com.arrow.selene.device.xbee.zcl.domain.general.scenes.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.ZclStatus;
import com.arrow.selene.device.xbee.zcl.domain.general.scenes.data.ExtensionField;

public class ViewSceneResponse extends ClusterSpecificCommand<ViewSceneResponse> {
	private int status;
	private int groupId;
	private int sceneId;
	private int transactionTime;
	private String sceneName;
	private Set<ExtensionField> extensionFields;

	public int getStatus() {
		return status;
	}

	public ViewSceneResponse withStatus(int status) {
		this.status = status;
		return this;
	}

	public int getGroupId() {
		return groupId;
	}

	public ViewSceneResponse withGroupId(int groupId) {
		this.groupId = groupId;
		return this;
	}

	public int getSceneId() {
		return sceneId;
	}

	public ViewSceneResponse withSceneId(int sceneId) {
		this.sceneId = sceneId;
		return this;
	}

	public int getTransactionTime() {
		return transactionTime;
	}

	public ViewSceneResponse withTransactionTime(int transactionTime) {
		this.transactionTime = transactionTime;
		return this;
	}

	public String getSceneName() {
		return sceneName;
	}

	public ViewSceneResponse withSceneName(String sceneName) {
		this.sceneName = sceneName;
		return this;
	}

	public Set<ExtensionField> getExtensionFields() {
		return extensionFields;
	}

	public ViewSceneResponse withExtensionFields(Set<ExtensionField> extensionFields) {
		this.extensionFields = extensionFields;
		return this;
	}

	@Override
	protected int getId() {
		return ScenesClusterCommands.VIEW_SCENE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		int extensionsSize = 0;
		if (status == ZclStatus.SUCCESS) {
			for (ExtensionField field : extensionFields) {
				extensionsSize += field.calcSize();
			}
		}
		ByteBuffer buffer = ByteBuffer.allocate(
				(status == ZclStatus.SUCCESS ? 6 + sceneName.length() : 4) + extensionsSize);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) status);
		buffer.putShort((short) groupId);
		buffer.put((byte) sceneId);
		if (status == ZclStatus.SUCCESS) {
			buffer.putShort((short) transactionTime);
			buffer.put(sceneName.getBytes(StandardCharsets.UTF_8));
			for (ExtensionField field : extensionFields) {
				buffer.put((byte) field.getClusterId());
				buffer.put((byte) field.getValue().length);
				buffer.put(field.getValue());
			}
		}
		return buffer.array();
	}

	@Override
	public ViewSceneResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 4, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		status = Byte.toUnsignedInt(buffer.get());
		groupId = Short.toUnsignedInt(buffer.getShort());
		sceneId = Byte.toUnsignedInt(buffer.get());
		if (status == ZclStatus.SUCCESS) {
			transactionTime = Short.toUnsignedInt(buffer.getShort());
			// FIXME check on real hardware
			//        byte[] name = new byte[buffer.remaining()];
			//        buffer.get(name);
			//        sceneName = new String(name);
			extensionFields.clear();
			while (buffer.hasRemaining()) {
				int clusterId = Byte.toUnsignedInt(buffer.get());
				byte[] value = new byte[buffer.get()];
				buffer.get(value);
				extensionFields.add(new ExtensionField().withClusterId(clusterId).withValue(value));
			}
		}
		return this;
	}
}
