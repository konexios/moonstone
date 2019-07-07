package com.arrow.selene.device.xbee.zcl.domain.general.commissioning.commands;

import java.nio.ByteBuffer;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class RestoreStartupParameters extends ClusterSpecificCommand<RestoreStartupParameters> {
	private byte options;
	private byte index;

	public byte getOptions() {
		return options;
	}

	public RestoreStartupParameters withOptions(byte options) {
		this.options = options;
		return this;
	}

	public byte getIndex() {
		return index;
	}

	public RestoreStartupParameters withIndex(byte index) {
		this.index = index;
		return this;
	}

	@Override
	protected int getId() {
		return CommissioningClusterCommands.RESTORE_STARTUP_PARAMETERS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.put(options);
		buffer.put(index);
		return buffer.array();
	}

	@Override
	public RestoreStartupParameters fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		options = buffer.get();
		index = buffer.get();
		return this;
	}
}
