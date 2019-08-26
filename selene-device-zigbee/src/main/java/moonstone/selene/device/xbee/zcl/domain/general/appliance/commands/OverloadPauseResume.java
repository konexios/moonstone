package moonstone.selene.device.xbee.zcl.domain.general.appliance.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class OverloadPauseResume extends NoPayloadCommand {
	@Override
	protected int getId() {
		return ApplianceControlClusterCommands.OVERLOAD_PAUSE_RESUME_COMMAND_ID;
	}
}
