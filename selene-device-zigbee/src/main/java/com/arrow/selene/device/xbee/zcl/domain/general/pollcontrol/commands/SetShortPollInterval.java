package com.arrow.selene.device.xbee.zcl.domain.general.pollcontrol.commands;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.digi.xbee.api.utils.ByteUtils;

public class SetShortPollInterval extends ClusterSpecificCommand<SetShortPollInterval> {
	private int newShortPollInterval;

	public int getNewShortPollInterval() {
		return newShortPollInterval;
	}

	public SetShortPollInterval withNewShortPollInterval(int newShortPollInterval) {
		this.newShortPollInterval = newShortPollInterval;
		return this;
	}

	@Override
	protected int getId() {
		return PollControlClusterCommands.SET_SHORT_POLL_INTERVAL_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return ByteUtils.swapByteArray(ByteUtils.shortToByteArray((short) newShortPollInterval));
	}

	@Override
	public SetShortPollInterval fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		newShortPollInterval = ByteUtils.byteArrayToInt(ByteUtils.swapByteArray(payload));
		return this;
	}
}
