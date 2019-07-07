package com.arrow.selene.device.xbee.zcl.domain.general.powerprofile.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.general.powerprofile.data.PowerProfile;
import com.arrow.selene.device.xbee.zcl.domain.general.powerprofile.data.PowerProfileState;

public class PowerProfileStateResponse extends ClusterSpecificCommand<PowerProfileStateResponse> {
	private PowerProfile[] powerProfiles;

	public PowerProfile[] getPowerProfiles() {
		return powerProfiles;
	}

	public PowerProfileStateResponse withPowerProfiles(PowerProfile[] powerProfiles) {
		this.powerProfiles = powerProfiles;
		return this;
	}

	@Override
	protected int getId() {
		return PowerProfileClusterCommands.POWER_PROFILE_STATE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(1 + powerProfiles.length * 4);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) powerProfiles.length);
		for (PowerProfile powerProfile : powerProfiles) {
			buffer.put((byte) powerProfile.getPowerProfileId());
			buffer.put((byte) powerProfile.getEnergyPhaseId());
			buffer.put((byte) (powerProfile.isPowerProfileRemoteControl() ? 0x01 : 0x00));
			buffer.put((byte) powerProfile.getPowerProfileState().ordinal());
		}
		return buffer.array();
	}

	@Override
	public PowerProfileStateResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 1, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		powerProfiles = new PowerProfile[Byte.toUnsignedInt(buffer.get())];
		for (int i = 0; i < powerProfiles.length; i++) {
			int powerProfileId = Byte.toUnsignedInt(buffer.get());
			int energyPhaseId = Byte.toUnsignedInt(buffer.get());
			boolean powerProfileRemoteControl = buffer.get() == 0x01;
			PowerProfileState powerProfileState = PowerProfileState.values()[buffer.get()];
			powerProfiles[i] = new PowerProfile().withPowerProfileId(powerProfileId).withEnergyPhaseId(energyPhaseId)
					.withPowerProfileRemoteControl(powerProfileRemoteControl).withPowerProfileState(powerProfileState);
		}
		return this;
	}
}
