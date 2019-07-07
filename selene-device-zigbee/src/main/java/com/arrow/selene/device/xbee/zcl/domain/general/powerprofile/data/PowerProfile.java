package com.arrow.selene.device.xbee.zcl.domain.general.powerprofile.data;

public class PowerProfile {
	private int powerProfileId;
	private int energyPhaseId;
	private boolean powerProfileRemoteControl;
	private PowerProfileState powerProfileState;

	public int getPowerProfileId() {
		return powerProfileId;
	}

	public PowerProfile withPowerProfileId(int powerProfileId) {
		this.powerProfileId = powerProfileId;
		return this;
	}

	public int getEnergyPhaseId() {
		return energyPhaseId;
	}

	public PowerProfile withEnergyPhaseId(int energyPhaseId) {
		this.energyPhaseId = energyPhaseId;
		return this;
	}

	public boolean isPowerProfileRemoteControl() {
		return powerProfileRemoteControl;
	}

	public PowerProfile withPowerProfileRemoteControl(boolean powerProfileRemoteControl) {
		this.powerProfileRemoteControl = powerProfileRemoteControl;
		return this;
	}

	public PowerProfileState getPowerProfileState() {
		return powerProfileState;
	}

	public PowerProfile withPowerProfileState(PowerProfileState powerProfileState) {
		this.powerProfileState = powerProfileState;
		return this;
	}
}
