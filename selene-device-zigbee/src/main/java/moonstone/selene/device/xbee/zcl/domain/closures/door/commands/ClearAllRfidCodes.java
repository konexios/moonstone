package moonstone.selene.device.xbee.zcl.domain.closures.door.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class ClearAllRfidCodes extends NoPayloadCommand {
	@Override
	protected int getId() {
		return DoorLockClusterCommands.CLEAR_ALL_RFIDS_COMMAND_ID;
	}
}
