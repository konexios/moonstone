package com.arrow.selene.device.xbee.zcl.domain.general.scenes.commands;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.digi.xbee.api.utils.ByteUtils;

public class GetSceneMembership extends ClusterSpecificCommand<GetSceneMembership> {
	private int groupId;

	public int getGroupId() {
		return groupId;
	}

	public GetSceneMembership withGroupId(int groupId) {
		this.groupId = groupId;
		return this;
	}

	@Override
	protected int getId() {
		return ScenesClusterCommands.GET_SCENE_MEMBERSHIP_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return ByteUtils.swapByteArray(ByteUtils.shortToByteArray((short) groupId));
	}

	@Override
	public GetSceneMembership fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		groupId = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(payload));
		return this;
	}
}
