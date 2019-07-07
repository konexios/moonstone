package com.arrow.selene.device.xbee.zcl.domain.general.identify.commands;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.digi.xbee.api.utils.ByteUtils;

public class IdentifyQueryResponse extends ClusterSpecificCommand<IdentifyQueryResponse> {
	private int timeout;

	public int getTimeout() {
		return timeout;
	}

	public IdentifyQueryResponse withTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	@Override
	protected int getId() {
		return IdentifyClusterCommands.IDENTIFY_QUERY_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return ByteUtils.swapByteArray(ByteUtils.shortToByteArray((short) timeout));
	}

	@Override
	public IdentifyQueryResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		timeout = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(payload));
		return this;
	}
}
