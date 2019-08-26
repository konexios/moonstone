package moonstone.selene.device.xbee.zcl.domain.general.powerprofile.commands;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class EnergyPhasesScheduleStateRequest extends ClusterSpecificCommand<EnergyPhasesScheduleStateRequest> {
	private int powerProfileId;

	public int getPowerProfileId() {
		return powerProfileId;
	}

	public EnergyPhasesScheduleStateRequest withPowerProfileId(int powerProfileId) {
		this.powerProfileId = powerProfileId;
		return this;
	}

	@Override
	protected int getId() {
		return PowerProfileClusterCommands.ENERGY_PHASES_SCHEDULE_STATE_REQUEST_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return new byte[] {(byte) powerProfileId};
	}

	@Override
	public EnergyPhasesScheduleStateRequest fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 1, "payload length is incorrect");
		powerProfileId = Byte.toUnsignedInt(payload[0]);
		return this;
	}
}
