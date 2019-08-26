package moonstone.selene.device.xbee.zcl.domain.general.appliance.commands;

import org.apache.commons.lang3.Validate;

import moonstone.selene.device.xbee.zcl.ClusterSpecificCommand;

public class OverloadWarning extends ClusterSpecificCommand<OverloadWarning> {
	private byte[] warningEvent = new byte[1];

	public byte[] getWarningEvent() {
		return warningEvent;
	}

	public OverloadWarning withWarningEvent(byte warningEvent) {
		this.warningEvent[0] = warningEvent;
		return this;
	}

	@Override
	protected int getId() {
		return ApplianceControlClusterCommands.OVERLOAD_WARNING_COMMAND_ID;
	}

	@Override
	public byte[] toPayload() {
		return warningEvent;
	}

	@Override
	public OverloadWarning fromPayload(byte[] payload) {
		Validate.notNull(payload, "payload is null");
		Validate.isTrue(payload.length == 1, "payload length is incorrect");
		warningEvent = payload;
		return this;
	}
}
