package com.arrow.selene.device.xbee.zcl.domain.general.powerprofile.data;

public class EnergyPhase {
	private int energyPhaseId;
	private int macroPhaseId;
	private int expectedDuration;
	private int peakPower;
	private int energy;
	private int maxActivationDelay;

	public int getEnergyPhaseId() {
		return energyPhaseId;
	}

	public EnergyPhase withEnergyPhaseId(int energyPhaseId) {
		this.energyPhaseId = energyPhaseId;
		return this;
	}

	public int getMacroPhaseId() {
		return macroPhaseId;
	}

	public EnergyPhase withMacroPhaseId(int macroPhaseId) {
		this.macroPhaseId = macroPhaseId;
		return this;
	}

	public int getExpectedDuration() {
		return expectedDuration;
	}

	public EnergyPhase withExpectedDuration(int expectedDuration) {
		this.expectedDuration = expectedDuration;
		return this;
	}

	public int getPeakPower() {
		return peakPower;
	}

	public EnergyPhase withPeakPower(int peakPower) {
		this.peakPower = peakPower;
		return this;
	}

	public int getEnergy() {
		return energy;
	}

	public EnergyPhase withEnergy(int energy) {
		this.energy = energy;
		return this;
	}

	public int getMaxActivationDelay() {
		return maxActivationDelay;
	}

	public EnergyPhase withMaxActivationDelay(int maxActivationDelay) {
		this.maxActivationDelay = maxActivationDelay;
		return this;
	}
}
