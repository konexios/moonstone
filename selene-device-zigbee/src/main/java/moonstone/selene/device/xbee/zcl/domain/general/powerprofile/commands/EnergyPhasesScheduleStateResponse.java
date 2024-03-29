package moonstone.selene.device.xbee.zcl.domain.general.powerprofile.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.general.powerprofile.data.ScheduledPhase;

public class EnergyPhasesScheduleStateResponse extends ClusterSpecificCommand<EnergyPhasesScheduleStateResponse> {
	private int powerProfileId;
	private ScheduledPhase[] scheduledPhases;

	public int getPowerProfileId() {
		return powerProfileId;
	}

	public EnergyPhasesScheduleStateResponse withPowerProfileId(int powerProfileId) {
		this.powerProfileId = powerProfileId;
		return this;
	}

	public ScheduledPhase[] getScheduledPhases() {
		return scheduledPhases;
	}

	public EnergyPhasesScheduleStateResponse withScheduledPhases(ScheduledPhase[] scheduledPhases) {
		this.scheduledPhases = scheduledPhases;
		return this;
	}

	@Override
	protected int getId() {
		return PowerProfileClusterCommands.ENERGY_PHASES_SCHEDULE_STATE_RESPONSE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(2 + scheduledPhases.length * 3);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) powerProfileId);
		buffer.put((byte) scheduledPhases.length);
		for (ScheduledPhase scheduledPhase : scheduledPhases) {
			buffer.put((byte) scheduledPhase.getEnergyPhaseId());
			buffer.putShort((short) scheduledPhase.getScheduledTime());
		}
		return buffer.array();
	}

	@Override
	public EnergyPhasesScheduleStateResponse fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 2, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		powerProfileId = Byte.toUnsignedInt(buffer.get());
		scheduledPhases = new ScheduledPhase[Byte.toUnsignedInt(buffer.get())];
		for (int i = 0; i < scheduledPhases.length; i++) {
			scheduledPhases[i] = new ScheduledPhase().withEnergyPhaseId(Byte.toUnsignedInt(buffer.get()))
					.withScheduledTime(Short.toUnsignedInt(buffer.getShort()));
		}
		return this;
	}
}
