package moonstone.selene.device.xbee.zcl.domain.general.powerprofile.commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;
import moonstone.selene.device.xbee.zcl.domain.general.powerprofile.data.EnergyPhase;

public class PowerProfileNotification extends ClusterSpecificCommand<PowerProfileNotification> {
	private int totalProfileNum;
	private int powerProfileId;
	private EnergyPhase[] energyPhases;

	public int getTotalProfileNum() {
		return totalProfileNum;
	}

	public PowerProfileNotification withTotalProfileNum(int totalProfileNum) {
		this.totalProfileNum = totalProfileNum;
		return this;
	}

	public int getPowerProfileId() {
		return powerProfileId;
	}

	public PowerProfileNotification withPowerProfileId(int powerProfileId) {
		this.powerProfileId = powerProfileId;
		return this;
	}

	public EnergyPhase[] getEnergyPhases() {
		return energyPhases;
	}

	public PowerProfileNotification withEnergyPhases(EnergyPhase[] energyPhases) {
		this.energyPhases = energyPhases;
		return this;
	}

	@Override
	protected int getId() {
		return PowerProfileClusterCommands.POWER_PROFILE_NOTIFICATION_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		ByteBuffer buffer = ByteBuffer.allocate(3 + energyPhases.length * 10);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) totalProfileNum);
		buffer.put((byte) powerProfileId);
		buffer.put((byte) energyPhases.length);
		for (EnergyPhase energyPhase : energyPhases) {
			buffer.put((byte) energyPhase.getEnergyPhaseId());
			buffer.put((byte) energyPhase.getMacroPhaseId());
			buffer.putShort((short) energyPhase.getExpectedDuration());
			buffer.putShort((short) energyPhase.getPeakPower());
			buffer.putShort((short) energyPhase.getEnergy());
			buffer.putShort((short) energyPhase.getMaxActivationDelay());
		}
		return buffer.array();
	}

	@Override
	public PowerProfileNotification fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length >= 3, "payload length is incorrect");
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		totalProfileNum = Byte.toUnsignedInt(buffer.get());
		powerProfileId = Byte.toUnsignedInt(buffer.get());
		energyPhases = new EnergyPhase[Byte.toUnsignedInt(buffer.get())];
		for (int i = 0; i < energyPhases.length; i++) {
			int energyPhaseId = Byte.toUnsignedInt(buffer.get());
			int macroPhaseId = Byte.toUnsignedInt(buffer.get());
			int expectedDuration = Short.toUnsignedInt(buffer.getShort());
			int peakPower = Short.toUnsignedInt(buffer.getShort());
			int energy = Short.toUnsignedInt(buffer.getShort());
			int maxActivationDelay = Short.toUnsignedInt(buffer.getShort());
			energyPhases[i] = new EnergyPhase().withEnergyPhaseId(energyPhaseId).withMacroPhaseId(macroPhaseId)
					.withExpectedDuration(expectedDuration).withPeakPower(peakPower).withEnergy(energy)
					.withMaxActivationDelay(maxActivationDelay);
		}
		return this;
	}
}
