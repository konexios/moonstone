package com.arrow.selene.device.xbee.zcl.general;

import java.nio.ByteBuffer;

import com.arrow.selene.device.xbee.zcl.ZclHeader;

public class DiscoverCommandsGeneratedResponse extends GeneralResponse<DiscoverCommandsGeneratedResponse> {
	private boolean discoveryComplete;
	private byte[] commands;

	public boolean isDiscoveryComplete() {
		return discoveryComplete;
	}

	public byte[] getCommands() {
		return commands;
	}

	@Override
	protected DiscoverCommandsGeneratedResponse fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		discoveryComplete = buffer.get() == 0x01;
		commands = new byte[buffer.remaining()];
		buffer.get(commands);
		return this;
	}

	@Override
	protected int getId() {
		return HaProfileCommands.DISCOVER_COMMANDS_GENERATED_RSP;
	}

	public ZclHeader getHeader() {
		return header;
	}
}
