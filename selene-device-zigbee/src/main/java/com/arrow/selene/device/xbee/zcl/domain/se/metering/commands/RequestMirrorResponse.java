package com.arrow.selene.device.xbee.zcl.domain.se.metering.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class RequestMirrorResponse extends ClusterSpecificCommand<RequestMirrorResponse> {
	private int endpointId;

	public int getEndpointId() {
		return endpointId;
	}

	public RequestMirrorResponse withEndpointId(int endpointId) {
		this.endpointId = endpointId;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.REQUEST_MIRROR_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) endpointId);
		return buffer.array();
	}

	@Override
	public RequestMirrorResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		endpointId = Short.toUnsignedInt(buffer.getShort());
		return this;
	}
}
