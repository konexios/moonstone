package com.arrow.selene.device.xbee.zcl.domain.general.identify.commands;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.general.identify.data.EzModeInvokeAction;

public class EzModeInvoke extends ClusterSpecificCommand<EzModeInvoke> {
	private EzModeInvokeAction action;

	public EzModeInvokeAction getAction() {
		return action;
	}

	public EzModeInvoke withAction(EzModeInvokeAction action) {
		this.action = action;
		return this;
	}

	@Override
	protected int getId() {
		return IdentifyClusterCommands.EZ_MODE_INVOKE_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return new byte[] {(byte) action.ordinal()};
	}

	@Override
	public EzModeInvoke fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 1, "payload length is incorrect");
		action = EzModeInvokeAction.values()[payload[0]];
		return this;
	}
}
