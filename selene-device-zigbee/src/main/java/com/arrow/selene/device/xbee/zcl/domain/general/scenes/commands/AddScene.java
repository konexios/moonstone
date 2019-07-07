package com.arrow.selene.device.xbee.zcl.domain.general.scenes.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.general.scenes.data.ExtensionField;

public class AddScene extends ClusterSpecificCommand<AddScene> {
	private int groupId;
	private int sceneId;
	private int transactionTime;
	private String sceneName;
	private Set<ExtensionField> extensionFields;

	public int getGroupId() {
		return groupId;
	}

	public AddScene withGroupId(int groupId) {
		this.groupId = groupId;
		return this;
	}

	public int getSceneId() {
		return sceneId;
	}

	public AddScene withSceneId(int sceneId) {
		this.sceneId = sceneId;
		return this;
	}

	public int getTransactionTime() {
		return transactionTime;
	}

	public AddScene withTransactionTime(int transactionTime) {
		this.transactionTime = transactionTime;
		return this;
	}

	public String getSceneName() {
		return sceneName;
	}

	public AddScene withSceneName(String sceneName) {
		this.sceneName = sceneName;
		return this;
	}

	public Set<ExtensionField> getExtensionFields() {
		return extensionFields;
	}

	public AddScene withExtensionFields(Set<ExtensionField> extensionFields) {
		this.extensionFields = extensionFields;
		return this;
	}

	@Override
	protected int getId() {
		return ScenesClusterCommands.ADD_SCENE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		int extensionsSize = 0;
		for (ExtensionField field : extensionFields) {
			extensionsSize += field.calcSize();
		}
		ByteBuffer buffer = ByteBuffer.allocate(5 + sceneName.length() + extensionsSize);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) groupId);
		buffer.put((byte) sceneId);
		buffer.putShort((short) transactionTime);
		buffer.put(sceneName.getBytes(StandardCharsets.UTF_8));
		for (ExtensionField field : extensionFields) {
			buffer.put((byte) field.getClusterId());
			buffer.put((byte) field.getValue().length);
			buffer.put(field.getValue());
		}
		return buffer.array();
	}

	@Override
	public AddScene fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 5, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		groupId = Short.toUnsignedInt(buffer.getShort());
		sceneId = Byte.toUnsignedInt(buffer.get());
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
		return this;
	}
}
