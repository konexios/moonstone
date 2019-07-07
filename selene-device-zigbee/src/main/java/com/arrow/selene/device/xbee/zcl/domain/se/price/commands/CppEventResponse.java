package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.price.data.CppAuth;

public class CppEventResponse extends ClusterSpecificCommand<CppEventResponse> {
	private long minIssuerEvent;
	private CppAuth cppAuth;

	public long getMinIssuerEvent() {
		return minIssuerEvent;
	}

	public CppEventResponse withMinIssuerEvent(long minIssuerEvent) {
		this.minIssuerEvent = minIssuerEvent;
		return this;
	}

	public CppAuth getCppAuth() {
		return cppAuth;
	}

	public CppEventResponse withCppAuth(CppAuth cppAuth) {
		this.cppAuth = cppAuth;
		return this;
	}

	@Override
	protected int getId() {
		return PriceClusterCommands.CPP_EVENT_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(5);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt((int) minIssuerEvent);
		buffer.put((byte) cppAuth.ordinal());
		return buffer.array();
	}

	@Override
	public CppEventResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 5, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		minIssuerEvent = Integer.toUnsignedLong(buffer.getInt());
		cppAuth = CppAuth.values()[Byte.toUnsignedInt(buffer.get())];
		return this;
	}
}
