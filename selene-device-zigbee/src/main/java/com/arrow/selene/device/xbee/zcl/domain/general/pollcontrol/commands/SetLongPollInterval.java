package com.arrow.selene.device.xbee.zcl.domain.general.pollcontrol.commands;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.digi.xbee.api.utils.ByteUtils;

public class SetLongPollInterval extends ClusterSpecificCommand<SetLongPollInterval> {
	private long newLongPollInterval;

	public long getNewLongPollInterval() {
		return newLongPollInterval;
	}

	public SetLongPollInterval withNewLongPollInterval(long newLongPollInterval) {
		this.newLongPollInterval = newLongPollInterval;
		return this;
	}

	@Override
	protected int getId() {
		return PollControlClusterCommands.SET_LONG_POLL_INTERVAL_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return ByteUtils.swapByteArray(ByteUtils.intToByteArray((int) newLongPollInterval));
	}

	@Override
	public SetLongPollInterval fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 4, "payload length is incorrect");
		newLongPollInterval = ByteUtils.byteArrayToLong(ByteUtils.swapByteArray(payload));
		return this;
	}
}
