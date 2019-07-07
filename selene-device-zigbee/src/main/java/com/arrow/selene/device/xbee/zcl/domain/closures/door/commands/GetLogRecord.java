package com.arrow.selene.device.xbee.zcl.domain.closures.door.commands;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.digi.xbee.api.utils.ByteUtils;

public class GetLogRecord extends ClusterSpecificCommand<GetLogRecord> {
	private int logIndex;

	public int getLogIndex() {
		return logIndex;
	}

	public GetLogRecord withLogIndex(int logIndex) {
		this.logIndex = logIndex;
		return this;
	}

	@Override
	protected int getId() {
		return DoorLockClusterCommands.GET_LOG_RECORD_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return ByteUtils.swapByteArray(ByteUtils.shortToByteArray((short) logIndex));
	}

	@Override
	protected GetLogRecord fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		logIndex = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(payload));
		return this;
	}
}
