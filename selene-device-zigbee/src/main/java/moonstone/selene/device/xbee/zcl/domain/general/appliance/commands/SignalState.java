package moonstone.selene.device.xbee.zcl.domain.general.appliance.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class SignalState extends NoPayloadCommand {
	@Override
	protected int getId() {
		return ApplianceControlClusterCommands.SIGNAL_STATE_COMMAND_ID;
	}
}
