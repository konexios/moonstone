package com.arrow.selene.device.zigbee.data;

import java.io.Serializable;

public class PermitJoinPayload implements Serializable {
	private static final long serialVersionUID = -6573440869194238615L;

	private int duration;

	public int getDuration() {
		return duration;
	}
}
