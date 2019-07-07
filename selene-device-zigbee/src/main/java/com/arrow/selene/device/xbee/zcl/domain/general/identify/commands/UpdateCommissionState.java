package com.arrow.selene.device.xbee.zcl.domain.general.identify.commands;

import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.general.identify.attributes.CommissionState;
import com.arrow.selene.device.xbee.zcl.domain.general.identify.data.CommissionStateAction;

public class UpdateCommissionState extends ClusterSpecificCommand<UpdateCommissionState> {
	private CommissionStateAction action;
	private Set<CommissionState> commissionStates;

	public CommissionStateAction getAction() {
		return action;
	}

	public UpdateCommissionState withAction(CommissionStateAction action) {
		this.action = action;
		return this;
	}

	public Set<CommissionState> getCommissionStates() {
		return commissionStates;
	}

	public UpdateCommissionState withCommissionStates(Set<CommissionState> commissionStates) {
		this.commissionStates = commissionStates;
		return this;
	}

	@Override
	protected int getId() {
		return IdentifyClusterCommands.UPDATE_COMMISSION_STATE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) action.ordinal();
		bytes[1] = (byte) CommissionState.getByValues(commissionStates);
		return bytes;
	}

	@Override
	public UpdateCommissionState fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 2, "payload length is incorrect");
		action = CommissionStateAction.values()[payload[0]];
		commissionStates = CommissionState.getByValue(payload[1]);
		return this;
	}
}
