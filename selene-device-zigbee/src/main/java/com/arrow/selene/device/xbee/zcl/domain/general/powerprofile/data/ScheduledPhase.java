package com.arrow.selene.device.xbee.zcl.domain.general.powerprofile.data;

public class ScheduledPhase {
	private int energyPhaseId;
	private int scheduledTime;

	public int getEnergyPhaseId() {
		return energyPhaseId;
	}

	public ScheduledPhase withEnergyPhaseId(int energyPhaseId) {
		this.energyPhaseId = energyPhaseId;
		return this;
	}

	public int getScheduledTime() {
		return scheduledTime;
	}

	public ScheduledPhase withScheduledTime(int scheduledTime) {
		this.scheduledTime = scheduledTime;
		return this;
	}
}
