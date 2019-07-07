package com.arrow.selene.device.xbee.zcl.domain.general.commissioning.commands;

import java.nio.ByteBuffer;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class ResetStartupParameters extends ClusterSpecificCommand<ResetStartupParameters> {
	private boolean resetCurrent;
	private boolean resetAll;
	private boolean eraseIndex;
	private byte index;

	public boolean isResetCurrent() {
		return resetCurrent;
	}

	public ResetStartupParameters withResetCurrent(boolean resetCurrent) {
		this.resetCurrent = resetCurrent;
		return this;
	}

	public boolean isResetAll() {
		return resetAll;
	}

	public ResetStartupParameters withResetAll(boolean resetAll) {
		this.resetAll = resetAll;
		return this;
	}

	public boolean isEraseIndex() {
		return eraseIndex;
	}

	public ResetStartupParameters withEraseIndex(boolean eraseIndex) {
		this.eraseIndex = eraseIndex;
		return this;
	}

	public byte getIndex() {
		return index;
	}

	public ResetStartupParameters withIndex(byte index) {
		this.index = index;
		return this;
	}

	@Override
	protected int getId() {
		return CommissioningClusterCommands.RESET_STARTUP_PARAMETERS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		int options = 0x00;
		if (resetCurrent) {
			options |= 0b00000_001;
		}
		if (resetAll) {
			options |= 0b00000_010;
		}
		if (eraseIndex) {
			options |= 0b00000_100;
		}
		buffer.put((byte) options);
		buffer.put(index);
		return buffer.array();
	}

	@Override
	public ResetStartupParameters fromPayload(byte[] payload) {
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		byte options = buffer.get();
		resetCurrent = (options & 0b00000_001) != 0;
		resetAll = (options & 0b00000_010) != 0;
		eraseIndex = (options & 0b00000_100) != 0;
		index = buffer.get();
		return this;
	}
}
