package com.arrow.selene.device.xbee.zcl.domain.ha.events.data;

public class AlertStructure {
	private int alertId;
	private Category category;
	private PresenceRecovery presenceRecovery;
	private byte proprietary;

	public int getAlertId() {
		return alertId;
	}

	public AlertStructure withAlertId(int alertId) {
		this.alertId = alertId;
		return this;
	}

	public Category getCategory() {
		return category;
	}

	public AlertStructure withCategory(Category category) {
		this.category = category;
		return this;
	}

	public PresenceRecovery getPresenceRecovery() {
		return presenceRecovery;
	}

	public AlertStructure withPresenceRecovery(PresenceRecovery presenceRecovery) {
		this.presenceRecovery = presenceRecovery;
		return this;
	}

	public byte getProprietary() {
		return proprietary;
	}

	public AlertStructure withProprietary(byte proprietary) {
		this.proprietary = proprietary;
		return this;
	}
}
