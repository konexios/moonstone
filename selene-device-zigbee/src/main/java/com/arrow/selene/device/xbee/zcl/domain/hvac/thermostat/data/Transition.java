package com.arrow.selene.device.xbee.zcl.domain.hvac.thermostat.data;

public class Transition {
	private int transitionTime;
	private short heatSetpoint;
	private short coolSetpoint;

	public int getTransitionTime() {
		return transitionTime;
	}

	public Transition withTransitionTime(int transitionTime) {
		this.transitionTime = transitionTime;
		return this;
	}

	public short getHeatSetpoint() {
		return heatSetpoint;
	}

	public Transition withHeatSetpoint(short heatSetpoint) {
		this.heatSetpoint = heatSetpoint;
		return this;
	}

	public short getCoolSetpoint() {
		return coolSetpoint;
	}

	public Transition withCoolSetpoint(short coolSetpoint) {
		this.coolSetpoint = coolSetpoint;
		return this;
	}
}
