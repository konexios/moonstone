package moonstone.selene.device.xbee.zcl.domain.general.powerprofile.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class PowerProfileStateRequest extends NoPayloadCommand {
	@Override
	protected int getId() {
		return PowerProfileClusterCommands.POWER_PROFILE_STATE_REQUEST_COMMAND_ID;
	}
}
