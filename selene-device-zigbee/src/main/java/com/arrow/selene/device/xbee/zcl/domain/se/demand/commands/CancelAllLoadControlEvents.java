package com.arrow.selene.device.xbee.zcl.domain.se.demand.commands;

import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.demand.data.CancelControl;

public class CancelAllLoadControlEvents extends ClusterSpecificCommand<CancelAllLoadControlEvents> {
	private Set<CancelControl> cancelControls;

	public Set<CancelControl> getCancelControls() {
		return cancelControls;
	}

	public CancelAllLoadControlEvents withCancelControls(Set<CancelControl> cancelControls) {
		this.cancelControls = cancelControls;
		return this;
	}

	@Override
	protected int getId() {
		return DemandResponseAndLoadControlClusterCommands.CANCEL_ALL_LOAD_CONTROL_EVENTS_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return new byte[]{(byte) CancelControl.getValue(cancelControls)};
	}

	@Override
	public CancelAllLoadControlEvents fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 1, "payload length is incorrect");
		cancelControls = CancelControl.getByValue(payload[0]);
		return this;
	}
}
