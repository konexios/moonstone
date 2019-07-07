package com.arrow.selene.device.xbee.zcl.domain.se.metering.commands;

import org.apache.commons.lang3.Validate;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;
import com.arrow.selene.device.xbee.zcl.domain.se.metering.data.LocalSupplyStatus;

public class LocalChangeSupply extends ClusterSpecificCommand<LocalChangeSupply> {
	private LocalSupplyStatus proposedSupplyStatus;

	public LocalSupplyStatus getProposedSupplyStatus() {
		return proposedSupplyStatus;
	}

	public LocalChangeSupply withProposedSupplyStatus(LocalSupplyStatus proposedSupplyStatus) {
		this.proposedSupplyStatus = proposedSupplyStatus;
		return this;
	}

	@Override
	protected int getId() {
		return SimpleMeteringClusterCommands.LOCAL_CHANGE_SUPPLY_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return new byte[]{(byte) proposedSupplyStatus.ordinal()};
	}

	@Override
	public LocalChangeSupply fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 1, "payload length is incorrect");
		proposedSupplyStatus = LocalSupplyStatus.values()[payload[0]];
		return this;
	}
}
