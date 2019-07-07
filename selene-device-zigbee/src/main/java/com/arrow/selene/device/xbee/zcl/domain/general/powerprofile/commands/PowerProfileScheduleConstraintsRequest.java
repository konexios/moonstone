package com.arrow.selene.device.xbee.zcl.domain.general.powerprofile.commands;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class PowerProfileScheduleConstraintsRequest
		extends ClusterSpecificCommand<PowerProfileScheduleConstraintsRequest> {
	private int powerProfileId;

	public int getPowerProfileId() {
		return powerProfileId;
	}

	public PowerProfileScheduleConstraintsRequest withPowerProfileId(int powerProfileId) {
		this.powerProfileId = powerProfileId;
		return this;
	}

	@Override
	protected int getId() {
		return PowerProfileClusterCommands.POWER_PROFILE_SCHEDULE_CONSTRAINTS_REQUEST_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return new byte[]{(byte) powerProfileId};
	}

	@Override
	public PowerProfileScheduleConstraintsRequest fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 1, "payload length is incorrect");
		powerProfileId = Byte.toUnsignedInt(payload[0]);
		return this;
	}
}
