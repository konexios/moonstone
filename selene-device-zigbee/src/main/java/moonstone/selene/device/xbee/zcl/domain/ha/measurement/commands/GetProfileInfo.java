package moonstone.selene.device.xbee.zcl.domain.ha.measurement.commands;

import moonstone.selene.device.xbee.zcl.NoPayloadCommand;

public class GetProfileInfo extends NoPayloadCommand {
	@Override
	protected int getId() {
		return ElectricalMeasurementClusterCommands.GET_PROFILE_INFO_COMMAND_COMMAND_ID;
	}
}
