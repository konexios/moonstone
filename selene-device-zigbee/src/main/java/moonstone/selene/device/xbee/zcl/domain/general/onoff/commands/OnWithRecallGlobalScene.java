package moonstone.selene.device.xbee.zcl.domain.general.onoff.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class OnWithRecallGlobalScene extends NoPayloadCommand {
	@Override
	protected int getId() {
		return OnOffClusterCommands.ON_WITH_RECALL_GLOBAL_SCENE_COMMAND_ID;
	}
}
