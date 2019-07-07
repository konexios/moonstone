package com.arrow.selene.device.xbee.zcl;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.engine.Utils;

public abstract class NoPayloadCommand extends ClusterSpecificCommand<NoPayloadCommand> {
	@Override
	public final byte[] toPayload() {
		return Utils.EMPTY_BYTE_ARRAY;
	}

	@Override
	public final NoPayloadCommand fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 0, "payload length is incorrect");
		return this;
	}
}
